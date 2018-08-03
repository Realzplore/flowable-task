package com.flowable.modules.flowableChain.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.flowable.core.domain.FtDomain;
import lombok.Data;

/**
 * 审批链
 *
 * @Author: liping.zheng
 * @Date: 2018/8/3
 */
@Data
@TableName("ft_flowable_chain")
public class FlowableChain extends FtDomain {
    /**
     * 表单Id
     */
    @TableField
    @JsonSerialize(using = ToStringSerializer.class)
    private Long formId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long currentNodeId;
}
