<script setup lang="ts">
    
</script>

<template>
    <div class="container">
        <div class="container-not-found">
            <div class="logo">
                <a routerLink="/"><img src="../assets/logo.png">
                    <h3>My Task List</h3>
                </a>
            </div>
            <h2>Erro: 404!</h2>
            <p>This address is not accessible.</p>
        </div>
    </div>
</template>

<style scoped>
.container {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    min-height: 100vh;
}

.container-not-found {
    text-align: center;
    background-color: #FEFEFE;
    padding: 30px 80px 80px 80px;
    box-shadow: #C0C0C0 3px 3px 6px;
    margin-bottom: 150px;
}

.container-not-found h2 {
    font-size: 44px;
}

.container-not-found p {
    font-size: 28px;
}

.logo {
    padding: 10px;
    margin-bottom: 50px;
    text-decoration: none;
}

.logo img {
    width: 30px;
}

@media (max-width: 750px) {
    .container-not-found {
        max-width: 90%;
    }
}
</style>

