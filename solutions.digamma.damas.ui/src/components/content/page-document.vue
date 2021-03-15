<template>
    <app-page layout="standard">
        <main-content-file v-if="document" :file="document">
            <app-box shadow>
                <app-cell :span="12">
                    <app-text-input
                        type="textarea"
                        label="Add comment"
                        ref="input"
                        :action="commentActionEnabled ? 'paper-plane solid' : ''"
                        :lines="2"
                        @input="$_commentInput"
                        @action="$_commentAction" />
                </app-cell>
                <app-cell
                        v-for="(comment, index) in comments"
                        :key="index"
                        :span="12">
                    {{comment.text}}
                </app-cell>
            </app-box>
            <app-row align="right" gutter>
                <app-button @click="download" floating>
                    <app-icon symbol="download" solid />
                </app-button>
            </app-row>
        </main-content-file>
    </app-page>
</template>

<script>
import documentService from '@/service/document'
import commentService from '@/service/comment'
import MainContentFile from './main-content-file'
import AppTextInput from '../widgets/app-text-input'

export default {
    name: 'PageDocument',
    data() {
        return {
            id: null,
            document: null,
            comments: [],
            commentActionEnabled: null
        }
    },
    components: {
        AppTextInput,
        MainContentFile
    },
    created() {
        this.load(this.$route.params.id)
    },
    watch: {
        id() {
            this.retrieve();
        },
        '$route' (to) {
            this.load(to.params.id)
        }
    },
    methods: {
        load(id) {
            this.id = id
        },
        async retrieve() {
            this.document = await documentService.retrieve(this.id, true)
            await this.retrieveComments()
        },
        async download(event) {
            event.preventDefault()
            if (this.document) {
                let id = this.document.id
                let name = this.document.name
                let type = this.document.mimeType || "application/octet-stream"
                let blob = await documentService.download(id)
                let link = document.createElement('a')
                link.href = window.URL.createObjectURL(new Blob([blob], { type }))
                link.download = name
                link.click()
            }
        },
        async retrieveComments() {
          this.comments = await commentService.listForFile(this.id)
        },
        openRenameDialog(event) {
            this.$refs.renameFileDialog.show()
            event.preventDefault()
        },
        openDeleteDialog(event) {
            this.$refs.deleteFileDialog.show()
            event.preventDefault()
        },
        $_commentInput(value) {
            this.commentActionEnabled = !!value
        },
        async $_commentAction(value) {
            await commentService.create({
                receiverId: this.id,
                text: value
            })
            await this.retrieveComments()
            this.$refs.input.setText("")
        }
    }
}
</script>
