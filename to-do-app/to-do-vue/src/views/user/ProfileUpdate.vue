<template>
    <div class="container-personal-info">
        <h2>My Personal Information</h2>
        <div class="profile-info">
            <div class="label-field">
                <label class="label"> {{ firstName }} {{ lastName }}
                </label>
            </div>
        </div>
        <hr />
        <div class="profile-form">
            <form @submit.prevent="updateProfile()">
                <div class="label-field">
                    <label class="label">First Name</label>
                    <input v-model="firstName" type="text" required class="text-field">
                </div>
                <div class="label-field">
                    <label class="label">Last Name</label>
                    <input v-model="lastName" type="text" class="text-field">
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
import Loading from '@/components/Loading.vue';
import ErrorMessage from '@/components/Error-message.vue';
import SuccessMessage from '@/components/Success-message.vue';

const userStore = useUserStore();
const loading = ref(false);
const errorMessage = ref('');
const successMessage = ref('');
const firstName = ref(userStore.user!.name);
const lastName = ref(userStore.user!.lastName && userStore.user!.lastName !== null ? userStore.user!.lastName : '');

const updateProfile = () => {
    if (!isFormValid()) {
        errorMessage.value = getErrorMessageInputValues()
    } else {
        loading.value = true;
        userStore.updateUserProfile(firstName.value.trim(), lastName.value.trim())
            .then(() => {
                loading.value = false;
                console.log(getMessageOperationSucceded())
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
    return firstName.value !== null && firstName.value.trim().length > 0
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