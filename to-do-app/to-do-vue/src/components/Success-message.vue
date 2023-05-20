<template>
    <div class="background" v-if="message != null && message.length > 0">

        <div class="message success-message">
            <div class="close-button">
                <i class="fa fa-close" @click="close"></i>
            </div>
            <div>
                <h2>Info</h2>
                <p> {{ message }} </p>
            </div>
        </div>


    </div>
</template>

<script setup lang="ts">
defineProps(['message'])
const emit = defineEmits(['onClose'])

const close = () => {
    emit("onClose")
}
</script>

<style scoped>
.success-message {
    background-color: rgba(250, 250, 250, 1);
    padding: 15px 5px;
    text-align: center;
    border-radius: 2px;
    border: 1px solid rgba(250, 250, 250, 1);
}

.success-message p {
    padding: 30px;
    font-size: 16px;
    font-family: sans-serif;
    letter-spacing: 1px;
    align-self: center;
}

.success-message .close-button i {
    padding-right: 10px;
    cursor: pointer;
}
</style>