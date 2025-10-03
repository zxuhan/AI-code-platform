package com.zxuhan.aicodebackend.controller;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.zxuhan.aicodebackend.annotation.AuthCheck;
import com.zxuhan.aicodebackend.common.BaseResponse;
import com.zxuhan.aicodebackend.common.ResultUtils;
import com.zxuhan.aicodebackend.constant.UserConstant;
import com.zxuhan.aicodebackend.exception.ErrorCode;
import com.zxuhan.aicodebackend.exception.ThrowUtils;
import com.zxuhan.aicodebackend.model.dto.chathistory.ChatHistoryQueryRequest;
import com.zxuhan.aicodebackend.model.entity.ChatHistory;
import com.zxuhan.aicodebackend.model.entity.User;
import com.zxuhan.aicodebackend.model.service.ChatHistoryService;
import com.zxuhan.aicodebackend.model.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

/**
 * Chat history controller
 */
@RestController
@RequestMapping("/chatHistory")
@RequiredArgsConstructor
public class ChatHistoryController {


    private final ChatHistoryService chatHistoryService;

    private final UserService userService;

    /**
     * Paginated query of chat history for a specific application (cursor-based pagination)
     *
     * @param appId          Application ID
     * @param pageSize       Page size
     * @param lastCreateTime Creation time of the last record
     * @param request        Request
     * @return Paginated chat history
     */
    @GetMapping("/app/{appId}")
    public BaseResponse<Page<ChatHistory>> listAppChatHistory(@PathVariable Long appId,
                                                              @RequestParam(defaultValue = "10") int pageSize,
                                                              @RequestParam(required = false) Instant lastCreateTime,
                                                              HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Page<ChatHistory> result = chatHistoryService.listAppChatHistoryByPage(appId, pageSize, lastCreateTime, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * Admin paginated query of all chat history
     *
     * @param chatHistoryQueryRequest Query request
     * @return Paginated chat history
     */
    @PostMapping("/admin/list/page/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<ChatHistory>> listAllChatHistoryByPageForAdmin(@RequestBody ChatHistoryQueryRequest chatHistoryQueryRequest) {
        ThrowUtils.throwIf(chatHistoryQueryRequest == null, ErrorCode.PARAMS_ERROR);
        long pageNum = chatHistoryQueryRequest.getPageNum();
        long pageSize = chatHistoryQueryRequest.getPageSize();
        // Query database
        QueryWrapper queryWrapper = chatHistoryService.getQueryWrapper(chatHistoryQueryRequest);
        Page<ChatHistory> result = chatHistoryService.page(Page.of(pageNum, pageSize), queryWrapper);
        return ResultUtils.success(result);
    }
}