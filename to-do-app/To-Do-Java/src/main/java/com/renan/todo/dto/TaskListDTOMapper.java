package com.renan.todo.dto;

import com.renan.todo.model.Task;
import com.renan.todo.model.TaskList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskListDTOMapper implements Function<TaskList, TaskListDTO> {

    private final TaskDTOMapper taskDTOMapper;

    /**
     * Convert a list of tasks (from the same taskList) to a TaskListDTO
     *
     * @param tasks - list of tasks
     *
     * @return the new TaskListDTO
     */
    public TaskListDTO convertToListDTO(List<Task> tasks) {
        if (tasks != null && tasks.size() > 0) {
            TaskListDTO result = this.apply(tasks.get(0).getList());
            result.setTasks(tasks.stream().map(taskDTOMapper).collect(Collectors.toList()));
            return result;
        }
        return null;
    }

    /**
     * Convert multiple lists of tasks and tasks to DTO
     *
     * @param tasks - the tasks with the list id
     *
     * @return - the new DTO
     */
    public List<TaskListDTO> convertMultipleListsAndTasks(List<Task> tasks) {
        List<TaskListDTO> result = new ArrayList<>();
        if (tasks != null && tasks.size() > 0) {
            List<TaskList> listsTasks = new ArrayList<>();
            for (Task task : tasks) {
                if (!listsTasks.contains(task.getList())) {
                    listsTasks.add(task.getList());
                }
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
     *
     * @return - the new DTO
     */
    public List<TaskListDTO> convertMultipleListsAndTasks(List<TaskList> taskLists, List<Task> tasks) {
        List<TaskListDTO> result = taskLists.stream().map(this).collect(Collectors.toList());
        if (tasks != null && tasks.size() > 0) {
            for (TaskListDTO taskListDTO : result) {
                taskListDTO.setTasks(tasks.stream().filter(task -> task.getList().getId().equals(taskListDTO.getId()))
                        .map(taskDTOMapper).collect(Collectors.toList()));
            }
        }
        return result;
    }

    /**
     * Convert a list of tasks and taksList to DTO
     *
     * @param taskList - the taskList object
     * @param tasks    - the list of tasks
     *
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
     *
     * @return - the dto
     */
    @Override
    public TaskListDTO apply(TaskList taskList) {
        return new TaskListDTO(taskList.getId(), taskList.getListName(), taskList.getListDescription(),
                new ArrayList<TaskDTO>());
    }
}