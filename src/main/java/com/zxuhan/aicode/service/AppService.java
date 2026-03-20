package com.zxuhan.aicode.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.zxuhan.aicode.model.dto.app.AppAddRequest;
import com.zxuhan.aicode.model.dto.app.AppQueryRequest;
import com.zxuhan.aicode.model.entity.App;
import com.zxuhan.aicode.model.entity.User;
import com.zxuhan.aicode.model.vo.AppVO;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * Application service.
 */
public interface AppService extends IService<App> {

    /**
     * Generate application code via chat.
     *
     * @param appId     application id
     * @param message   user prompt
     * @param loginUser logged-in user
     * @return streaming chat response
     */
    Flux<String> chatToGenCode(Long appId, String message, User loginUser);

    /**
     * Create an application.
     *
     * @param appAddRequest add request
     * @param loginUser     logged-in user
     * @return new application id
     */
    Long createApp(AppAddRequest appAddRequest, User loginUser);

    /**
     * Deploy an application.
     *
     * @param appId     application id
     * @param loginUser logged-in user
     * @return deployment URL
     */
    String deployApp(Long appId, User loginUser);

    /**
     * Asynchronously generate the application screenshot and update its cover.
     *
     * @param appId  application id
     * @param appUrl application URL
     */
    void generateAppScreenshotAsync(Long appId, String appUrl);

    /**
     * Build the application VO.
     *
     * @param app application entity
     * @return application VO
     */
    AppVO getAppVO(App app);

    /**
     * Build the application VO list.
     *
     * @param appList application entity list
     * @return application VO list
     */
    List<AppVO> getAppVOList(List<App> appList);

    /**
     * Build a query wrapper from the request.
     *
     * @param appQueryRequest query parameters
     * @return query wrapper
     */
    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

}
