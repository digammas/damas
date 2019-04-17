<template>
    <div class="mdl-grid">
        <div
                v-if="isCentered"
                class="mdl-layout-spacer"></div>
        <slot></slot>
        <div
                v-if="isCentered"
                class="mdl-layout-spacer"></div>
    </div>
</template>

<script>
    export default {
        name: "AppContainer",
        props: {
            align: {
                type: "default" | "center",
                default: "default"
            }
        },
        computed: {
            isCentered() {
                return this.align === 'center'
            }
        }
    }
</script>

<style scoped>

</style>