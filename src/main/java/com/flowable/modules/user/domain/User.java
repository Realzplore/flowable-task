package com.flowable.modules.user.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.flowable.core.domain.FtDomain;
import lombok.Data;

/**
 * @Author: liping.zheng
 * @Date: 2018/7/27
 */
@Data
@TableName("ft_user")
public class User extends FtDomain {
    /**
     * 用户名称
     */
    @TableField
    private String name;

    /**
     * 人员架构组Id
     */
    @TableField
    private Long groupId;

    /**
     * 职业信息Id
     */
    @TableField
    private Long professionInfoId;

    /**
     * 上级Id
     */
    @TableField
    private Long parentUserId;
}
