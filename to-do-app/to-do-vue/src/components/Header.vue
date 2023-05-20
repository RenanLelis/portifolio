<template>
    <header>
        <div class="list-menu" @click="menuClick">
            <i class="fa fa-bars"></i>
        </div>
        <div class="logo">
            <a routerLink="/"><img src="../assets/logo.png"><span>My Task List</span></a>
        </div>
    </header>
</template>

<script setup lang="ts">
const emit = defineEmits(['onMenuClick'])

const menuClick = () => {
    emit("onMenuClick")
}
</script>

<style scoped>
header {
    background-color: #FDFDFD;
    margin: 0;
    padding: 3px 15px;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    box-shadow: 0 0 .2em #818181;
    min-height: 60px;
}

header .list-menu {
    align-items: center;
    justify-content: center;
    cursor: pointer;
    color: #0672A4;
    margin: auto 10px;
    padding: 10px;
    height: 100%;
}

header .logo {
    padding: 0 10px;
    display: flex;
    justify-content: center;
    align-items: center;
    text-decoration: none;
}

header .logo a {
    color: #0672A4;
    text-align: center;
    align-items: center;
    justify-content: center;
    display: flex;
    flex-direction: row;
    font-family: sans-serif;
    font-weight: bold;
}

header .logo img {
    width: 30px;
    margin: 0 1em 0 0;
}

header .logo span {
    font-size: 1.5 em;
}
</style>