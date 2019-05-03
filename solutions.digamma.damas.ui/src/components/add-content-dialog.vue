<template>
    <app-dialog
            ref="dialogBox"
            title="New file">
        <template>
            <app-tab-container>
                <app-tab-item id="create-folder" title="Folder" selected ref="createFolderTab">
                    <app-text-input
                            id="folder-name"
                            required="required"
                            label="Folder's name"
                            v-model="folderName"/>
                </app-tab-item>
                <app-tab-item id="upload-file" title="Document" ref="uploadDocumentTab">
                    <div v-if="fileErrorMessage">{{fileErrorMessage}}</div>
                    <app-text-input label="File Name" ref="fileNameTextInput"/>
                    <app-file-upload label="File" @change="fileChanged" ref="upload"/>
                </app-tab-item>
            </app-tab-container>
        </template>
        <template #actions>
            <button
                    type="button"
                    class="mdl-button"
                    @click="create">
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

import AppTabContainer from "./widgets/app-tab-container";
import AppTabItem from "./widgets/app-tab-item";
import AppTextInput from "./widgets/app-text-input";
import AppFileUpload from "./widgets/app-file-upload";
import AppDialog from "./widgets/app-dialog";

export default {
    name: "AddContentDialog",
    components: {AppDialog, AppFileUpload, AppTextInput, AppTabItem, AppTabContainer},
    props: {
        parentId: {
            type: String,
            required: true
        }
    },
    data() {
        return {
            folderName: null,
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
        create() {
            if (this.$refs.createFolderTab.isSelected()) {
                folderService.create(this.parentId, this.folderName).then(folder => {
                    this.fireChange(folder)
                    this.folderName = null
                })
                this.hide()
            } else if (this.$refs.uploadDocumentTab.isSelected()) {
                if (this.$refs.upload.getName()) {
                    documentService.create(
                            this.parentId,
                            this.$refs.fileNameTextInput.getText(),
                            this.$refs.upload.getMimeType()).then(doc => {
                        this.fireChange(doc)
                        this.$refs.upload.binaryPayload().then(payload => {
                            documentService.upload(doc.id, payload)
                        })
                    })
                    this.hide()
                } else {
                    this.fileErrorMessage = "Please select a file to upload."
                }
            }
        },
        fileChanged(name) {
            this.fileErrorMessage = null
            this.$refs.fileNameTextInput.setText(name)
        },
        fireChange(file) {
            this.$emit('change', file)
        }
    }
}
</script>