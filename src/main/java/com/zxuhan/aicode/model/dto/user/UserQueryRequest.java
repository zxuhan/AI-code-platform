package com.zxuhan.aicode.model.dto.user;

import com.zxuhan.aicode.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * Display name.
     */
    private String userName;

    /**
     * Account.
     */
    private String userAccount;

    /**
     * Profile / bio.
     */
    private String userProfile;

    /**
     * User role: user/admin/ban.
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}