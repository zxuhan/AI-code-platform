package com.zxuhan.aicodebackend.model.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * User ID
     */
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long id;

    /**
     * User account
     */
    @Column("user_account")
    private String userAccount;

    /**
     * User password
     */
    @Column("user_password")
    private String userPassword;

    /**
     * User display name
     */
    @Column("user_name")
    private String userName;

    /**
     * User avatar image URL
     */
    @Column("user_avatar")
    private String userAvatar;

    /**
     * User profile
     */
    @Column("user_profile")
    private String userProfile;

    /**
     * User role: user/admin
     */
    @Column("user_role")
    private String userRole;

    /**
     * Last edit time
     */
    @Column("edit_time")
    private Instant editTime;

    /**
     * Record creation time
     */
    @Column("create_time")
    private Instant createTime;

    /**
     * Record update time
     */
    @Column("update_time")
    private Instant updateTime;

    /**
     * Soft delete flag (0: not deleted, 1: deleted)
     */
    @Column(value = "is_delete", isLogicDelete = true)
    private Integer isDelete;
}