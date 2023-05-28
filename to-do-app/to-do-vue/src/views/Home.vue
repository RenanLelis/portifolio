<template>
    <div class="container">
        <Header @on-menu-click="onMenuClick" />
        <div class="content">
            <RouterView />
        </div>
        <Footer />
    </div>

    <div class="sidenav-home" :class="{ 'sidenav-home-open': showMenu }">
        <div class="close-button">
            <i class="fa fa-close" @click="onMenuClick()"></i>
        </div>
        <Lists @on-new-task-list="onNewTaskList" @on-show-all-tasks="showAllTasks" @on-select-task-list="selectTaskList($event)"/>
        <hr class="solid">
        <div class="user-options">
            <i class="fa fa-user-circle"><span> User Options</span></i>
            <ul>
                <li @click="onMenuClick()"><router-link to="/home/profile">My Profile</router-link></li>
                <li @click="onMenuClick()"><router-link to="/home/mypassword">Change My Password</router-link></li>
                <li @click="onMenuClick()"><router-link to="/logout">Logout</router-link></li>
            </ul>
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
import { useTaskStore } from '@/stores/task_store';
import { ref } from 'vue'
import Loading from '@/components/Loading.vue';
import ErrorMessage from '@/components/Error-message.vue';
import SuccessMessage from '@/components/Success-message.vue';
import type { TaskList } from '@/model/task_list';
import Header from '@/components/Header.vue';
import Footer from '@/components/Footer.vue';
import Lists from './task/Lists.vue';
import { RouterView } from 'vue-router'


const showMenu = ref(false);
const loading = ref(false);
const errorMessage = ref('');
const successMessage = ref('');

const taskStore = useTaskStore()

const getTasksAndLists = () => {
    taskStore.fetchTasksAndLists()
        .then(() => { })
        .catch(err => handleError(err))
}

const onMenuClick = () => {
    showMenu.value = !showMenu.value;
    if (showMenu.value) {
        getTasksAndLists();
    }
}

const onNewTaskList = () => {
    onMenuClick();
    router.push('/home/list/new');
}

const showAllTasks = () => {
    taskStore.setShowAllTasks();
    onMenuClick();
    router.push({ name: 'taskview' });
}

const selectTaskList = (selectedTaskList: TaskList) => {
    taskStore.selectTaskList(selectedTaskList);
    onMenuClick();
    router.push({ name: 'taskview' });
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
}

getTasksAndLists();

</script>
<style scoped>
.container {
    height: 100%;
    width: 100%;
    min-height: 100vh;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
}

.content {
    flex: 1;
    display: flex;
    flex-direction: column;
}

.sidenav-home {
    height: 100%;
    width: 0;
    position: fixed;
    z-index: 1;
    top: 0;
    left: 0;
    background-color: #FDFDFD;
    overflow-x: hidden;
    transition: 0.4s;
    padding-top: 15px;
    box-shadow: 0 .15em .4em #818181;
}

.sidenav-home-open {
    width: 400px;
    max-width: 100%;
}

.sidenav-home .close-button {
    width: 100%;
    display: flex;
    justify-content: flex-end;
}

.sidenav-home .close-button i {
    margin-right: 10px;
    padding: 10px;
    cursor: pointer;
    color: #006698;
}

.list-menu {
    cursor: pointer;
    color: #006698;
    margin: 20px 10px;
    padding: 10px;
    max-width: 30px;
}

.user-options {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    margin-top: 15px;
    text-align: center;
}

.user-options i {
    padding: 10px;
}

.user-options ul {
    list-style-type: none;
    width: 100%;
}

.user-options ul li {
    width: 100%;
}

.user-options ul li:hover a {
    background-color: rgba(215, 215, 215, 0.95);
    cursor: pointer;
}

.user-options ul li a {
    padding: 8px;
    text-decoration: none;
    font-size: 18px;
    color: #A1A1A1;
    display: block;
    transition: 0.2s;
    width: 100%;
}

.user-options ul li:hover a {
    color: #006698;
}

@media (max-width: 800px) {

    .sidenav-home {
        padding-top: 15px;
    }

    .sidenav-home-open {
        width: 380px;
        max-width: 100%;
    }

    .sidenav-home a {
        font-size: 18px;
    }

}
</style>