package com.zxuhan.aicodebackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * User add request
 */
@Data
public class UserAddRequest implements Serializable {

    /**
     * user name
     */
    private String userName;

    /**
     * user account
     */
    private String userAccount;

    /**
     * user avatar
     */
    private String userAvatar;

    /**
     * user profile
     */
    private String userProfile;

    /**
     * user role: user, admin
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}