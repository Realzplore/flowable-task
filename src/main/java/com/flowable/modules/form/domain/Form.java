package com.flowable.modules.form.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.flowable.core.domain.FtDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 表单
 * @Author: liping.zheng
 * @Date: 2018/8/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ft_form")
public class Form extends FtDomain {
    @TableField
    private String formName;

    @TableField
    @JsonSerialize(using = ToStringSerializer.class)
    Long tenantId;
}
