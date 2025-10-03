package com.zxuhan.aicodebackend.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;


/**
 * user info after login
 */
@Data
public class LoginUserVO implements Serializable {

    /**
     * user id
     */
    private Long id;

    /**
     * user account
     */
    private String userAccount;

    /**
     * user name
     */
    private String userName;

    /**
     * user avatar
     */
    private String userAvatar;

    /**
     * user profile
     */
    private String userProfile;

    /**
     * user role：user/admin
     */
    private String userRole;

    /**
     * create time
     */
    private Instant createTime;

    /**
     * update time
     */
    private Instant updateTime;

    private static final long serialVersionUID = 1L;
}