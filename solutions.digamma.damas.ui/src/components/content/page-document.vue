<template>
    <app-page layout="standard">
        <main-content-file v-if="document" :file="document">
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
import MainContentFile from './main-content-file'

export default {
    name: 'PageDocument',
    data() {
        return {
            id: null,
            document: null
        }
    },
    components: {
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
        openRenameDialog(event) {
            this.$refs.renameFileDialog.show()
            event.preventDefault()
        },
        openDeleteDialog(event) {
            this.$refs.deleteFileDialog.show()
            event.preventDefault()
        }
    }
}
</script>