<template>
    <div class="container-auth">
        <div class="card-auth">
            <div class="logo">
                <a><img src="../../assets/logo.png">
                    <h3>My Task List</h3>
                </a>
            </div>
            <h2>Password Reset</h2>
            <form @submit.prevent="resetPassword">
                <div class="label-field">
                    <label class="label">E-mail: {{ email }} </label>
                </div>
                <div class="label-field">
                    <label class="label">New Password Code</label>
                    <input v-model="newPasswordCode" type="text" required class="text-field">
                </div>
                <div class="label-field">
                    <label class="label">New Password</label>
                    <input v-model="newPassword" type="password" required class="text-field">
                </div>
                <div>
                    <div class="div-submit-button">
                        <button type="submit" class="send-button">Send</button>
                    </div>
                    <div class="action-links">
                        <router-link class="side-link" to="/userregistration">New user?, Register here</router-link>
                        <router-link class="side-link" to="/login">Make Login</router-link>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <div v-if="loading">
        <Loading />
    </div>

    <ErrorMessage :errorMessage="errorMessage" @on-close="closeErrorMessage" />
</template>

<script setup lang="ts">
import { getErrorMessage, getErrorMessageInputValues, getMessage } from '@/msg/messages';
import router from '@/router';
import { useUserStore } from '@/stores/user_store';
import { ref } from 'vue'
import { isEmailValid, isPasswordInputValid } from '../validator/validator';
import { LENGTH_NEW_PASSWORD_CODE } from '../../consts/consts'
import Loading from '@/components/Loading.vue';
import ErrorMessage from '@/components/Error-message.vue';
import { useRoute } from 'vue-router';

const route = useRoute()
const userStore = useUserStore();
const loading = ref(false);
const errorMessage = ref('');
const email = route.params.email.toString()
const newPassword = ref('');
const newPasswordCode = ref('');

const resetPassword = () => {
    if (!isFormValid()) {
        errorMessage.value = getErrorMessageInputValues()
    } else {
        loading.value = true;
        userStore.registerNerPasswordFromCode(email, newPassword.value.trim(), newPasswordCode.value.trim())
            .then(() => {
                loading.value = false;
                router.push({name: 'taskview'});
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
            router.push({ name: 'useractivationrequest', params: { email: email } })
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
    return isEmailValid(email)
        && newPasswordCode.value.trim().length === LENGTH_NEW_PASSWORD_CODE
        && isPasswordInputValid(newPassword.value)
}

</script>

<style scoped></style>