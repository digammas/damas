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
                    <app-text-input label="File Name" ref="fileNameTextInput"/>
                    <app-file-upload label="File" @change="fileChanged" />
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
import AppTabContainer from "./widgets/app-tab-container";
import AppTabItem from "./widgets/app-tab-item";
import AppTextInput from "./widgets/app-text-input";
import AppFileUpload from "./widgets/app-file-upload";

import content from '@/service/content'
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
            folderName: null
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
                content.create(this.parentId, this.folderName).then(folder => {
                    this.$emit('change', folder)
                    this.folderName = null
                })
            }
            this.hide()
        },
        fileChanged(name) {
            this.$refs.fileNameTextInput.setText(name)
        }
    }
}
</script>