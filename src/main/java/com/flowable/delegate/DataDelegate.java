package com.flowable.delegate;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @Author: liping.zheng
 * @Date: 2018/7/23
 */
public class DataDelegate implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(DataDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        log.info("execution Id :{}", execution.getId());
        log.info("serviceTask : saveData is running.");
    }
}
