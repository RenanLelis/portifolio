<template>
    <div class="task-form">
        <h2>Task List</h2>
        <form @submit.prevent="save">
            <div class="label-field">
                <label class="label">List Name</label>
                <input v-model="listName" type="text" required class="text-field">
            </div>
            <div class="label-field">
                <label class="label">List Description</label>
                <textarea v-model="listDescription" class="textarea"></textarea>
            </div>
            <div class="div-submit-button">
                <button type="button" @click="cancel" class="action-button">Cancel</button>
                <button type="submit" class="action-button">Send</button>
            </div>
        </form>
    </div>

    <div v-if="loading">
        <Loading />
    </div>

    <ErrorMessage :errorMessage="errorMessage" @on-close="closeErrorMessage" />
</template>
<script setup lang="ts">
import Loading from '@/components/Loading.vue';
import ErrorMessage from '@/components/Error-message.vue';
import { ref } from 'vue';
import { getErrorMessage, getErrorMessageInputValues, getMessage } from '@/msg/messages';
import router from '@/router';
import { useTaskStore } from '@/stores/task_store';
import { useRoute } from 'vue-router';

const loading = ref(false);
const errorMessage = ref('');
const listName = ref('');
const listDescription = ref('');
const route = useRoute();
const id = route.params.id.toString();

const taskStore = useTaskStore();

const checkIfIsEditingList = () => {
    if (id !== "new") {
        const idList = Number(id);
        if (idList && !isNaN(idList)) {
            const currentList = taskStore.getListById(idList);
            if (currentList == null) {
                router.push({ name: 'not-found' });
            } else {
                listName.value = currentList.listName;
                listDescription.value = currentList.listDescription;
            }
        } else {
            router.push({ name: 'not-found' });
        }
    }
}

checkIfIsEditingList();

const save = () => {
    if (listName.value.trim().length <= 0) {
        errorMessage.value = getErrorMessageInputValues();
    } else {
        const idList = Number(id);
        if (idList && !isNaN(idList)) {
            saveListEdited(idList);
        } else {
            saveNewList();
        }
    }
}

const saveNewList = () => {
    loading.value = true;
    taskStore.createTaskList(listName.value.trim(), listDescription.value.trim())
        .then(() => {
            loading.value = false;
            router.push('/');
        })
        .catch(err => {
            loading.value = false;
            handleError(err);
        })
}

const saveListEdited = (idList: number) => {
    loading.value = true;
    taskStore.updateTaskList(listName.value.trim(), listDescription.value.trim(), idList)
        .then(() => {
            loading.value = false;
            router.push('/');
        })
        .catch(err => {
            loading.value = false;
            handleError(err);
        })
}

const cancel = () => {
    router.push('/');
}

const handleError = (err: any) => {
    if (err.errorMessage) errorMessage.value = getMessage(err.errorMessage);
    else errorMessage.value = getErrorMessage();
}

const closeErrorMessage = () => {
    errorMessage.value = '';
}

</script>
<style scoped></style>