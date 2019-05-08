<template>
    <div v-if="!!clipboard">
        <app-floating-message :active="!!clipboard" closable @action="$_clearClipboard">
            <span v-if="clipboard" key="clipboardFull">
                <a href>{{actionVerb}}</a>&nbsp;<strong>{{fileName}}</strong> here?
            </span>
        </app-floating-message>
    </div>
</template>

<script>
import AppFloatingMessage from "../widgets/app-floating-message";

export default {
    name: "message-clipboard",
    components: {AppFloatingMessage},
    computed: {
        clipboard() {
            return this.$store.state.content.clipboard
        },
        fileType() {
            return this.clipboard && this.clipboard.file.content ? "Folder" : "Document"
        },
        fileName() {
            return this.clipboard && this.clipboard.file.name
        },
        actionVerb() {
            return this.clipboard && this.clipboard.type === 'move' ? "Move" : "Copy"
        }
    },
    methods: {
        $_clearClipboard(event) {
            this.$store.dispatch("content/update", { clipboard: null })
        }
    }
}
</script>