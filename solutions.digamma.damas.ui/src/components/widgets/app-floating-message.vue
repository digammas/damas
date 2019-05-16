<template>
    <div
            ref="container"
            class="mdl-snackbar"
            :class="classes">
        <div class="mdl-snackbar__text">
            <slot></slot>
        </div>
        <button
                class="mdl-snackbar__action"
                type="button">
            <slot name="action">
                <a
                        href
                        v-if="closable"
                        @click="$_onCancelClick">
                    Cancel
                </a>
            </slot>
        </button>
    </div>
</template>

<script>
export default {
    name: "AppFloatingMessage",
    props: {
        closable: Boolean,
        active: Boolean
    },
    computed: {
        classes() {
            return {
                'mdl-snackbar--active': this.active && !this.hidden
            }
        }
    },
    methods: {
        $_onCancelClick(event) {
            this.$emit('action')
            event.preventDefault()
        }
    }
}
</script>