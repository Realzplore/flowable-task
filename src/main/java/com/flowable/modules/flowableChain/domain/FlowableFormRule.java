package com.flowable.modules.flowableChain.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.flowable.core.domain.FtDomain;
import com.flowable.modules.flowableChain.enumeration.FormRule;
import lombok.Data;

/**
 * 审批链单据规则
 * @Author: liping.zheng
 * @Date: 2018/8/3
 */
@Data
@TableName("ft_flowable_form_rule")
public class FlowableFormRule extends FtDomain {
    @TableField
    Long formId;

    @TableField
    FormRule formRule;
}
