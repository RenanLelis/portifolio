<template>
    <div class="task-form">
        <h2>Task</h2>
        <form @submit.prevent="save">
            <div class="label-field">
                <label class="label">Task Name</label>
                <input v-model="taskName" type="text" required class="text-field">
            </div>
            <div class="label-field">
                <label class="label">Task Description</label>
                <textarea v-model="taskDescription" class="textarea"></textarea>
            </div>
            <div class="label-field">
                <label class="label">Task Deadline</label>
                <input type="date" v-model="taskDeadline" class="text-field">
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
const taskName = ref('');
const taskDescription = ref('');
const taskDeadline = ref('');
const route = useRoute();
const id = route.params.id.toString();

const taskStore = useTaskStore();

const checkIfIsEditingTask = () => {
    if (id !== "new") {
        const idList = Number(id);
        if (idList && !isNaN(idList)) {
            const currentTask = taskStore.getTaskById(idList);
            if (currentTask == null) {
                router.push({ name: 'not-found' });
            } else {
                taskName.value = currentTask.taskName;
                taskDescription.value = currentTask.taskDescription !== null ? currentTask.taskDescription : '';
                taskDeadline.value = currentTask.deadline !== null ? currentTask.deadline : '';
            }
        } else {
            router.push({ name: 'not-found' });
        }
    }
}

checkIfIsEditingTask();

const save = () => {
    if (taskName.value.trim().length <= 0 || !isDeadlineValid(taskDeadline.value)) {
        errorMessage.value = getErrorMessageInputValues();
    } else {
        const idTask = Number(id);
        if (idTask && !isNaN(idTask)) {
            saveTaskEdited(idTask);
        } else {
            saveNewTask();
        }
    }
}

const saveTaskEdited = (id: number) => {
    loading.value = true;
    taskStore.updateTask(id, taskName.value.trim(), taskDescription.value.trim(), taskDeadline.value.trim())
        .then(() => {
            loading.value = false;
            router.push('/');
        })
        .catch(err => {
            loading.value = false;
            handleError(err);
        })
}

const saveNewTask = () => {
    if (taskStore.selectedList === null || taskStore.selectedList.id <= 0) {
        router.push('/');
    } else {
        loading.value = true;
        taskStore.createTask(
            taskName.value.trim(),
            taskDescription.value.trim(),
            taskDeadline.value.trim(),
            taskStore.selectedList!.id)
            .then(() => {
                loading.value = false;
                router.push('/');
            })
            .catch(err => {
                loading.value = false;
                handleError(err);
            })
    }
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

const isDeadlineValid = (deadline: string | null): boolean => {
    if (deadline === null || deadline.trim().length <= 0) return true;
    const deadlineFormatted = deadline.trim();
    if (deadlineFormatted.length !== 10) return false;
    var regEx = /^\d{4}-\d{2}-\d{2}$/;
    if (!deadlineFormatted.match(regEx)) return false;  // Invalid format
    var d = new Date(deadlineFormatted);
    var dNum = d.getTime();
    if (!dNum && dNum !== 0) return false; // NaN value, Invalid date
    return d.toISOString().slice(0, 10) === deadlineFormatted;
}

</script>
<style scoped></style>