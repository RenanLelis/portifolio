<template></template>
<script setup lang="ts">
import router from '@/router';
import { useUserStore } from '@/stores/user_store';

const userStore = useUserStore();
userStore.logout();
router.push('/login');

</script>
<style></style>