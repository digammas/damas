<template>
    <button
            type="button"
            @click="$emit('click', $event)"
            class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect">
        <slot/>
    </button>
</template>

<script>
export default {
    name: "AppButton"
}
</script>

<style scoped>

</style>