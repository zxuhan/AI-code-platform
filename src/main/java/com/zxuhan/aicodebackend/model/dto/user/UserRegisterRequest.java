package com.zxuhan.aicodebackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * User register request
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * user account
     */
    private String userAccount;

    /**
     * user password
     */
    private String userPassword;


    /**
     * check password
     */
    private String checkPassword;
}
