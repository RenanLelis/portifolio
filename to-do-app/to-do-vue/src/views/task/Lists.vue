<template>
    <div class="list-nav-container">
        <div class="title">
            <h3>My lists</h3>
            <button class="send-button" @click="newTaskList">New Task List</button>
        </div>
        <div v-if="taskStore.lists && taskStore.lists !== null && taskStore.lists.length > 0" class="my-task-lists">
            <hr class="solid">
            <ul>
                <li @click="showAllMyTasks">
                    <span>Show All Tasks</span>
                </li>
                <li @click="selectTaskList(taskList)" v-for="taskList in taskStore.lists">
                    <span>{{ taskList.listName }}</span>
                </li>
            </ul>
        </div>
    </div>
</template>

<script setup lang="ts">
import type { TaskList } from '@/model/task_list';
import { useTaskStore } from '@/stores/task_store';

const emit = defineEmits(['onNewTaskList', 'onShowAllTasks', 'onSelectTaskList']);
const taskStore = useTaskStore();

const newTaskList = () => {
    emit('onNewTaskList');
}

const showAllMyTasks = () => {
    emit('onShowAllTasks');
}

const selectTaskList = (taskList: TaskList) => {
    emit('onSelectTaskList', taskList);
}

</script>

<style scoped>
.list-nav-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
}

.title {
    width: 100%;
}

.title h3 {
    padding: 0 0 20px 0;
}

.title button {
    margin: 5px 10%;
    max-width: 80%;
}

.my-task-lists {
    margin-top: 20px;
    text-align: center;
    width: 100%;
}

.my-task-lists ul {
    list-style-type: none;
    width: 100%;
}

.my-task-lists ul li {
    width: 100%;
}

.my-task-lists ul li:hover span {
    background-color: rgba(215, 215, 215, 0.95);
    cursor: pointer;
}

.my-task-lists ul li span {
    padding: 8px;
    text-decoration: none;
    font-size: 18px;
    color: #A1A1A1;
    display: block;
    transition: 0.2s;
    width: 100%;
}

.my-task-lists ul li:hover span {
    color: #006698;
}
</style>