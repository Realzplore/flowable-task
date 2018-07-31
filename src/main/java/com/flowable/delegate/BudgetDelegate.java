package com.flowable.delegate;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: liping.zheng
 * @Date: 2018/7/23
 */
public class BudgetDelegate implements JavaDelegate {
    private static final Logger log = LoggerFactory.getLogger(BudgetDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        log.info("execution Id : {}", execution.getId());
        log.info("serviceTask : check budget is running.");
    }
}
