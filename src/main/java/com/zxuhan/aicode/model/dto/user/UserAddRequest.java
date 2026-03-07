package com.zxuhan.aicode.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * Create-user request.
 */
@Data
public class UserAddRequest implements Serializable {

    /**
     * Display name.
     */
    private String userName;

    /**
     * Account.
     */
    private String userAccount;

    /**
     * Avatar URL.
     */
    private String userAvatar;

    /**
     * User profile / bio.
     */
    private String userProfile;

    /**
     * User role: user, admin.
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}