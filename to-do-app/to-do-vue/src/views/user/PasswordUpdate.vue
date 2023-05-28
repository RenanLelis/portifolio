<template>
    <div class="container-personal-info">
        <h2>Change My Password</h2>
        <div class="profile-info">
            <div class="label-field">
                <label class="label"> {{ userStore.user!.name }} {{ userStore.user!.lastName !== null ?
                    userStore.user!.lastName : '' }} </label>
            </div>
        </div>
        <hr />
        <div class="profile-form">
            <form @submit.prevent="updateMyPassword">
                <div class="label-field">
                    <label class="label">New Password</label>
                    <input v-model="password" type="password" required class="text-field">
                </div>
                <div>
                    <div class="div-submit-button">
                        <button type="submit" class="send-button">Send</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <div v-if="loading">
        <Loading />
    </div>

    <ErrorMessage :errorMessage="errorMessage" @on-close="closeErrorMessage" />
    <SuccessMessage :message="successMessage" @on-close="closeMessage" />
</template>

<script setup lang="ts">
import { getErrorMessage, getErrorMessageInputValues, getMessage, getMessageOperationSucceded } from '@/msg/messages';
import router from '@/router';
import { useUserStore } from '@/stores/user_store';
import { ref } from 'vue'
import { isPasswordInputValid } from '../validator/validator';
import Loading from '@/components/Loading.vue';
import ErrorMessage from '@/components/Error-message.vue';
import SuccessMessage from '@/components/Success-message.vue';

const userStore = useUserStore();
const loading = ref(false);
const errorMessage = ref('');
const successMessage = ref('');
const password = ref('');

const updateMyPassword = () => {
    if (!isFormValid()) {
        errorMessage.value = getErrorMessageInputValues();
    } else {
        loading.value = true;
        userStore.updateUserPassword(password.value)
            .then(() => {
                loading.value = false;
                successMessage.value = getMessageOperationSucceded();
            })
            .catch(err => {
                loading.value = false;
                handleError(err);
            })
    }
}

const handleError = (err: any) => {
    if (err.errorMessage) errorMessage.value = getMessage(err.errorMessage);
    else errorMessage.value = getErrorMessage();
}

const closeErrorMessage = () => {
    errorMessage.value = '';
}

const closeMessage = () => {
    successMessage.value = '';
    router.push('/');
}

const isFormValid = (): boolean => {
    return isPasswordInputValid(password.value)
}
</script>

<style scoped>
.container-personal-info {
    max-width: 90%;
    width: 630px;
    margin: 30px auto;
    height: 100%;
    padding: 30px;
}

.profile-info {
    margin: 10px 0;
}

hr {
    margin: 15px 7px;
}
</style>