<template>
    <div class="background" v-if="message != null && message.length > 0">

        <div class="message confirm-message">
            <div>
                <h2>Confirmation</h2>
                <p> {{ message }} </p>
                <div class="actions">
                    <button @click="close">Cancel</button>
                    <button @click="confirm">Confirm</button>
                </div>
            </div>
        </div>


    </div>
</template>

<script setup lang="ts">

defineProps(['message'])
const emit = defineEmits(['onClose', 'onConfirm'])

const close = () => {
    emit("onClose")
}

const confirm = () => {
    emit("onConfirm")
}

</script>

<style scoped>
.confirm-message {
    background-color: rgba(250, 250, 250, 1);
    padding: 15px;
    text-align: center;
    border-radius: 2px;
    border: 1px solid rgba(250, 250, 250, 1);
}

.confirm-message p {
    padding: 30px;
    font-size: 16px;
    font-family: sans-serif;
    letter-spacing: 1px;
    align-self: center;
}

.actions {
    display: flex;
    justify-content: space-around;
    margin-left: 3px;
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
</style>