<template>
    <div class="container" @click="closeMenuList">
        <!-- ----------------  selected list  ---------------- -->

        <div class="list">
            <div class="list-info">
                <div v-if="taskStore.isSetShowAllTasks"><!--spacer--></div>
                <h2 v-if="taskStore.isSetShowAllTasks">All My Tasks</h2>
                <div><!--spacer--></div>
                <h2 v-if="!taskStore.isSetShowAllTasks && taskStore.selectedList !== null && taskStore.selectedList.id > 0">
                    {{ taskStore.selectedList!.listName }}</h2>
                <div v-if="!taskStore.isSetShowAllTasks && taskStore.selectedList !== null && taskStore.selectedList.id > 0"
                    class="list-actions-dropdown">
                    <i class="fa fa-ellipsis-v" @click="dropMenuList($event)"></i>

                    <div v-if="showMenuList" class="list-actions-dropdown-content">
                        <ul>
                            <li @click="newList">
                                <i class="fa fa-plus">
                                    <span> New List</span>
                                </i>
                            </li>

                            <li v-if="!taskStore.isSetShowAllTasks && taskStore.selectedList !== null && taskStore.selectedList.id > 0"
                                @click="editList">
                                <i class="fa-solid fa-pen-to-square">
                                    <span> Edit List</span>
                                </i>
                            </li>

                            <li v-if="!taskStore.isSetShowAllTasks && taskStore.tasks.length > 0" @click="completeList">
                                <i class="fa fa-check-square">
                                    <span> Complete All tasks</span>
                                </i>
                            </li>

                            <li v-if="!taskStore.isSetShowAllTasks && taskStore.tasks.length > 0" @click="uncompleteList">
                                <i class="fa fa-square">
                                    <span> Uncomplete all tasks</span>
                                </i>
                            </li>

                            <li v-if="!taskStore.isSetShowAllTasks && taskStore.tasks.length > 0 && taskStore.lists.length > 1"
                                @click="showMoveTaskOption(null)">
                                <i class="fa-solid fa-arrow-up-right-from-square">
                                    <span> Move tasks to...</span>
                                </i>
                            </li>

                            <li v-if="!taskStore.isSetShowAllTasks && taskStore.selectedList !== null && taskStore.selectedList.id > 0 && taskStore.lists.length > 1"
                                @click="deleteList" class="delete">
                                <i class="fa fa-trash">
                                    <span> Delete List</span>
                                </i>
                            </li>

                        </ul>
                    </div>
                </div>
            </div>
        </div>

        <!-- ----------------  tasks  ---------------- -->

        <div class="exib-options">
            <button class="send-button" @click="newTask">New Task</button>
        </div>
        <div class="tasks">
            <div class="list-tasks" v-if="exibTasks">
                <div v-for="task in taskStore.tasks" :key="task.id">
                    <TaskVue :task="task.id" @on-edit="editTask(task)" @on-delete="deleteTask(task.id)"
                        @on-move="showMoveTaskOption(task.id)" @on-coplete="completeTask(task.id)"
                        @on-uncomplete="uncompleteTask(task.id)" />
                </div>
            </div>
        </div>
    </div>

    <div v-if="showListSelector">
        <ListSelector :lists="getListsToSelect()" @on-close="closeListSelector"
            @on-select-list="selectListToMoveTask($event)" />
    </div>

    <div v-if="loading">
        <Loading />
    </div>

    <ErrorMessage :errorMessage="errorMessage" @on-close="closeErrorMessage" />
    <ConfirmMessage :message="confirmMessageDelete" @on-close="closeConfirmMessageDelete" @on-confirm="confirmDeleteList" />
    <ConfirmMessage :message="confirmMessageCompleteList" @on-close="closeConfirmMessageCompleteList"
        @on-confirm="confirmCompleteList" />
    <ConfirmMessage :message="confirmMessageUncompleteList" @on-close="closeConfirmMessageUncompleteList"
        @on-confirm="confirmUncompleteList" />
</template>
<script setup lang="ts">
import Loading from '@/components/Loading.vue';
import ErrorMessage from '@/components/Error-message.vue';
import ListSelector from '@/components/List-selector.vue';
import ConfirmMessage from '@/components/Confirm-message.vue';
import TaskVue from './Task.vue';

import type { Task } from '@/model/task';
import type { TaskList } from '@/model/task_list';
import { getErrorMessage, getMessage, getMessageConfirmCompleteList, getMessageConfirmDelete, getMessageConfirmUncompleteList } from '@/msg/messages';
import { useTaskStore } from '@/stores/task_store';
import { ref, type Ref } from 'vue';
import router from '@/router';


const taskStore = useTaskStore();
const exibTasks = ref(true);
const showListSelector = ref(false);
const showMenuList = ref(false);
const loading = ref(false);
const errorMessage = ref('');
const confirmMessageDelete = ref('');
const confirmMessageCompleteList = ref('');
const confirmMessageUncompleteList = ref('');
const selectedTask: Ref<number | null> = ref(null);

const getListsToSelect = () => {
    if (taskStore.selectedList === null) { return taskStore.lists }
    return taskStore.lists.filter(list => list.id.toString() !== taskStore.selectedList!.id.toString())
}

const dropMenuList = (event: any) => {
    event.stopPropagation();
    showMenuList.value = !showMenuList.value;
}

const closeMenuList = () => {
    if (showMenuList) {
        showMenuList.value = false;
    }
}


// ----------------------------------------------

const newList = () => {
    closeMenuList();
    router.push(`/home/list/new`);
}

const editList = () => {
    closeMenuList();
    if (taskStore.selectedList !== null) {
        router.push(`/home/list/${taskStore.selectedList.id}`);
    }
}

const deleteList = () => {
    closeMenuList();
    if (taskStore.lists.length > 1) {
        confirmMessageDelete.value = getMessageConfirmDelete();
    }
}

const confirmDeleteList = () => {
    closeConfirmMessageDelete()
    if (taskStore.selectedList !== null && taskStore.selectedList!.id > 0) {
        loading.value = true;
        taskStore.deleteTaskList(taskStore.selectedList!.id)
            .then(() => {
                loading.value = false;
            })
            .catch(err => handleError(err))
    }
}

const completeList = () => {
    closeMenuList();
    confirmMessageCompleteList.value = getMessageConfirmCompleteList();
}

const confirmCompleteList = () => {
    closeConfirmMessageCompleteList()
    if (taskStore.selectedList !== null) {
        loading.value = true;
        exibTasks.value = false;
        taskStore.completeTasksFromList(taskStore.selectedList!.id)
            .then(() => {
                loading.value = false;
                exibTasks.value = true;
            })
            .catch(err => handleError(err))
    }
}

const uncompleteList = () => {
    closeMenuList();
    confirmMessageUncompleteList.value = getMessageConfirmUncompleteList();
}

const confirmUncompleteList = () => {
    closeConfirmMessageUncompleteList()
    if (taskStore.selectedList !== null) {
        loading.value = true;
        exibTasks.value = false;
        taskStore.uncompleteTasksFromList(taskStore.selectedList!.id)
            .then(() => {
                loading.value = false;
                exibTasks.value = true;
            })
            .catch(err => handleError(err))
    }
}

const moveTasksFromList = (listOrigin: TaskList, listDestiny: TaskList) => {
    closeListSelector();
    loading.value = true;
    exibTasks.value = false;
    taskStore.moveTasksFromList(listOrigin.id, listDestiny.id)
        .then(() => {
            handleResponse();
        })
        .catch(err => handleError(err))
}

// ----------------------------------------------

const newTask = () => {
    router.push(`/home/task/new`);
}

const editTask = (task: Task) => {
    router.push(`/home/task/${task.id}`);
}

const deleteTask = (taskID: number) => {
    loading.value = true;
    taskStore.deleteTask(taskID)
        .then(() => { loading.value = false; })
        .catch(err => handleError(err))
}

const completeTask = (taskID: number) => {
    taskStore.completeTask(taskID)
        .then(() => { handleResponse() })
        .catch(err => handleError(err))
}

const uncompleteTask = (taskID: number) => {
    taskStore.uncompleteTask(taskID)
        .then(() => { handleResponse() })
        .catch(err => handleError(err))
}

const showMoveTaskOption = (taskID: number | null) => {
    selectedTask.value = taskID;
    showListSelector.value = true;
}

const moveTaskToList = (task: number, list: TaskList) => {
    closeListSelector();
    loading.value = true;
    exibTasks.value = false;
    taskStore.moveTaskToList(task, list.id)
        .then(() => { handleResponse() })
        .catch(err => handleError(err))
}

const selectListToMoveTask = (list: TaskList) => {
    if (selectedTask.value !== null) { moveTaskToList(selectedTask.value, list); }
    else { moveTasksFromList(taskStore.selectedList!, list) }
}

// ----------------------------------------------

const handleResponse = () => {
    loading.value = false;
    exibTasks.value = true;
}

const handleError = (err: any) => {
    loading.value = false;
    exibTasks.value = true;
    console.log(err);
    if (err.errorMessage) errorMessage.value = getMessage(err.errorMessage);
    else errorMessage.value = getErrorMessage();
}

const closeConfirmMessageDelete = () => {
    confirmMessageDelete.value = "";
}

const closeConfirmMessageCompleteList = () => {
    confirmMessageCompleteList.value = "";
}

const closeConfirmMessageUncompleteList = () => {
    confirmMessageUncompleteList.value = "";
}

const closeListSelector = () => {
    showListSelector.value = false;
    selectedTask.value = null;
}

const closeErrorMessage = () => {
    errorMessage.value = '';
}

const initializeView = () => {
    if (!taskStore.isAppInitialized) {
        taskStore.fetchTasksAndLists()
            .then(() => {
            })
            .catch(err => handleError(err))
    }
}

initializeView();

</script>
<style scoped>
.container {
    max-width: 90%;
    width: 700px;
    margin: 30px auto;
    height: 100%;
    padding: 30px;
}

.list-info {
    padding-bottom: 2px;
    margin-bottom: 3px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 2px solid #A0A0A0;
}

.list-actions-dropdown {
    margin: 5px;
    position: relative;
    display: inline-block;
}

.list-actions-dropdown i {
    color: #A0A0A0;
    padding: 10px;
    cursor: pointer;
}

.list-actions-dropdown-content ul {
    list-style-type: none;
    width: 230px;
}

.list-actions-dropdown-content ul li {
    width: 100%;
    padding: 8px 5px;
}

.list-actions-dropdown-content ul li:hover {
    background-color: rgba(225, 225, 225, 0.95);
    cursor: pointer;
}

.list-actions-dropdown-content ul li i {
    color: #0672A4;
}

.list-actions-dropdown-content ul li span {
    padding: 8px;
    text-decoration: none;
    font-size: 18px;
    color: #0672A4;
    transition: 0.2s;
    width: 100%;
}

.list-actions-dropdown-content ul li:hover i,
.list-actions-dropdown-content ul li:hover span {
    color: #006698;
}

.list-actions-dropdown-content {
    display: block;
    position: absolute;
    right: 0;
    background-color: #f9f9f9;
    min-width: 100px;
    box-shadow: 0px 8px 16px 0px rgba(0, 0, 0, 0.2);
    z-index: 1;
}

.list-actions-dropdown-content ul li.delete i {
    color: #A22029;
}

.list-actions-dropdown-content ul li.delete i span {
    color: #A22029;
}

.list-actions-dropdown-content ul li.delete:hover i {
    color: #820009;
}

.list-actions-dropdown-content ul li.delete:hover i span {
    color: #820009;
}

/* ----------------------- */

.option-menu {
    padding: 5px;
    font-size: 16px;
}

/* ----------------------------- */
/* Tasks */

.tasks {
    width: 100%;
    margin: 10px auto;
}

.exib-options {
    display: flex;
    align-items: center;
    justify-content: flex-end;
    margin: 10px 5px;
}

.exib-options i {
    padding: 5px;
    cursor: pointer;
}

.send-button {
    max-width: 200px;
}

.card-tasks {
    width: 100%;
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    justify-content: flex-start;
}

.list-tasks {
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: stretch;
}
</style>