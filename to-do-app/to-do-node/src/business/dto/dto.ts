import { Task } from "../../model/task";
import { TaskList } from "../../model/taskList";
import { User } from "../../model/user";
import token from "../token/token"

export class UserDTO {
    constructor(
        public jwt: string,
        public expiresIn: number,
        public id: number,
        public name: string,
        public lastName: string | null,
        public email: string,
        public userStatus: number,
    ) { }
}

export class TaskDTO {
    constructor(
        public id: number,
        public taskName: string,
        public taskDescription: string | null,
        public deadline: string | null,
        public taskStatus: number,
    ) { }
}

export class TaskListDTO {
    constructor(
        public id: number,
        public listName: string,
        public listDescription: string | null,
        public tasks: TaskDTO[],
    ) { }
}

export const convertUserToDTO = (user: User) => {
    return new UserDTO(
        token.generateToken(user),
        token.EXP_TIME,
        user.id!,
        user.firstName,
        user.lastName,
        user.email,
        user.userStatus
    );
}

export const refreshUserDTO = (userDTO: UserDTO) => {
    return new UserDTO(
        token.refreshToken(userDTO.jwt),
        token.EXP_TIME,
        userDTO.id,
        userDTO.name,
        userDTO.lastName,
        userDTO.email,
        userDTO.userStatus
    );
}

export const convertTaskListToDTO = (list: TaskList): TaskListDTO => {
    return new TaskListDTO(
        list.id!,
        list.listName!,
        list.listDescription,
        []
    );
}

export const convertTaskListsToDTOs = (lists: TaskList[]): TaskListDTO[] => {
    let result: TaskListDTO[] = []
    lists.forEach(list => {
        result.push(convertTaskListToDTO(list))
    });
    return result;
}

export const convertTaskToDTO = (task: Task): TaskDTO => {
    return new TaskDTO(
        task.id!,
        task.taskName,
        task.taskDescription,
        task.deadline,
        task.taskStatus
    )
}

export const convertTasksToDTOs = (tasks: Task[]): TaskDTO[] => {
    let result: TaskDTO[] = []
    tasks.forEach(task => {
        result.push(convertTaskToDTO(task))
    });
    return result;
}

export const convertTasksAndListsToDTO = (lists: TaskList[], tasks: Task[]): TaskListDTO[] => {
    let tasklistsDTO: TaskListDTO[] = convertTaskListsToDTOs(lists);
    tasks.forEach(task => {
        const taskDTO = convertTaskToDTO(task);
        for (let i = 0; i < tasklistsDTO.length; i++) {
            if (tasklistsDTO[i].id === task.listID) {
                tasklistsDTO[i].tasks.push(taskDTO);
                break;
            }
        }
    });
    return tasklistsDTO;
}
