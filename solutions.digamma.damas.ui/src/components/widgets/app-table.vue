<template>
    <table
            class="mdl-data-table mdl-js-data-table"
            :class="classes"
            ref="table">
        <slot></slot>
    </table>
</template>

<script>
export default {
    name: "AppTable",
    props: {
        selectable: Boolean,
        shadow: Boolean
    },
    computed: {
        classes() {
            return {
                'mdl-shadow--2dp': this.shadow
            }
        }
    },
    mounted() {
        componentHandler.upgradeElement(this.$refs.table)
    }
}
</script>

<style scoped>

.mdl-data-table td {
    text-align: inherit;
}

</style>