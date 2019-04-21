<template>
    <button
            type="button"
            @click="$emit('click', $event)"
            class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect">
        {{text}}
    </button>
</template>

<script>
export default {
    name: "AppButton",
    props: {
        text: String
    }
}
</script>

<style scoped>

</style>