<template>
    <div class="mdl-cell mdl-cell--12-col">
        <div class="mdl-grid" :class="{'mdl-shadow--2dp': shadow}">
            <slot></slot>
        </div>
    </div>
</template>

<script>
export default {
    name: "AppBox",
    props: {
        shadow: Boolean
    }
}
</script>

<style scoped>

</style>