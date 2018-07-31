package com.flowable.delegate;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: liping.zheng
 * @Date: 2018/7/24
 */
public class ExpenseDelegate implements JavaDelegate {
    private static final Logger log = LoggerFactory.getLogger(ExpenseDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        log.info("审批已通过，请确认----------------------");
    }
}
