<template>
    <app-dialog
            ref="dialogBox"
            title="Rename File">
        <template>
                Are you sure you want to permanently delete {{fileType}} and all its content?
        </template>
        <template #actions>
            <button
                    type="button"
                    class="mdl-button"
                    @click="remove">
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
    name: "DialogDeleteFile",
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
    computed: {
        fileType() {
            return this.file.content ? "folder" : "document"
        }
    },
    methods: {
        show() {
            this.$refs.dialogBox.show()
        },
        hide() {
            this.$refs.dialogBox.hide()
        },
        remove() {
            let parentId = this.file.parentId
            let service = this.file.content ? folderService : documentService
            service.remove(this.file.id).then(() => {
                this.$router.push({name: 'content', params: {id: parentId}})
            })
            this.hide()
        }
    }
}
</script>