// export const BASE_URL = "http://localhost:5000";
export const BASE_URL = "http://localhost:8080";
export const USER_DATA = "USER_DATA";

export const URL_UPDATE_PROFILE: string = BASE_URL + "/api/user/profile";
export const URL_UPDATE_PASSWORD: string = BASE_URL + "/api/user/password";

export const URL_LOGIN: string = BASE_URL + "/api/auth/login";
export const URL_FORGOT_PASSWORD: string = BASE_URL + "/api/auth/recoverpassword";
export const URL_NEW_PASSWORD: string = BASE_URL + "/api/auth/passwordreset";
export const URL_REGISTER_USER: string = BASE_URL + "/api/auth/userregistration";
export const URL_ACTIVATE_USER: string = BASE_URL + "/api/auth/useractivation";
export const URL_REQUEST_USER_ACTIVATION: string = BASE_URL + "/api/auth/useractivationrequest";