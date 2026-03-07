package com.zxuhan.aicode.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * Update-user request.
 */
@Data
public class UserUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * Display name.
     */
    private String userName;

    /**
     * Avatar URL.
     */
    private String userAvatar;

    /**
     * Profile / bio.
     */
    private String userProfile;

    /**
     * User role: user/admin.
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}