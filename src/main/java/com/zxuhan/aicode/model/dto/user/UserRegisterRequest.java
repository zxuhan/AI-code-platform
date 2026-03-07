package com.zxuhan.aicode.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * User registration request.
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Account.
     */
    private String userAccount;

    /**
     * Password.
     */
    private String userPassword;

    /**
     * Confirmation of the password.
     */
    private String checkPassword;
}