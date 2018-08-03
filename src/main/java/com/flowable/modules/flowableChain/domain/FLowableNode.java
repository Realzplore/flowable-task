package com.flowable.modules.flowableChain.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.flowable.core.domain.FtDomain;
import com.flowable.modules.flowableChain.enumeration.NodeType;
import lombok.Data;

/**
 * @Author: liping.zheng
 * @Date: 2018/8/3
 */
@Data
@TableName("ft_flwoable_node")
public class FLowableNode extends FtDomain {
    @TableField
    Long flowableChainId;

    /**
     * 节点类型
     */
    @TableField
    NodeType nodeType;

    /**
     * 下一审批链节点
     */
    @TableField
    Long nextFlowableNode;



}
