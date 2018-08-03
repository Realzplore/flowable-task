package com.flowable.modules.form.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.flowable.core.domain.BaseInfoFtDomain;
import com.flowable.modules.form.enumeration.FieldType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 具体表单
 * @Author: liping.zheng
 * @Date: 2018/8/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ft_form_field_value")
public class FormFieldValue extends BaseInfoFtDomain {
    @TableField
    private Long formFieldId;

    private FieldType fieldType;

    private Integer intValue;

    private Double doubleValue;

    private Long longValue;

    private String stringValue;

    private Boolean booleanValue;
}
