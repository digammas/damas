<template>
    <div v-if="!!clipboard">
        <app-floating-message :active="!!clipboard" closable @action="$_onCancel">
            <span v-if="validAction" key="validAction">
                <a href @click="$_onClick">{{actionVerb}}</a>&nbsp;<strong>{{fileName}}</strong> here?
            </span>
            <span v-else key="invalidAction">
                Cannot {{actionVerb.toLowerCase()}} <strong>{{fileName}}</strong> here.
            </span>
        </app-floating-message>
    </div>
</template>

<script>
import AppFloatingMessage from "@/components/widgets/app-floating-message";

import folderService from '@/service/folder'
import documentService from '@/service/document'

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
        $_onCancel() {
            this.$store.dispatch("content/update", { clipboard: null })
        },
        async $_onClick(event) {
            event.preventDefault()
            let service = this.clipboard.file.content ? folderService : documentService
            if (this.clipboard.type == 'move') {
                await service.move(this.clipboard.file.id, this.destination.id)
                this.$store.dispatch("content/update", { clipboard: null })
                this.$emit('change')
                this.$bus$emit('success', "Moved successfully")
            }
        }
    }
}
</script>