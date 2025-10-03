package com.zxuhan.aicodebackend.model.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.zxuhan.aicodebackend.model.dto.app.AppQueryRequest;
import com.zxuhan.aicodebackend.model.entity.App;
import com.zxuhan.aicodebackend.model.entity.User;
import com.zxuhan.aicodebackend.model.vo.AppVO;
import reactor.core.publisher.Flux;

import java.util.List;


/**
 * App service layer
 */
public interface AppService extends IService<App> {

    /**
     * Generate application code through conversation
     *
     * @param appId Application ID
     * @param message Prompt message
     * @param loginUser login user
     * @return
     */
    Flux<String> chatToGenCode(Long appId, String message, User loginUser);

    /**
     * App deploy
     *
     * @param appId app ID
     * @param loginUser login user
     * @return accessible deploy URL
     */
    String deployApp(Long appId, User loginUser);


    /**
     * Get AppVO
     *
     * @param app
     * @return
     */
    AppVO getAppVO(App app);

    /**
     * Get AppVO list (paginated)
     *
     * @param appList
     * @return
     */
    List<AppVO> getAppVOList(List<App> appList);

    /**
     * Construct data query parameters based on query conditions
     *
     * @param appQueryRequest
     * @return
     */
    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);


}