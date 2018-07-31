package com.flowable.listener;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @Author: liping.zheng
 * @Date: 2018/7/25
 */
public class ApproveTaskHandler implements TaskListener {
    private static final Logger log = LoggerFactory.getLogger(ApproveTaskHandler.class);
    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("进入决策表--------------------------------");
        log.info("决策表执行完成----------------------------");
        log.info("task Id : {}", delegateTask.getId());
        Map<String, Object> params = delegateTask.getTransientVariables();
        log.info("task variables : {}", params.toString());
        delegateTask.setAssignee((String)params.get("decisionPerson"));
    }
}
