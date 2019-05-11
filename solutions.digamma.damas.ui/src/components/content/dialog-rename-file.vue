<template>
    <app-dialog
            ref="dialogBox"
            title="Rename">
        <template>
            <app-text-input
                    id="file-name"
                    required="required"
                    label="File's new name"
                    v-model="name"/>
        </template>
        <template #actions>
            <button
                    type="button"
                    class="mdl-button"
                    @click="rename">
                OK
            </button>
            <button
                    type="button"
                    class="mdl-button"
                    @click="hide">
                Cancel
            </button>
        </template>
    </app-dialog>
</template>

<script>
import folderService from '@/service/folder'
import documentService from '@/service/document'
import AppTextInput from "@/components/widgets/app-text-input";

import AppDialog from "@/components/widgets/app-dialog";

export default {
    name: "DialogRenameFile",
    components: {
        AppDialog,
        AppTextInput
    },
    props: {
        file: {
            type: Object,
            required: true
        }
    },
    data() {
        return {
            name: null
        }
    },
    methods: {
        show() {
            this.$refs.dialogBox.show()
        },
        hide() {
            this.$refs.dialogBox.hide()
        },
        rename() {
            if (this.file.name !== this.name) {
                let service = this.file.content ? folderService : documentService
                service.rename(this.file.id, this.name).then(file => {
                    this.fireChange(file)
                    this.name = null
                })
                this.hide()
            } else {
                this.$bus$emit('error', "Name is the same as old name!")
            }
        },
        fireChange(file) {
            this.$emit('change', file)
        }
    }
}
</script>