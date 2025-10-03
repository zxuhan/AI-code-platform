package com.zxuhan.aicodebackend.model.dto.user;

import com.zxuhan.aicodebackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageRequest implements Serializable {

    /**
     * user id
     */
    private Long id;

    /**
     * user name
     */
    private String userName;

    /**
     * user account
     */
    private String userAccount;

    /**
     * user profile
     */
    private String userProfile;

    /**
     * user role：user/admin/ban
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}