<template>
    <div class="container-auth">
    <div class="card-auth">
        <div class="logo">
            <a><img src="../../assets/logo.png">
                <h3>My Task List</h3>
            </a>
        </div>
        <h2>Login</h2>
        <form @submit.prevent="login">
            <div class="label-field">
                <label class="label">E-mail</label>
                <input type="email" v-model="email" required class="text-field">
                <!-- <p *ngIf="formLogin.get('email')!.invalid && formLogin.get('email')!.touched"
                    class="error-field-message">
                    Error on e-mail input.
                </p> -->
            </div>
            <div class="label-field">
                <label class="label">Password</label>
                <input type="password" v-model="password" required class="text-field">
                <!-- <p *ngIf="formLogin.get('password')!.invalid && formLogin.get('password')!.touched"
                    class="error-field-message">
                    Error on password input.
                </p> -->
            </div>
            <div>
                <div class="div-submit-button">
                    <button type="submit" class="send-button">Send</button>
                </div>
                <div class="action-links">
                    <router-link to="/recoverpassword">Forgot password?</router-link>
                    <router-link to="/userregistration">New user?, Register here</router-link>
                </div>
            </div>
        </form>
    </div>
</div>

<div v-if="loading">
    <Loading/>
</div>

<ErrorMessage :errorMessage="errorMessage" @on-close="closeErrorMessage"/>
</template>

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
                router.push({name: 'home'})
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

<style scoped></style>