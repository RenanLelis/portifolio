package com.renan.todo.dto;

import com.renan.todo.entities.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TaskDTOMapper implements Function<Task, TaskDTO> {

    @Autowired
    private TaskListDTOMapper taskListDTOMapper;

    /**
     * Convert a list of tasks (from the same taskList) to a TaskListDTO
     *
     * @param tasks - list of tasks
     * @return the new TaskListDTO
     */
    public TaskListDTO convertToListDTO(List<Task> tasks) {
        if (tasks != null && tasks.size() > 0) {
            TaskListDTO result = taskListDTOMapper.apply(tasks.get(0).getList());
            result.setTasks(tasks.stream().map(this).collect(Collectors.toList()));
            return result;
        }
        return null;
    }

    /**
     * Convert a task to an taskDTO
     *
     * @param task - object to convert to DTO
     * @return - the dto
     */
    @Override
    public TaskDTO apply(Task task) {
        return new TaskDTO(
                task.getId(),
                task.getTaskName(),
                task.getTaskDescription(),
                task.getDeadline(),
                task.getTaskStatus()
        );
    }

}
