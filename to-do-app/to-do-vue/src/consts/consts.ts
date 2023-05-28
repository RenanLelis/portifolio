export const BASE_URL = "http://localhost:8080";
export const USER_DATA = "USER_DATA";

export const LENGTH_NEW_PASSWORD_CODE = 6;
export const LENGTH_ACTIVATION_CODE = 6;

export const URL_UPDATE_PROFILE: string = BASE_URL + "/api/user/profile";
export const URL_UPDATE_PASSWORD: string = BASE_URL + "/api/user/password";

export const URL_LOGIN: string = BASE_URL + "/api/auth/login";
export const URL_FORGOT_PASSWORD: string = BASE_URL + "/api/auth/recoverpassword";
export const URL_NEW_PASSWORD: string = BASE_URL + "/api/auth/passwordreset";
export const URL_REGISTER_USER: string = BASE_URL + "/api/auth/userregistration";
export const URL_ACTIVATE_USER: string = BASE_URL + "/api/auth/useractivation";
export const URL_REQUEST_USER_ACTIVATION: string = BASE_URL + "/api/auth/useractivationrequest";

export const URL_TASK_LIST: string = BASE_URL + '/api/taskList';
export const URL_TASK_LISTS_MOVE: string = BASE_URL + '/api/taskList/tasks/move';
export const URL_TASK_LISTS_MOVE_FROM_LIST: string = BASE_URL + '/api/taskList/tasks/moveFromList';
export const URL_TASK_LISTS_COMPLETE_TASKS: string = BASE_URL + '/api/taskList/tasks/complete';
export const URL_TASK_LISTS_UNCOMPLETE_TASKS: string = BASE_URL + '/api/taskList/tasks/uncomplete';
export const URL_TASKS_BY_LISTS: string = BASE_URL + '/api/taskList/tasks';
export const URL_TASK: string = BASE_URL + '/api/task';
export const URL_TASK_MOVE: string = BASE_URL + '/api/task/list';
export const URL_TASK_COMPLETE: string = BASE_URL + '/api/task/complete';
export const URL_TASK_UNCOMPLETE: string = BASE_URL + '/api/task/uncomplete';
export const URL_TASKS_DELETE: string = BASE_URL + '/api/tasks';
export const URL_TASKS_COMPLETE: string = BASE_URL + '/api/tasks/complete';
export const URL_TASKS_UNCOMPLETE: string = BASE_URL + '/api/tasks/uncomplete';