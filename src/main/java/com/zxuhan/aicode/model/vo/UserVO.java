package com.zxuhan.aicode.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Desensitized user info.
 */
@Data
public class UserVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * Account.
     */
    private String userAccount;

    /**
     * Display name.
     */
    private String userName;

    /**
     * Avatar URL.
     */
    private String userAvatar;

    /**
     * User profile / bio.
     */
    private String userProfile;

    /**
     * User role: user/admin.
     */
    private String userRole;

    /**
     * Creation time.
     */
    private LocalDateTime createTime;

    private static final long serialVersionUID = 1L;
}