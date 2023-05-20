<template>
    <div class="background" v-if="lists && lists !== null && lists.length > 0">
        <div class="message list-select">
            <div class="content">
                <h2>Select the list</h2>

                <div class="my-task-lists">
                    <ul>
                        <li @click="selectList(taskList)" v-for="taskList in lists">
                            <span>{{ taskList.listName }}</span>
                        </li>
                    </ul>
                </div>

                <div class="actions">
                    <button @click="close">Cancel</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import type { TaskList } from '@/model/task_list';

defineProps(['lists'])
const emit = defineEmits(['onClose', 'onSelectList'])

const close = () => {
    emit("onClose")
}

const selectList = (list: TaskList) => {
    emit("onSelectList", list)
}
</script>

<style scoped>
.list-select {
    min-width: 250px;
    background-color: rgba(250, 250, 250, 1);
    padding: 20px;
    text-align: center;
    border-radius: 2px;
    border: 1px solid rgba(250, 250, 250, 1);
}

.content {
    width: 100%;
}

.actions {
    margin: 15px;
    display: flex;
    justify-content: center;
}

.actions button {
    margin: 0 5px;
    padding: 10px 15px;
    cursor: pointer;
    border-radius: 30px;
    transition: .3s;
    border: none;
    color: #F0F0F0;
    background-color: #0672A4;
}

.actions button:hover {
    color: #FFFFFF;
    background-color: #006698;
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