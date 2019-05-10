<template>
    <div v-if="!!clipboard">
        <app-floating-message :active="!!clipboard" closable @action="$_clearClipboard">
            <span v-if="validAction" key="validAction">
                <a href>{{actionVerb}}</a>&nbsp;<strong>{{fileName}}</strong> here?
            </span>
            <span v-else key="invalidAction">
                Cannot {{actionVerb.toLowerCase()}} <strong>{{fileName}}</strong> here.
            </span>
        </app-floating-message>
    </div>
</template>

<script>
import AppFloatingMessage from "@/components/widgets/app-floating-message";

export default {
    name: "MessageClipboard",
    props: {
        destination: {
            type: Object,
            required: true
        }
    },
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
        },
        validAction() {
            return !!this.clipboard
                && !this.destination.path.startsWith(this.clipboard.file.path)
                && this.destination.path !== this.clipboard.file.path.split("/").slice(0, -1).join("/")
        }
    },
    methods: {
        $_clearClipboard() {
            this.$store.dispatch("content/update", { clipboard: null })
        }
    }
}
</script>