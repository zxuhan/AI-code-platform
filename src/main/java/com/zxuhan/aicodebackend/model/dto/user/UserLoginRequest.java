package com.zxuhan.aicodebackend.model.dto.user;


import lombok.Data;

import java.io.Serializable;

/**
 * User login
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * user account
     */
    private String userAccount;

    /**
     * user password
     */
    private String userPassword;
}
