# to-do-vue

Front-End para app To-Do desenvolvido em Vue 3
## Project Setup

```sh
npm install
```

### Compile and Hot-Reload for Development

```sh
npm run dev
```

### Type-Check, Compile and Minify for Production

```sh
npm run build
```

### Run Unit Tests with [Vitest](https://vitest.dev/)

```sh
npm run test:unit
```

### Lint with [ESLint](https://eslint.org/)

```sh
npm run lint
```

## Arquitetura do projeto

O projeto foi desenvolvido considerando o uso de componentes Vue, vue router e pinia para gerenciamento de state.

Aqui um exemplo de view que faz chamada a uma userStore

```
<script setup lang="ts">

import { getErrorMessage, getErrorMessageInputValues, getMessage } from '@/msg/messages';
import router from '@/router';
import { useUserStore } from '@/stores/user_store';
import { ref } from 'vue'
import { isEmailValid, isPasswordInputValid } from '../validator/validator';
import Loading from '@/components/Loading.vue';
import ErrorMessage from '@/components/Error-message.vue';

const userStore = useUserStore();
const loading = ref(false);
const errorMessage = ref('');
const email = ref('');
const password = ref('');

const login = () => {
    if (!isFormValid()) {
        errorMessage.value = getErrorMessageInputValues()
    } else {
        loading.value = true;
        userStore.login(email.value.trim().toLocaleLowerCase(), password.value.trim())
            .then(() => {
                loading.value = false;
                router.push({name: 'taskview'})
            })
            .catch(err => {
                loading.value = false;
                handleError(err);
            })
    }
}

const handleError = (err: any) => {
    if (err.errorMessage) {
        if (err.errorMessage === "MSE07") {
            router.push({ name: 'useractivationrequest', params: { email: email.value } })
        }
        else { errorMessage.value = getMessage(err.errorMessage); }
    } else {
        errorMessage.value = getErrorMessage();
    }
}

const closeErrorMessage = () => {
    errorMessage.value = '';
}

const isFormValid = (): boolean => {
    return isEmailValid(email.value) && isPasswordInputValid(password.value)
}

</script>
```

Aqui a userStore, que gerencia o estado (state) e faz a comunicação com o back-end via HTTP

```
import { ref, computed, type Ref } from 'vue'
import { defineStore } from 'pinia'
import type { User, UserData } from '@/model/user'
import { URL_ACTIVATE_USER, URL_FORGOT_PASSWORD, URL_LOGIN, URL_NEW_PASSWORD, URL_REGISTER_USER, URL_REQUEST_USER_ACTIVATION, URL_UPDATE_PASSWORD, URL_UPDATE_PROFILE, USER_DATA } from '@/consts/consts';
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
            const data = sessionStorage.getItem(USER_DATA);
            if (data !== null) {
                user.value = JSON.parse(data);
            }
        }
    }

    const updateUserProfile = (firstName: string, lastName: string): Promise<null> => {
        return new Promise((resolve, reject) => {
            sendHttpRequest(URL_UPDATE_PROFILE, 'PUT', JSON.stringify({ "firstName": firstName, "lastName": lastName }))
                .then((res) => {
                    if (res.ok) {
                        if (user.value && user.value !== null) {
                            user.value.name = firstName;
                            user.value.lastName = lastName;
                        }
                        return resolve(null);
                    } else {
                        res.json()
                            .then(body => { return reject(body); })
                            .catch(error => { console.log(error); return reject(error); })
                    }
                },
                    (error => { console.log(error); return reject(error); }))
                .catch(error => { console.log(error); return reject(error); })

        })
    }

    const updateUserPassword = (newPassword: string): Promise<null> => {
        return new Promise((resolve, reject) => {
            sendHttpRequest(URL_UPDATE_PASSWORD, 'PUT', JSON.stringify({ "password": newPassword }))
                .then((res) => {
                    if (res.ok) {
                        return resolve(null);
                    } else {
                        res.json()
                            .then(body => { return reject(body); })
                            .catch(error => { console.log(error); return reject(error); })
                    }
                },
                    (error => { console.log(error); return reject(error); }))
                .catch(error => { console.log(error); return reject(error); })
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
        if (timer !== null) { clearTimeout(timer); }
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
        };
        sessionStorage.setItem(USER_DATA, JSON.stringify(user.value))
        autoLogout(userData.expiresIn);
    }

    const forgotPassword = (email: string): Promise<null> => {
        return new Promise((resolve, reject) => {
            sendHttpRequest(URL_FORGOT_PASSWORD, 'POST', JSON.stringify({
                "email": email
            }))
                .then(res => {
                    if (res.ok) {
                        return resolve(null);
                    } else {
                        res.json()
                            .then(body => { return reject(body); })
                            .catch(error => { console.log(error); return reject(error); })
                    }
                })
                .catch(error => { console.log(error); return reject(error); })
        })
    }

    const registerNerPasswordFromCode = (email: string, password: string, newPasswordCode: string): Promise<null> => {
        return new Promise((resolve, reject) => {
            sendHttpRequest(URL_NEW_PASSWORD, 'POST', JSON.stringify({
                "email": email, "password": password, "newPasswordCode": newPasswordCode
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

    const registerUser = (email: string, password: string, firstName: string, lastName: string): Promise<null> => {
        return new Promise((resolve, reject) => {
            sendHttpRequest(URL_REGISTER_USER, 'POST', JSON.stringify({
                "email": email, "password": password, "firstName": firstName, "lastName": lastName
            }))
                .then(res => {
                    if (res.ok) {
                        return resolve(null);
                    } else {
                        res.json()
                            .then(body => { return reject(body); })
                            .catch(error => { console.log(error); return reject(error); })
                    }
                })
                .catch(error => { console.log(error); return reject(error); })
        })

    }

    const requestUserActivation = (email: string): Promise<null> => {
        return new Promise((resolve, reject) => {
            sendHttpRequest(URL_REQUEST_USER_ACTIVATION, 'POST', JSON.stringify({
                "email": email
            }))
                .then(res => {
                    if (res.ok) {
                        return resolve(null);
                    } else {
                        res.json()
                            .then(body => { return reject(body); })
                            .catch(error => { console.log(error); return reject(error); })
                    }
                })
                .catch(error => { console.log(error); return reject(error); })
        })
    }

    const activateUser = (email: string, activationCode: string): Promise<null> => {
        return new Promise((resolve, reject) => {
            sendHttpRequest(URL_ACTIVATE_USER, 'POST', JSON.stringify({
                "email": email, "activationCode": activationCode
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

```