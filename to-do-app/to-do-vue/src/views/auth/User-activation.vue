<template>
    <div class="container-auth">
        <div class="card-auth">
            <div class="logo">
                <a><img src="../../assets/logo.png">
                    <h3>My Task List</h3>
                </a>
            </div>
            <h2>User Activation</h2>
            <form @submit.prevent="activateUser">
                <div class="label-field">
                    <label class="label">E-mail: {{ email }} </label>
                </div>
                <div class="label-field">
                    <label class="label">Activation Code</label>
                    <input v-model="activationCode" type="text" required class="text-field">
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
import { useRoute } from 'vue-router';
import { isEmailValid } from '../validator/validator';
import { LENGTH_ACTIVATION_CODE } from '@/consts/consts';

const route = useRoute()
const userStore = useUserStore();
const loading = ref(false);
const errorMessage = ref('');
const email = route.params.email.toString();
const activationCode = ref('');

const activateUser = () => {
    if (!isFormValid()) {
        errorMessage.value = getErrorMessageInputValues()
    } else {
        loading.value = true;
        userStore.activateUser(email, activationCode.value.trim())
            .then(() => {
                loading.value = false;
                router.push({ name: 'taskview' })
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
        && activationCode.value !== null && activationCode.value.trim().length === LENGTH_ACTIVATION_CODE
}

</script>

<style scoped></style>