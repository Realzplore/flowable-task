package com.flowable.service;

import com.flowable.modules.expense.domain.Expense;
import com.flowable.modules.expense.service.ExpenseService;
import com.flowable.security.SecurityUtil;
import org.flowable.engine.*;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: liping.zheng
 * @Date: 2018/8/10
 */
@Service
public class ExpenseProcessService {
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ExpenseService expenseService;

    @Transactional
    public Expense startProcessInstanceByKey(String processInstanceKey, Expense expense) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("userId", Optional.ofNullable(expense.getUserId()).orElse(SecurityUtil.getCurrentUserId()));
        variables.put("money", expense.getMoney());
        variables.put("count", 1);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processInstanceKey, variables);
        expense.setUserId(Optional.ofNullable(expense.getUserId()).orElse(SecurityUtil.getCurrentUserId()));
        expense.setProcessInstanceId(processInstance.getId());
        return expenseService.insert(expense);
    }

    public List<Expense> getRuntimeExpenseList(List<String> processInstanceIdList) {
        return processInstanceIdList.stream().map(f -> expenseService.selectByProcessInstanceId(f)).collect(Collectors.toList());
    }

}
