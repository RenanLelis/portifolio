<template>
    <div class="container-auth">

        <div class="card-auth">
            <div class="logo">
                <a><img src="../../assets/logo.png">
                    <h3>My Task List</h3>
                </a>
            </div>
            <h2>User Activation</h2>
            <form @submit.prevent="requestUserActivation">
                <div class="label-field">
                    <label class="label">E-mail: {{ email }} </label>
                </div>

                <div class="label-field">
                    <p>
                        To access the system you need to activate your user, do you want to receive an activation code by
                        e-mail?
                    </p>
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
import Loading from '@/components/Loading.vue';
import ErrorMessage from '@/components/Error-message.vue';
import { isEmailValid } from '../validator/validator';
import { useRoute } from 'vue-router';

const route = useRoute()
const userStore = useUserStore();
const loading = ref(false);
const errorMessage = ref('');
const email = route.params.email.toString();

const requestUserActivation = () => {
    if (!isFormValid()) {
        errorMessage.value = getErrorMessageInputValues()
    } else {
        loading.value = true;
        userStore.requestUserActivation(email.trim().toLowerCase())
            .then(() => {
                loading.value = false;
                router.push({ name: 'useractivation', params: { email: email } })
            })
            .catch(err => {
                loading.value = false;
                handleError(err);
            })
    }
}

const handleError = (err: any) => {
    if (err.errorMessage) {
        errorMessage.value = getMessage(err.errorMessage);
    } else {
        errorMessage.value = getErrorMessage();
    }
}

const closeErrorMessage = () => {
    errorMessage.value = '';
}

const isFormValid = (): boolean => {
    return isEmailValid(email)
}
</script>

<style scoped>
p {
    margin: auto;
    padding: 30px;
}
</style>