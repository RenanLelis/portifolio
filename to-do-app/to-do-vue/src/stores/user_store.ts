import { ref, computed, type Ref } from 'vue'
import { defineStore } from 'pinia'
import type { User, UserData } from '@/model/user'
import { URL_LOGIN, URL_UPDATE_PROFILE, USER_DATA } from '@/consts/consts';
import { sendHttpRequest } from './interceptor';
import router from '@/router';

export const useUserStore = defineStore('user', () => {


    const user: Ref<User | null> = ref(null);
    let timer: any = null;

    const isUserLoggedIn = computed(() => {
        return (
            user.value !== null &&
            user.value.jwt !== null &&
            user.value.jwt.length > 0 &&
            user.value.tokenExpirationDate &&
            user.value.tokenExpirationDate > new Date().getTime()) as boolean;
    })

    const loadUserInMemory = () => {
        if (user.value === null) {
            const data = sessionStorage.getItem(USER_DATA)
            if (data !== null) {
                user.value = JSON.parse(data);
            }
        }
    }

    const updateUserProfile = (firstName: string, lastName: string): Promise<null> => {
        return new Promise((resolve, reject) => {
            sendHttpRequest(URL_UPDATE_PROFILE, 'POST', JSON.stringify({ "firstName": firstName, "lastName": lastName }))
                .then((res) => {
                    console.log(res.ok)
                    console.log(res.body)
                    if (res.ok) {
                        //TODO update user data on the store
                        return resolve(null);
                    }
                    //TODO check for error on the body
                },
                    (error => { console.log(error); return reject(error); }))
                .catch(error => { console.log(error); return reject(error); })

        })
    }

    const updateUserPassword = (newPassword: string): Promise<null> => {
        return new Promise((resolve, reject) => {
            //TODO
        })
    }

    const login = (email: string, password: string): Promise<null> => {
        return new Promise((resolve, reject) => {
            sendHttpRequest(URL_LOGIN, 'POST', JSON.stringify({
                "email": email, "password": password
            }))
                .then(res => {
                    res.json()
                        .then(body => {
                            if (body.errorMessage) return reject(body);
                            handleLogin(body as UserData)
                            return resolve(null)
                        })
                        .catch(error => { console.log(error); return reject(error); })
                })
                .catch(error => { console.log(error); return reject(error); })
        })
    }

    const logout = () => {
        sessionStorage.removeItem(USER_DATA);
        user.value = null;
    }

    const autoLogout = (expiresIn: number) => {
        if (timer !== null) { clearTimeout(timer); }
        timer = setTimeout(() => { logout(); router.push('/login'); }, expiresIn);
      }

    const handleLogin = (userData: UserData) => {
        user.value = {
            email: userData.email,
            id: userData.id,
            jwt: userData.jwt,
            name: userData.name,
            status: userData.userStatus,
            lastName: userData.lastName,
            tokenExpirationDate: new Date().getTime() + userData.expiresIn
        }
        autoLogout(userData.expiresIn);
    }

    const forgotPassword = (email: string): Promise<null> => {
        return new Promise((resolve, reject) => {
            //TODO
        })
    }

    const registerNerPasswordFromCode = (email: string, password: string, newPasswordCode: string): Promise<null> => {
        return new Promise((resolve, reject) => {
            //TODO
        })
    }

    const registerUser = (email: string, password: string, firstName: string, lastName: string): Promise<null> => {
        return new Promise((resolve, reject) => {
            //TODO
        })
    }

    const requestUserActivation = (email: string): Promise<null> => {
        return new Promise((resolve, reject) => {
            //TODO
        })
    }

    const activateUser = (email: string, activationCode: string): Promise<null> => {
        return new Promise((resolve, reject) => {
            //TODO
        })
    }

    loadUserInMemory();

    return {
        user,
        isUserLoggedIn,
        updateUserProfile,
        updateUserPassword,
        login,
        forgotPassword,
        registerNerPasswordFromCode,
        registerUser,
        requestUserActivation,
        activateUser,
        handleLogin,
        logout
    }
})
