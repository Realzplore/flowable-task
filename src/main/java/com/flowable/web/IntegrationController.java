package com.flowable.web;

import com.flowable.security.SecurityUtil;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: liping.zheng
 * @Date: 2018/7/20
 */
@RestController
@RequestMapping("/api/public")
public class IntegrationController {
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ProcessEngineConfiguration processEngineConfiguration;

    /**
     * 开启流程实例
     *
     * @param processKey
     * @param money
     * @param description
     * @return
     */
    @GetMapping("/addProcess")
    @ResponseBody
    public String addExpense(@RequestParam String processKey,
                             @RequestParam Double money,
                             @RequestParam(required = false) Integer count,
                             @RequestParam(required = false) String description) {
        //启动流程
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", SecurityUtil.getCurrentUserId());
        map.put("money", money);
        map.put("count", (count == null) ? 1 : count);
        map.put("description", description);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey, map);
        return "提交成功，流程Id为：" + processInstance.getId();
    }

    /**
     * 获取正在处理中的流程列表
     * @return
     */
    @GetMapping(value = "/getProcessList")
    public String getProcessList() {
        List<ProcessInstance> processInstanceList = runtimeService.createProcessInstanceQuery().list();
        return processInstanceList.toString();
    }

    /**
     * 获取流程审批历史
      * @param processInstanceId
     * @return
     */
    @GetMapping(value = "/getHistoryApprove/{processInstanceId}")
    public List<Map<String, Object>> getHistoryApprove(@PathVariable String processInstanceId) {
        List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).orderByHistoricTaskInstanceStartTime().asc().list();
        List<Map<String, Object>> result = new ArrayList<>();
        if (historicTaskInstanceList == null || historicTaskInstanceList.size() == 0) {
            return result;
        }
        for (HistoricTaskInstance historicTaskInstance : historicTaskInstanceList) {
            HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).taskId(historicTaskInstance.getId()).variableName("approved").singleResult();
            Map<String, Object> taskParam = new HashMap<>();
            taskParam.put("taskId", historicTaskInstance.getId());
            taskParam.put("assignee", historicTaskInstance.getAssignee());
            taskParam.put("approved", (historicVariableInstance == null) ? null : historicVariableInstance.getValue());
            taskParam.put("startTime", historicTaskInstance.getStartTime());
            taskParam.put("endTime", historicTaskInstance.getEndTime());
            result.add(taskParam);
        }
        return result;
    }

    /**
     * 生成流程图
     * @param response
     * @param processId
     * @throws Exception
     */
    @RequestMapping(value = "/processDiagram")
    public void genProcessDiagram(HttpServletResponse response, @RequestParam String processId) throws Exception {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
        //流程走完的不显示图
        if (processInstance == null) {
            return;
        }
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        //使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
        String instanceId = task.getProcessInstanceId();
        List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(instanceId).list();

        //得到正在执行的Activity的Id
        List<String> activityIds = new ArrayList<>();
        List<String> flows = new ArrayList<>();
        for (Execution execution : executions) {
            List<String> ids = runtimeService.getActiveActivityIds(execution.getId());
            activityIds.addAll(ids);
        }

        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        ProcessEngineConfiguration engineConfiguration = processEngineConfiguration.buildProcessEngine().getProcessEngineConfiguration();
        ProcessDiagramGenerator diagramGenerator = engineConfiguration.getProcessDiagramGenerator();
        InputStream in = diagramGenerator.generateDiagram(bpmnModel, "png", activityIds, flows, engineConfiguration.getActivityFontName(), engineConfiguration.getLabelFontName(), engineConfiguration.getAnnotationFontName(), engineConfiguration.getClassLoader(), 1.0);
        OutputStream out = null;
        byte[] buff = new byte[1024];
        int length = 0;
        try {
            out = response.getOutputStream();
            while ((length = in.read(buff)) != -1) {
                out.write(buff, 0, length);
            }
        }finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
}
