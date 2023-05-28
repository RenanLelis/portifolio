<template>
    <div class="container-auth">
        <div class="card-auth">
            <div class="logo">
                <a><img src="../../assets/logo.png">
                    <h3>My Task List</h3>
                </a>
            </div>
            <h2>User Registration</h2>
            <form @submit.prevent="registerUser">
                <div class="label-field">
                    <label class="label">E-mail</label>
                    <input v-model="email" type="email" required class="text-field">
                </div>
                <div class="label-field">
                    <label class="label">Password</label>
                    <input v-model="password" type="password" required class="text-field">
                </div>
                <div class="label-field">
                    <label class="label">Name</label>
                    <input v-model="name" type="text" required class="text-field">
                </div>
                <div class="label-field">
                    <label class="label">Last Name</label>
                    <input v-model="lastName" type="text" required class="text-field">
                </div>
                <div>
                    <div class="div-submit-button">
                        <button type="submit" class="send-button">Send</button>
                    </div>
                    <div class="action-links">
                        <router-link class="side-link" to="/recoverpassword">Forgot password?</router-link>
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
import Loading from '@/components/Loading.vue';
import ErrorMessage from '@/components/Error-message.vue';

const userStore = useUserStore();
const loading = ref(false);
const errorMessage = ref('');
const email = ref('');
const password = ref('');
const name = ref('');
const lastName = ref('');

const registerUser = () => {
    if (!isFormValid()) {
        errorMessage.value = getErrorMessageInputValues()
    } else {
        loading.value = true;
        userStore.registerUser(email.value.trim().toLocaleLowerCase(), password.value.trim(), name.value.trim(), lastName.value.trim())
            .then(() => {
                loading.value = false;
                router.push({ name: 'useractivation', params: { email: email.value } })
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
    return isEmailValid(email.value)
        && isPasswordInputValid(password.value)
        && name.value != null && name.value.trim().length > 0
}

</script>

<style scoped>
.container-auth .card-auth {
    margin: auto;
}
</style>