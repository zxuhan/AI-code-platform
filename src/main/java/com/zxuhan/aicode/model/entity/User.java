package com.zxuhan.aicode.model.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

import java.io.Serial;

import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("user")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long id;

    /**
     * Account.
     */
    @Column("userAccount")
    private String userAccount;

    /**
     * Password.
     */
    @Column("userPassword")
    private String userPassword;

    /**
     * Display name.
     */
    @Column("userName")
    private String userName;

    /**
     * Avatar URL.
     */
    @Column("userAvatar")
    private String userAvatar;

    /**
     * User profile / bio.
     */
    @Column("userProfile")
    private String userProfile;

    /**
     * User role: user/admin.
     */
    @Column("userRole")
    private String userRole;

    /**
     * Edit time.
     */
    @Column("editTime")
    private LocalDateTime editTime;

    /**
     * Creation time.
     */
    @Column("createTime")
    private LocalDateTime createTime;

    /**
     * Update time.
     */
    @Column("updateTime")
    private LocalDateTime updateTime;

    /**
     * Logical delete flag.
     */
    @Column(value = "isDelete", isLogicDelete = true)
    private Integer isDelete;

}
