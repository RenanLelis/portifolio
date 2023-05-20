<template>
    <footer>
        <!-- Footer Section Start -->
        <div class="footer">
            <div class="politics">
                <p>© Renan Lelis. All rights reserved.</p>
            </div>
            <div class="sitecreation">
                <p>2023 | Created by <a target="_blank" href="https://renanlelis.github.io/renanlelis/">Renan
                        Lelis</a></p>
            </div>
        </div>
    </footer>
</template>

<script setup lang="ts">

</script>

<style scoped>
footer {
    background-color: #353535;
    margin-top: auto;
}

p {
    color: #F0F0F0;
    padding: 10px;
}

.footer {
    background-color: #353535;
    padding: 15px 0;
    width: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
}

.footer .sitecreation {
    padding: 0 15px;
}

.footer .politics {
    font-size: 17px;
    font-weight: 400;
}

.footer a {
    font-weight: 500;
    color: #FFFFFF;
}
</style>