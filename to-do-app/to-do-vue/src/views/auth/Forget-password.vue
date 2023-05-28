<template>
    <div class="container-auth">
        <div class="card-auth">
            <div class="logo">
                <a><img src="../../assets/logo.png">
                    <h3>My Task List</h3>
                </a>
            </div>
            <h2>Forget your password</h2>
            <p>Tell us your e-mail to recover your password</p>
            <form @submit.prevent="sendRequest">
                <div class="label-field">
                    <label class="label">E-mail</label>
                    <input v-model="email" type="email" required class="text-field">
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
import { isEmailValid } from '../validator/validator';
import Loading from '@/components/Loading.vue';
import ErrorMessage from '@/components/Error-message.vue';

const userStore = useUserStore();
const loading = ref(false);
const errorMessage = ref('');
const email = ref('');

const sendRequest = () => {
    if (!isFormValid()) {
        errorMessage.value = getErrorMessageInputValues()
    } else {
        loading.value = true;
        userStore.forgotPassword(email.value.trim().toLocaleLowerCase())
            .then(() => {
                loading.value = false;
                router.push({ name: 'passwordreset', params: { email: email.value } })
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
}

</script>

<style scoped>
.card-auth p {
    margin: 0 0 15px 0;
}
</style>