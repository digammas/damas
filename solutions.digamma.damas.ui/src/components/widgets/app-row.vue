<template>
    <div class="mdl-grid" :class="classes">
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
            type: String,
            default: "default",
            validator: [].includes.bind(["default", "center"])
        },
        gutter: Boolean
    },
    computed: {
        isCentered() {
            return this.align === 'center'
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