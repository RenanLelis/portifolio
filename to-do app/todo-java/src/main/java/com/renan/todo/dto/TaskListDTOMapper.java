package com.renan.todo.dto;

import com.renan.todo.entities.Task;
import com.renan.todo.entities.TaskList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TaskListDTOMapper implements Function<TaskList, TaskListDTO> {

    @Autowired
    private TaskDTOMapper taskDTOMapper;

    /**
     * Convert multiple lists of tasks and tasks to DTO
     *
     * @param tasks - the tasks with the list id
     * @return - the new DTO
     */
    public List<TaskListDTO> convertMultipleListsAndTasks(List<Task> tasks) {
        List<TaskListDTO> result = new ArrayList<TaskListDTO>();
        if (tasks != null && tasks.size() > 0) {
            List<TaskDTO> tasksWithoutList = new ArrayList<TaskDTO>();
            List<TaskList> listsTasks = new ArrayList<TaskList>();
            for (Task task : tasks) {
                if (task.getList() == null || task.getList().getId() == null) {
                    tasksWithoutList.add(taskDTOMapper.apply(task));
                } else if (!listsTasks.contains(task.getList())) {
                    listsTasks.add(task.getList());
                }
            }
            if (tasksWithoutList.size() > 0) {
                result.add(new TaskListDTO(null, null, null, tasksWithoutList));
            }
            result = this.convertMultipleListsAndTasks(listsTasks, tasks);
        }
        return result;
    }

    /**
     * Convert multiple lists of tasks and tasks to DTO
     *
     * @param taskLists - the multiple taskList object
     * @param tasks     - the list of tasks
     * @return - the new DTO
     */
    public List<TaskListDTO> convertMultipleListsAndTasks(List<TaskList> taskLists, List<Task> tasks) {
        List<TaskListDTO> result = taskLists.stream().map(this::apply).collect(Collectors.toList());
        for (TaskListDTO taskListDTO : result) {
            taskListDTO.setTasks(
                    tasks.stream()
                            .filter(task -> task.getList().getId().equals(taskListDTO.getId()))
                            .map(taskDTOMapper).collect(Collectors.toList())
            );
        }
        return result;
    }

    /**
     * Convert a list of tasks and taksList to DTO
     *
     * @param taskList - the taskList object
     * @param tasks    - the list of tasks
     * @return - the new DTO
     */
    public TaskListDTO convert(TaskList taskList, List<Task> tasks) {
        TaskListDTO taskListDTO = this.apply(taskList);
        taskListDTO.setTasks(tasks.stream().map(taskDTOMapper).collect(Collectors.toList()));
        return taskListDTO;
    }

    /**
     * Convert a taskList to an taskListDTO
     *
     * @param taskList - object to convert to DTO
     * @return - the dto
     */
    @Override
    public TaskListDTO apply(TaskList taskList) {
        return new TaskListDTO(
                taskList.getId(),
                taskList.getListName(),
                taskList.getListDescription(),
                new ArrayList<TaskDTO>()
        );
    }
}