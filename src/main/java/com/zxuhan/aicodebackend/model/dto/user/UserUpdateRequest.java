package com.zxuhan.aicodebackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * User update request
 */
@Data

public class UserUpdateRequest implements Serializable {

    /**
     * user id
     */
    private Long id;

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

    private static final long serialVersionUID = 1L;
}