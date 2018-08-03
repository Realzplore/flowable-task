package com.flowable.modules.flowableChain.enumeration;

/**
 * 审批节点类型
 * @Author: liping.zheng
 * @Date: 2018/8/3
 */
public enum NodeType {
    START_EVENT("startEvent"),
    APPROVAL_TASK("approvalTask"),
    SERVICE_TASK("serviceTask"),
    NOTIFY_TASK("notifyTask"),
    END_EVENT("endEvent");


    String type;

    NodeType(String type) {
        this.type = type;
    }
}
