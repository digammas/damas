<template>
    <dialog ref="dialog" class="mdl-dialog">
        <h6
                v-if="title"
                class="mdl-dialog__title">
            {{title}}
        </h6>
        <div class="mdl-dialog__content">
            <slot></slot>
        </div>
        <div class="mdl-dialog__actions">
            <slot name="actions"></slot>
        </div>
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

<style>
.mdl-dialog__title {
    font-size: 1.5em;
}
</style>