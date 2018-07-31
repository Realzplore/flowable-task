package com.flowable.delegate;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: liping.zheng
 * @Date: 2018/7/24
 */
public class RejectDelegate implements JavaDelegate {
    private static final Logger log = LoggerFactory.getLogger(RejectDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        log.info("报销驳回，已发送邮件-----------------------------");
    }
}
