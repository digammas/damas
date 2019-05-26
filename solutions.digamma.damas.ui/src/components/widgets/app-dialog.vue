<template>
    <dialog
            ref="dialog"
            class="mdl-dialog"
            @click="$_onClick">
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
    name: "AppDialog",
    props: {
        title: String
    },
    methods: {
        show() {
            this.$refs.dialog.showModal()
        },
        hide() {
            this.$refs.dialog.close()
        },
        $_onClick(event) {
            let rect = this.$refs.dialog.getBoundingClientRect();
            if (
                    event.clientY <= rect.top ||
                    event.clientY > rect.top + rect.height ||
                    event.clientX <= rect.left ||
                    event.clientX > rect.left + rect.width) {
                this.hide()
            }
        }
    }
}
</script>

<style>
.mdl-dialog__title {
    font-size: 1.5em;
}
</style>