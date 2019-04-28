<template>
    <div class="mdl-grid" :class="classes">
        <div
                v-if="leftSpacer"
                class="mdl-layout-spacer"></div>
        <slot></slot>
        <div
                v-if="rightSpacer"
                class="mdl-layout-spacer"></div>
    </div>
</template>

<script>
export default {
    name: "AppRow",
    props: {
        align: {
            type: String,
            default: "default",
            validator: [].includes.bind(["default", "center", "left", "right"])
        },
        gutter: Boolean
    },
    computed: {
        leftSpacer() {
            return ["center", "right"].includes(this.align)
        },
        rightSpacer() {
            return ["center", "left"].includes(this.align)
        },
        classes() {
            return {
                'mdl-grid--no-spacing': !this.gutter
            }
        }
    }
}
</script>

<style scoped>

</style>