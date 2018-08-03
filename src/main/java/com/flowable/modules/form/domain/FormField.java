package com.flowable.modules.form.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.flowable.core.domain.BaseInfoFtDomain;
import com.flowable.modules.form.enumeration.FieldType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 表单属性
 *
 * @Author: liping.zheng
 * @Date: 2018/8/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ft_form_field")
public class FormField extends BaseInfoFtDomain {
    @TableField
    @JsonSerialize(using = ToStringSerializer.class)
    Long formId;

    private String fieldName;

    private FieldType fieldType;

    private String dataSource;
}
