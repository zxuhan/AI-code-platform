package com.zxuhan.aicodebackend.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.zxuhan.aicodebackend.model.dto.user.UserQueryRequest;
import com.zxuhan.aicodebackend.model.entity.User;
import com.zxuhan.aicodebackend.model.vo.LoginUserVO;
import com.zxuhan.aicodebackend.model.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * User service layer
 */
public interface UserService extends IService<User> {


    /**
     * User registration
     *
     * @param userAccount   user account
     * @param userPassword  user password
     * @param checkPassword check password
     * @return new user id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * Get logged-in user information
     *
     * @return
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * User login
     *
     * @param userAccount  user account
     * @param userPassword user password
     * @param request
     * @return user information
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * Get current logged-in user
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * Get user information
     *
     * @param user user information
     * @return
     */
    UserVO getUserVO(User user);

    /**
     * Get user information (paginated)
     *
     * @param userList user list
     * @return
     */
    List<UserVO> getUserVOList(List<User> userList);

    /**
     * User logout
     *
     * @param request
     * @return whether logout is successful
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * Construct data query parameters based on query conditions
     *
     * @param userQueryRequest
     * @return
     */
    QueryWrapper getQueryWrapper(UserQueryRequest userQueryRequest);



    /**
     * Encrypt
     *
     * @param userPassword user password
     * @return encrypted user password
     */
    String getEncryptPassword(String userPassword);
}
