<template>
    <div ref="container" class="mdl-js-snackbar mdl-snackbar">
        <div class="mdl-snackbar__text"></div>
        <button class="mdl-snackbar__action" type="button"></button>
    </div>
</template>

<script>
export default {
    name: "AppFlashMessage",
    data() {
        return {
            message: null,
            timeout: 2750
        }
    },
    methods: {
        show(message, timeout) {
            this.$refs.container.MaterialSnackbar.showSnackbar({
                message: message || this.message,
                timeout: timeout || this.timeout
            });
        }
    }
}
</script>

<style scoped>

</style>