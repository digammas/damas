<template>
    <app-dialog
            ref="dialogBox"
            title="Rename File">
        <template>
            <div v-if="fileErrorMessage">{{fileErrorMessage}}</div>
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

import AppTabContainer from "@/components/widgets/app-tab-container";
import AppTabItem from "@/components/widgets/app-tab-item";
import AppTextInput from "@/components/widgets/app-text-input";
import AppFileUpload from "@/components/widgets/app-file-upload";
import AppDialog from "@/components/widgets/app-dialog";

export default {
    name: "DialogRenameFile",
    components: {AppDialog, AppFileUpload, AppTextInput, AppTabItem, AppTabContainer},
    props: {
        file: {
            type: Object,
            required: true
        }
    },
    data() {
        return {
            name: null,
            fileErrorMessage: null
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
                this.fileErrorMessage = "Name is the same as old name!"
            }
        },
        fireChange(file) {
            this.$emit('change', file)
        }
    }
}
</script>