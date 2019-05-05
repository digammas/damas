<template>
    <button
            type="button"
            @click="$emit('click', $event)"
            class="mdl-button mdl-js-button mdl-js-ripple-effect"
            :class="classes">
        <slot/>
    </button>
</template>

<script>
export default {
    name: "AppButton",
    props: {
        floating: Boolean,
        toolbar: Boolean,
        flat: Boolean
    },
    computed: {
        classes() {
            return {
                'mdl-button--fab': this.floating,
                'mdl-button--icon': this.toolbar,
                'mdl-button--raised': !this.flat
            }
        }
    }
}
</script>

<style scoped>

</style>