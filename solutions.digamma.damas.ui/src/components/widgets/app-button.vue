<template>
    <button
            type="button"
            @click="$emit('click', $event)"
            class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect"
            :class="classes">
        <slot/>
    </button>
</template>

<script>
export default {
    name: "AppButton",
    props: {
        floating: Boolean
    },
    computed: {
        classes() {
            return {
                'mdl-button--fab': this.floating
            }
        }
    }
}
</script>

<style scoped>

</style>