package com.zxuhan.aicodebackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.zxuhan.aicodebackend.constant.AppConstant;
import com.zxuhan.aicodebackend.core.AiCodeGeneratorFacade;
import com.zxuhan.aicodebackend.exception.BusinessException;
import com.zxuhan.aicodebackend.exception.ErrorCode;
import com.zxuhan.aicodebackend.exception.ThrowUtils;
import com.zxuhan.aicodebackend.mapper.AppMapper;
import com.zxuhan.aicodebackend.model.dto.app.AppQueryRequest;
import com.zxuhan.aicodebackend.model.entity.App;
import com.zxuhan.aicodebackend.model.entity.User;
import com.zxuhan.aicodebackend.model.enums.ChatHistoryMessageTypeEnum;
import com.zxuhan.aicodebackend.model.enums.CodeGenTypeEnum;
import com.zxuhan.aicodebackend.service.AppService;
import com.zxuhan.aicodebackend.service.ChatHistoryService;
import com.zxuhan.aicodebackend.service.UserService;
import com.zxuhan.aicodebackend.model.vo.AppVO;
import com.zxuhan.aicodebackend.model.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * App service implementation
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements AppService {

    private final UserService userService;

    private final AiCodeGeneratorFacade aiCodeGeneratorFacade;

    private final ChatHistoryService chatHistoryService;

    @Override
    public Flux<String> chatToGenCode(Long appId, String message, User loginUser) {
        // 1. Parameter validation
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "Application ID error");
        ThrowUtils.throwIf(StrUtil.isBlank(message), ErrorCode.PARAMS_ERROR, "Prompt message cannot be empty");
        // 2. Query application information
        App app = this.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR, "Application does not exist");
        // 3. Permission validation, only the owner can chat with their own application
        if (!app.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "No permission to access this application");
        }
        // 4. Get the code generation type of the application
        String codeGenType = app.getCodeGenType();
        CodeGenTypeEnum codeGenTypeEnum = CodeGenTypeEnum.getEnumByValue(codeGenType);
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "No permission to access this application");
        }
        // 5. Before calling AI, save user message to database first
        chatHistoryService.addChatMessage(appId, message, ChatHistoryMessageTypeEnum.USER.getValue(), loginUser.getId());
        // 6. Call AI to generate code (streaming)
        Flux<String> contentFlux = aiCodeGeneratorFacade.generateAndSaveCodeStream(message, codeGenTypeEnum, appId);
        // 7. Collect AI response content and save record to chat history after completion
        StringBuilder aiResponseBuilder = new StringBuilder();
        return contentFlux.map(chunk -> {
            // Collect AI response content in real-time
            aiResponseBuilder.append(chunk);
            return chunk;
        }).doOnComplete(() -> {
            // After streaming is complete, save AI message to chat history
            String aiResponse = aiResponseBuilder.toString();
            chatHistoryService.addChatMessage(appId, aiResponse, ChatHistoryMessageTypeEnum.AI.getValue(), loginUser.getId());
        }).doOnError(error -> {
            // If AI reply fails, also need to save record to database
            String errorMessage = "AI reply failed：" + error.getMessage();
            chatHistoryService.addChatMessage(appId, errorMessage, ChatHistoryMessageTypeEnum.AI.getValue(), loginUser.getId());
        });
    }

    @Override
    public String deployApp(Long appId, User loginUser) {
        // 1. Parameter validation
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "Application ID error");
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR, "User not logged in");
        // 2. Query application information
        App app = this.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR, "Application does not exist");
        // 3. Permission validation, only the owner can deploy their own application
        if (!app.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "No permission to deploy this application");
        }
        // 4. Check if deployKey already exists
        String deployKey = app.getDeployKey();
        // If not, generate a 6-character deployKey (letters + numbers)
        if (StrUtil.isBlank(deployKey)) {
            deployKey = RandomUtil.randomString(6);
        }
        // 5. Get code generation type, get original code generation path (application access directory)
        String codeGenType = app.getCodeGenType();
        String sourceDirName = codeGenType + "_" + appId;
        String sourceDirPath = AppConstant.CODE_OUTPUT_ROOT_DIR + File.separator + sourceDirName;
        // 6. Check if path exists
        File sourceDir = new File(sourceDirPath);
        if (!sourceDir.exists() || !sourceDir.isDirectory()) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Application code path does not exist, please generate application first");
        }
        // 7. Copy files to deployment directory
        String deployDirPath = AppConstant.CODE_DEPLOY_ROOT_DIR + File.separator + deployKey;
        try {
            FileUtil.copyContent(sourceDir, new File(deployDirPath), true);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Application deployment failed: " + e.getMessage());
        }
        // 8. Update database
        App updateApp = new App();
        updateApp.setId(appId);
        updateApp.setDeployKey(deployKey);
        updateApp.setDeployedTime(Instant.now());
        boolean updateResult = this.updateById(updateApp);
        ThrowUtils.throwIf(!updateResult, ErrorCode.OPERATION_ERROR, "Failed to update application deployment information");
        // 9. Return accessible URL address
        return String.format("%s/%s", AppConstant.CODE_DEPLOY_HOST, deployKey);
    }

    @Override
    public AppVO getAppVO(App app) {
        if (app == null) {
            return null;
        }
        AppVO appVO = new AppVO();
        BeanUtil.copyProperties(app, appVO);
        Long userId = app.getUserId();
        if (userId != null) {
            User user = userService.getById(userId);
            UserVO userVO = userService.getUserVO(user);
            appVO.setUser(userVO);
        }
        return appVO;
    }

    @Override
    public List<AppVO> getAppVOList(List<App> appList) {
        if (CollUtil.isEmpty(appList)) {
            return new ArrayList<>();
        }

        Set<Long> userIds = appList.stream()
                .map(App::getUserId)
                .collect(Collectors.toSet());
        Map<Long, UserVO> userVOMap = userService.listByIds(userIds).stream()
                .collect(Collectors.toMap(User::getId, userService::getUserVO));
        return appList.stream().map(app -> {
            AppVO appVO = getAppVO(app);
            UserVO userVO = userVOMap.get(app.getUserId());
            appVO.setUser(userVO);
            return appVO;
        }).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest) {
        if (appQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "no parameters");
        }
        Long id = appQueryRequest.getId();
        String appName = appQueryRequest.getAppName();
        String cover = appQueryRequest.getCover();
        String initPrompt = appQueryRequest.getInitPrompt();
        String codeGenType = appQueryRequest.getCodeGenType();
        String deployKey = appQueryRequest.getDeployKey();
        Integer priority = appQueryRequest.getPriority();
        Long userId = appQueryRequest.getUserId();
        String sortField = appQueryRequest.getSortField();
        String sortOrder = appQueryRequest.getSortOrder();
        QueryWrapper queryWrapper = QueryWrapper.create()
                .eq("id", id)
                .like("app_name", appName)
                .like("cover", cover)
                .like("init_prompt", initPrompt)
                .eq("code_gen_type", codeGenType)
                .eq("deploy_key", deployKey)
                .eq("priority", priority)
                .eq("user_id", userId);

        // Add sorting with proper column name conversion
        if (StrUtil.isNotBlank(sortField)) {
            // Convert camelCase to snake_case for database
            String dbColumnName = StrUtil.toUnderlineCase(sortField);
            queryWrapper.orderBy(dbColumnName, "ascend".equals(sortOrder));
        } else {
            // Default sort by creation time descending
            queryWrapper.orderBy("create_time", false);
        }

        return queryWrapper;
    }

    /**
     * Delete related chat history when deleting application
     *
     * @param id
     * @return
     */
    @Override
    public boolean removeById(Serializable id) {
        if (id == null) {
            return false;
        }
        long appId = Long.parseLong(id.toString());
        if (appId <= 0) {
            return false;
        }
        // Delete chat history first
        try {
            chatHistoryService.deleteByAppId(appId);
        } catch (Exception e) {
            log.error("Fail to delete chat history：{}", e.getMessage());
        }
        // delete app
        return super.removeById(id);
    }
}
