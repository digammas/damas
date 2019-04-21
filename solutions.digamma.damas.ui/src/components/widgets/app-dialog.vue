<template>
    <dialog ref="dialog" class="mdl-dialog">
        <h6
                v-if="title"
                class="mdl-dialog__title">
            {{title}}
        </h6>
        <slot/>
    </dialog>
</template>

<script>
export default {
    name: "app-dialog",
    props: {
        title: String
    },
    methods: {
        show() {
            this.$refs.dialog.showModal()
        },
        hide() {
            this.$refs.dialog.close()
        }
    }
}
</script>