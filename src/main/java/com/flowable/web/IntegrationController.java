package com.flowable.web;

import com.flowable.modules.expense.domain.Expense;
import com.flowable.modules.user.domain.User;
import com.flowable.modules.user.dto.UserDTO;
import com.flowable.service.PreviewService;
import com.flowable.service.ExpenseProcessService;
import org.apache.ibatis.javassist.tools.rmi.ObjectNotFoundException;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private PreviewService previewService;

    @Autowired
    private ExpenseProcessService expenseProcessService;
    /**
     * 开启流程实例
     *
     * @param processKey
     * @return
     */
    @PostMapping(value = "/addProcess",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Expense> addExpense(@RequestParam String processKey,
                                              @RequestBody Expense expense) {
        return ResponseEntity.ok(expenseProcessService.startProcessInstanceByKey(processKey, expense));
    }

    /**
     * 获取正在处理中的报销单列表
     *
     * @return
     */
    @GetMapping(value = "/getProcessList")
    public ResponseEntity<List<Expense>> getProcessList() {
        List<String> processInstanceIdList = runtimeService.createProcessInstanceQuery().list().stream().map(f -> f.getProcessInstanceId()).collect(Collectors.toList());
        return ResponseEntity.ok(expenseProcessService.getRuntimeExpenseList(processInstanceIdList));
    }

    /**
     * 获取历史报销单列表
     *
     * @return
     */
    @GetMapping(value = "/getHisProcessList")
    public ResponseEntity<List<Expense>> getHisProcessList() {
        List<String> historicProcessInstanceIdList = historyService.createHistoricProcessInstanceQuery().list().stream().map(f -> f.getSuperProcessInstanceId()).collect(Collectors.toList());
        return ResponseEntity.ok(expenseProcessService.getRuntimeExpenseList(historicProcessInstanceIdList));
    }

    /**
     * 删除历史流程
     *
     * @param hisProcessInstanceId
     */
    @DeleteMapping(value = "delete/history/{hisProcessInstanceId}")
    public void deleteHisProcessInstance(@PathVariable String hisProcessInstanceId) {
        historyService.deleteHistoricProcessInstance(hisProcessInstanceId);
    }

    @DeleteMapping(value = "delete/{processInstanceId}")
    public void deleteProcessInstance(@PathVariable String processInstanceId, @RequestParam String deleteReason) {
        runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
    }


    /**
     * 获取流程审批历史
     *
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
            HistoricVariableInstance approved = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).taskId(historicTaskInstance.getId()).variableName("approved").singleResult();
            HistoricVariableInstance assignee = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).taskId(historicTaskInstance.getId()).variableName("assignee").singleResult();
            Map<String, Object> taskParam = new HashMap<>();
            taskParam.put("taskId", historicTaskInstance.getId());
            taskParam.put("assignee", (assignee == null) ? null : assignee.getValue());
            taskParam.put("approved", (approved == null) ? null : approved.getValue());
            taskParam.put("startTime", historicTaskInstance.getStartTime());
            taskParam.put("endTime", historicTaskInstance.getEndTime());
            result.add(taskParam);
        }
        return result;
    }

    /**
     * 模拟预览审批流程
     * @param processKey
     * @param expense
     * @return
     * @throws ObjectNotFoundException
     */
    @PostMapping(value = "/preview", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> previewProcess(@RequestParam String processKey,
                                                     @RequestBody Expense expense) throws ObjectNotFoundException {
        return ResponseEntity.ok(previewService.getPreviewProcessByDmnKey(processKey, expense));
    }


    @GetMapping(value = "/preview/running", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> previewProcessing(@RequestParam String taskId) throws ObjectNotFoundException {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            return null;
        }
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        List<Expense> expenseList = expenseProcessService.getRuntimeExpenseList(Collections.singletonList(task.getProcessInstanceId()));
        if (expenseList == null) {
            return null;
        }
        List<User> userList = previewService.getPreviewProcessByDmnKey(processInstance.getProcessDefinitionKey(), expenseList.get(0));
        return ResponseEntity.ok(userList.stream().map(f -> {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(f, userDTO);
            if (StringUtils.isEmpty(task.getAssignee())) {
                userDTO.setIsProcessed(Boolean.FALSE);
            }
            if (String.valueOf(userDTO.getId()).equals(task.getAssignee())) {
                userDTO.setIsProcessed(Boolean.TRUE);
            }
            return userDTO;
        }).collect(Collectors.toList()));
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
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
}
