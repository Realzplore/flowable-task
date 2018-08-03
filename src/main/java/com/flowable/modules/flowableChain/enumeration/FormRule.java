package com.flowable.modules.flowableChain.enumeration;

/**
 * @Author: liping.zheng
 * @Date: 2018/8/3
 */
public enum FormRule {
    /**
     * 加签
     */
    SIGN(1001),
    /**
     * 自选审批人
     */
    OPTION(1002),
    /**
     * 重复审批
     */
    REPEAT(1003),
    /**
     * 代理审批
     */
    PROXY(1004)
    ;

    Integer formRule;

    FormRule(Integer formRule) {
        this.formRule = formRule;
    }
}
