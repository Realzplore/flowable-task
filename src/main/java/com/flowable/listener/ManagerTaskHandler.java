package com.flowable.listener;

import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @Author: liping.zheng
 * @Date: 2018/7/11
 */
public class ManagerTaskHandler implements TaskListener {
    private static final Logger log = LoggerFactory.getLogger(ManagerTaskHandler.class);

    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("user Task executing-------------------------");
        log.info("userTask Id: {}", delegateTask.getId());
        Map<String, Object> params = delegateTask.getTransientVariables();
        Double money = (Double) params.get("money");
        log.info("expense money : {}", money);
        if (money < 500) {
            delegateTask.setAssignee("manager");
        } else {
            delegateTask.setAssignee("boss");
        }
        log.info("task assignee : {}", delegateTask.getAssignee());
    }
}
