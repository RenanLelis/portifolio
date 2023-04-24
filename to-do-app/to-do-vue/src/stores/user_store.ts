import { ref, computed, type Ref } from 'vue'
import { defineStore } from 'pinia'
import type { User } from '@/model/user'
import { URL_UPDATE_PROFILE, USER_DATA } from '@/consts/consts';
import { sendHttpRequest } from './interceptor';

export const useUserStore = defineStore('user', () => {


    const user: Ref<User | null> = ref(null);

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
                const loadedUser: User = JSON.parse(data);
                user.value = loadedUser;
            }
        }
    }

    const updateUserProfile = (firstName: string, lastName: string) => {
        return new Promise((resolve, reject) => {
            sendHttpRequest(URL_UPDATE_PROFILE, 'POST', JSON.stringify({ "firstName": firstName, "lastName": lastName }))
                .then((res) => {
                    console.log(res.body)
                    if (res.ok) {
                        //TODO update user data on the store
                        return resolve(null);
                    }
                    //TODO check for error on the body
                },
                    (error => {
                        console.log(error);
                        return reject(error);
                    }))

        })
    }

    const updateUserPassword = (newPassword: string) => {
        //TODO
    }

    const login = (email: string, password: string) => {
        //TODO
    }
    
    const forgotPassword = (email: string) => {
        //TODO
    }
    
    const registerNerPasswordFromCode = (email: string, password: string, newPasswordCode: string) => {
        //TODO
    }
    
    const registerUser = (email: string, password: string, firstName: string, lastName: string) => {
        //TODO
    }

    const requestUserActivation = (email: string) => {
        //TODO
    }
    
    const activateUser = (email: string, activationCode: string) => {
        //TODO
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
        activateUser
    }
})
