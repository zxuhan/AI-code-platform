package com.zxuhan.aicode.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.zxuhan.aicode.model.dto.user.UserQueryRequest;
import com.zxuhan.aicode.model.entity.User;
import com.zxuhan.aicode.model.vo.LoginUserVO;
import com.zxuhan.aicode.model.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * User service.
 */
public interface UserService extends IService<User> {

    /**
     * Register a user.
     *
     * @param userAccount   user account
     * @param userPassword  user password
     * @param checkPassword password confirmation
     * @return id of the newly created user
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * Get the desensitized logged-in user VO.
     *
     * @param user user entity
     * @return logged-in user VO
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * Log a user in.
     *
     * @param userAccount  user account
     * @param userPassword user password
     * @param request      HTTP request
     * @return desensitized user info
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * Get the currently logged-in user.
     *
     * @param request HTTP request
     * @return user entity
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * Get the desensitized user VO.
     *
     * @param user user entity
     * @return user VO
     */
    UserVO getUserVO(User user);

    /**
     * Get a list of desensitized user VOs.
     *
     * @param userList list of users
     * @return list of user VOs
     */
    List<UserVO> getUserVOList(List<User> userList);

    /**
     * Log the user out.
     *
     * @param request HTTP request
     * @return true if logout succeeded
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * Build a query wrapper from the request.
     *
     * @param userQueryRequest query parameters
     * @return query wrapper
     */
    QueryWrapper getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * Encrypt the user password.
     *
     * @param userPassword raw password
     * @return encrypted password
     */
    String getEncryptPassword(String userPassword);
}
