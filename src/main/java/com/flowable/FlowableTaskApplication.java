package com.flowable;

import org.flowable.dmn.api.*;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@SpringBootApplication
@MapperScan("com.flowable")
public class FlowableTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowableTaskApplication.class, args);
    }


    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private DmnRuleService dmnRuleService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private DmnRepositoryService dmnRepositoryService;


    private static final Logger log = LoggerFactory.getLogger(FlowableTaskApplication.class);


    @Component
    @Order(1)
    class DeploymentRunner implements CommandLineRunner{

        @Override
        public void run(String... args) throws Exception {
//            //部署DMN
//            //EXP_PRO_1
//            DmnDeployment dmnDeployment = dmnRepositoryService.createDeploymentQuery().decisionTableKey("EXP_PRO_1").singleResult();
//            if (dmnDeployment != null) {
//                dmnRepositoryService.deleteDeployment(dmnDeployment.getId());
//            }
//            dmnDeployment = dmnRepositoryService.createDeployment().addClasspathResource("dmn/Expense_Process.dmn").deploy();
//            DmnDecisionTable dmnDecisionTable = dmnRepositoryService.createDecisionTableQuery().decisionTableKey("EXP_PRO_1").singleResult();
//            log.info("dmn deployment key : {}, id : {}", dmnDecisionTable.getKey(), dmnDeployment.getId());
//            log.info("decision table key : {}, Id : {}", dmnDecisionTable.getKey(), dmnDecisionTable.getId());
//
//            //EXP_PRO_2
//            DmnDeployment dmnDeployment2 = dmnRepositoryService.createDeploymentQuery().decisionTableKey("EXP_PRO_2").singleResult();
//            if (dmnDeployment2 != null) {
//                dmnRepositoryService.deleteDeployment(dmnDeployment2.getId());
//            }
//            dmnDeployment2 = dmnRepositoryService.createDeployment().addClasspathResource("dmn/Expense_Process_2.dmn").deploy();
//            DmnDecisionTable dmnDecisionTable2 = dmnRepositoryService.createDecisionTableQuery().decisionTableKey("EXP_PRO_2").singleResult();
//            log.info("dmn deployment key : {}, id : {}", dmnDecisionTable2.getKey(), dmnDeployment2.getId());
//            log.info("decision table key : {}, Id : {}", dmnDecisionTable2.getKey(), dmnDecisionTable2.getId());
        }
    }

    /**
     * TestExpense
     *
     * @return
     */
//    @Component
    @Order(2)
    class  TestCommandLineRunner implements CommandLineRunner {
        @Override
        public void run(String... args) throws Exception {
            Scanner scanner = new Scanner(System.in);
            System.out.println("money: ");
            Double money = scanner.nextDouble();
            //启动流程实例
            Map<String, Object> map = new HashMap<>();
            map.put("money", money);
            log.info("流程实例启动--------------start process");
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Test_Expense", map);
            log.info("流程Id: {}", processInstance.getId());

            //流程实例到userTask
            Task task = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult();
            Map<String, Object> processVariables = taskService.getVariables(task.getId());
            log.info("task variables : {}", processVariables.toString());
            log.info("task assignee : {}", task.getAssignee());

            //完成任务
            Map<String, Object> result = new HashMap<>();
            result.put("approved", "yes");
            taskService.complete(task.getId(), result);
            log.info("task is completed.");
            log.info("Number of tasks : {}", taskService.createTaskQuery().count());
        }
    }


    /**
     * TestExpense2
     */
//    @Component
    @Order(3)
    class Test2CommandLineRunner implements CommandLineRunner {
        @Override
        public void run(String... args) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("name : ");
            String name = scanner.nextLine();
            System.out.println("expense money : ");
            Double money = scanner.nextDouble();

            //启动流程
            Map<String, Object> map = new HashMap<>();
            map.put("employee", name);
            map.put("money", money);
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Test_Expense_2", map);
            log.info("启动流程实例-----------------------------------");
            log.info("流程实例Id : {}", processInstance.getId());
//            log.info("执行实例Id : {}", execution.getId());

            //到达审批流程
            log.info("到达审批任务流程-------------------------------");
            Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
            log.info("任务实例Id : {}", task.getId());
            map = taskService.getVariables(task.getId());
            log.info("任务变量 : {}", map.toString());
            Map<String, Object> approvedMap = new HashMap<>();
            Double expenseMoney = (Double) map.get("money");
            approvedMap.put("approved", (expenseMoney <= 500) ? Boolean.TRUE : Boolean.FALSE);
            approvedMap.put("employee", map.get("employee"));
            log.info("审批任务完成-----------------------------------");
            log.info("审批结果 : {}", approvedMap.get("approved"));
            taskService.complete(task.getId(), approvedMap);

            //根据审批结果判断流程走向
            if ((Boolean) approvedMap.get("approved")) {
                log.info("申请人确认------------------------");
                task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskAssignee((String) approvedMap.get("employee")).singleResult();
                log.info("task Id : {}, assignee : {}", task.getId(), task.getAssignee());
                taskService.complete(task.getId());
                log.info("申请人确认完成------------------------");
            } else {
                log.info("报销驳回，已发送邮件-----------------------------");
            }
        }
    }


    /**
     * TestExpense3
     */
//    @Component
    @Order(4)
    class Test3CommandLineRunner implements CommandLineRunner {
        @Override
        public void run(String... args) throws Exception {
            //输入参数
            Scanner scanner = new Scanner(System.in);
            System.out.println("审批是否通过 : (Y/N)");
            String approved = scanner.nextLine();
            System.out.println("money : ");
            Double money = scanner.nextDouble();


            Map<String, Object> map = new HashMap<>();
            map.put("money", money);

            //启动流程实例
            log.info("启动流程实例------------------------------");
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Test_Expense_3", map);
            log.info("流程实例Id : {}", processInstance.getId());

            //进入决策表

            //根据决策表完成决定任务分配人
            log.info("根据决策表完成任务分配人-------------------");
            Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
            log.info("user task assignee : {}", task.getAssignee());

            //到达审批流程
            Map<String, Object> results = new HashMap<>();
            results.put("approved", ("Y".equals(approved)) ? Boolean.TRUE : Boolean.FALSE);
            log.info("审批任务完成-----------------------------------");
            log.info("审批结果 : {}", results.get("approved"));
            taskService.complete(task.getId(), results);

            //根据审批结果判断流程走向
        }
    }
}
