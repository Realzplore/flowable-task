package com.flowable.web;

import org.apache.ibatis.javassist.tools.rmi.ObjectNotFoundException;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: liping.zheng
 * @Date: 2018/7/25
 */
@RestController
@RequestMapping("/api/task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    /**
     * 获取任务列表
     * @param processInstanceId
     * @return
     * @throws ObjectNotFoundException
     */
    @GetMapping(value = "/getByProcessInstanceId")
    public List<Map<String, Object>> getTaskByProcessInstanceId(@RequestParam String processInstanceId) throws ObjectNotFoundException {
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        List<Map<String, Object>> result = new ArrayList<>();
        if (taskList == null || taskList.size() == 0) {
            return result;
        }
        for (Task task : taskList) {
            Map<String, Object> taskParam = new HashMap<>();
            taskParam.put("processInstanceId", processInstanceId);
            taskParam.put("taskId", task.getId());
            taskParam.put("variables", taskService.getVariables(task.getId()));
            taskParam.put("assignee", task.getAssignee());
            result.add(taskParam);
        }
        return result;
    }

    /**
     * 处理任务
     *
     * @param processInstanceId
     * @param isCompleted
     * @return
     * @throws ObjectNotFoundException
     */
    @PostMapping(value = "/handle", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> handleTask(@RequestParam String processInstanceId,
                                          @RequestParam String taskId,
                                          @RequestParam Long userId,
                                          @RequestParam Boolean isCompleted) throws ObjectNotFoundException {

        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).taskId(taskId).singleResult();
        if (task == null) {
            throw new ObjectNotFoundException("task");
        }
        Map<String, Object> params = taskService.getVariables(task.getId());
        if (!String.valueOf(userId).equals(task.getAssignee())) {
            throw new RuntimeException("不符合对应操作人权限.");
        }
        params.put("count", (Integer) params.get("count") + 1);
        params.put("approved", isCompleted);
        taskService.setVariableLocal(task.getId(), "approved", isCompleted);
        taskService.complete(task.getId(), params);
        Map<String, Object> result = new HashMap<>();
        result.put("processInstanceId", processInstanceId);
        result.put("taskId", task.getId());
        result.put("variables", params);
        return result;
    }

    @GetMapping(value = "/list/{userId}")
    public List<Map<String, Object>> getTaskListByEmployee(@PathVariable String userId) {
        List<Task> taskList = taskService.createTaskQuery().taskAssignee(userId).list();
        List<Map<String, Object>> result = new ArrayList<>();
        if (taskList == null || taskList.size() == 0) {
            return result;
        }
        for (Task task : taskList) {
            Map<String, Object> taskParam = new HashMap<>();
            taskParam.put("taskId", task.getId());
            taskParam.put("processInstanceId", task.getProcessInstanceId());
            taskParam.put("variables", taskService.getVariables(task.getId()));
            taskParam.put("assignee", task.getAssignee());
            taskParam.put("processVariables", task.getProcessVariables());
            result.add(taskParam);
        }
        return result;
    }

}
