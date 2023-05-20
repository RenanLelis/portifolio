<template>
    <div class="background" v-if="errorMessage != null && errorMessage.length > 0">

        <div class="message error-message">
            <div class="close-button">
                <i class="fa fa-close" @click="close"></i>
            </div>
            <div>
                <h2>Error</h2>
                <p> {{ errorMessage }} </p>
            </div>
        </div>


    </div>
</template>

<script setup lang="ts">

defineProps(['errorMessage'])
const emit = defineEmits(['onClose'])

const close = () => {
    emit("onClose")
}

</script>

<style>
.error-message {
    background-color: #F8D7DA;
    padding: 15px 5px;
    text-align: center;
    border-radius: 2px;
    border: 1px solid #F5C2C7;
}

.error-message h2 {
    color: #A22029;
}

.error-message p {
    padding: 30px;
    color: #A22029;
    font-size: 16px;
    font-family: sans-serif;
    letter-spacing: 1px;
    align-self: center;
}

.error-message .close-button i {
    padding-right: 10px;
    cursor: pointer;
    color: #A22029;
}
</style>