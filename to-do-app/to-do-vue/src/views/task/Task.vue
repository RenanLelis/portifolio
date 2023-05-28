<template>
    <div class="task" :class="{ 'completed': isTaskComplete }">
        <div class="task-completion">
            <input type="checkbox" :checked="isTaskComplete" @change="changeStatusTask" />
        </div>
        <div class="task-name">
            <span>{{ task.taskName }}</span>
        </div>
        <div class="actions">
            <button @click="move" v-if="taskStore.lists.length > 1"><i
                    class="fa-solid fa-arrow-up-right-from-square"></i></button>
            <button @click="edit"><i class="fa fa-pencil"></i></button>
            <button @click="deleteTask"><i class="fa fa-trash delete"></i></button>
        </div>
    </div>
</template>
<script setup lang="ts">
import { STATUS_COMPLETE, STATUS_INCOMPLETE, type Task } from '@/model/task';
import { useTaskStore } from '@/stores/task_store';
import { computed } from '@vue/reactivity';


const taskStore = useTaskStore()
const emit = defineEmits(['onUncomplete', 'onCoplete', 'onDelete', 'onEdit', 'onMove']);
const taskId = defineProps(['task'])
const task = taskStore.getTaskById(taskId.task)!

const isTaskComplete = computed(() => {
    return task.taskStatus === STATUS_COMPLETE;
})

const changeStatusTask = () => {
    if (task.taskStatus === STATUS_COMPLETE) {
        task.taskStatus = STATUS_INCOMPLETE;
        emit('onUncomplete');
    } else {
        task.taskStatus = STATUS_COMPLETE;
        emit('onCoplete');
    }
}

const deleteTask = () => {
    emit('onDelete');
}

const edit = () => {
    emit('onEdit');
}

const move = () => {
    emit('onMove');
}

</script>
<style scoped>
.task {
    width: 100%;
    margin-top: 10px;
    display: flex;
    border: .5px solid #F0F0F0;
    padding: 10px;
    border-radius: 3px;
    box-shadow: 0 .15em .4em #C0C0C0;
    flex-wrap: nowrap;
}

.completed {
    text-decoration: line-through;
}

.task-completion {
    min-width: 20px;
    align-self: center;
    margin-top: 2.5px;
}

.task-name {
    margin: 3.5px 0 3.5px 2.5px;
    width: 100%;
}

.actions {
    display: flex;
}

.actions button {
    border: none;
    padding: 6px;
    cursor: pointer;
    background-color: transparent;
    border-radius: 50%;
    font-size: 13px;
}

.actions button i {
    transition: all 0.2s;
    color: #006698;
}

.actions button i.delete {
    color: #A22029;
}

.actions button:hover {
    background-color: #D0D0D0;

}
</style>