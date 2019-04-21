<template>
    <div class="mdl-grid" :class="gutterClass">
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
    name: "AppRow",
    props: {
        align: {
            type: "default" | "center",
            default: "default"
        },
        gutter: {
            type: "on" | "off",
            default: "off"
        }
    },
    computed: {
        isCentered() {
            return this.align === 'center'
        },
        gutterClass() {
            return this.gutter !== "on" && "mdl-grid--no-spacing"
        }
    }
}
</script>

<style scoped>

</style>