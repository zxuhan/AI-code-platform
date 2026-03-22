package com.zxuhan.aicode.controller;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.zxuhan.aicode.annotation.AuthCheck;
import com.zxuhan.aicode.common.BaseResponse;
import com.zxuhan.aicode.common.ResultUtils;
import com.zxuhan.aicode.constant.UserConstant;
import com.zxuhan.aicode.exception.ErrorCode;
import com.zxuhan.aicode.exception.ThrowUtils;
import com.zxuhan.aicode.model.dto.chathistory.ChatHistoryQueryRequest;
import com.zxuhan.aicode.model.entity.ChatHistory;
import com.zxuhan.aicode.model.entity.User;
import com.zxuhan.aicode.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import com.zxuhan.aicode.service.ChatHistoryService;

import java.time.LocalDateTime;

/**
 * Chat history controller.
 */
@RestController
@RequestMapping("/chatHistory")
public class ChatHistoryController {

    @Resource
    private ChatHistoryService chatHistoryService;

    @Resource
    private UserService userService;

    /**
     * Cursor-based paginated query of chat history for a given application.
     *
     * @param appId          application ID
     * @param pageSize       page size
     * @param lastCreateTime creation time of the last record
     * @param request        HTTP request
     * @return paginated chat history
     */
    @GetMapping("/app/{appId}")
    public BaseResponse<Page<ChatHistory>> listAppChatHistory(@PathVariable Long appId,
                                                              @RequestParam(defaultValue = "10") int pageSize,
                                                              @RequestParam(required = false) LocalDateTime lastCreateTime,
                                                              HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Page<ChatHistory> result = chatHistoryService.listAppChatHistoryByPage(appId, pageSize, lastCreateTime, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * Admin: paginated list of all chat histories.
     *
     * @param chatHistoryQueryRequest query request
     * @return paginated chat history
     */
    @PostMapping("/admin/list/page/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<ChatHistory>> listAllChatHistoryByPageForAdmin(@RequestBody ChatHistoryQueryRequest chatHistoryQueryRequest) {
        ThrowUtils.throwIf(chatHistoryQueryRequest == null, ErrorCode.PARAMS_ERROR);
        long pageNum = chatHistoryQueryRequest.getPageNum();
        long pageSize = chatHistoryQueryRequest.getPageSize();
        // Query the data
        QueryWrapper queryWrapper = chatHistoryService.getQueryWrapper(chatHistoryQueryRequest);
        Page<ChatHistory> result = chatHistoryService.page(Page.of(pageNum, pageSize), queryWrapper);
        return ResultUtils.success(result);
    }
}
