<template>
    <app-page layout="standard">
        <layout-file v-if="document" :file="document">
            <template>
                <app-row align="right" gutter>
                    <app-button @click="download" floating>
                        <app-icon symbol="download" solid />
                    </app-button>
                </app-row>
            </template>
        </layout-file>
    </app-page>
</template>

<script>
import documentService from '@/service/document'

import AppPage from "@/components/layouts/app-page";
import AppRow from "@/components/widgets/app-row";
import AppBox from "@/components/widgets/app-box";
import AppButton from "@/components/widgets/app-button";
import AppIcon from "@/components/widgets/app-icon";
import LayoutFile from "@/components/content/layout-file";

export default {
    name: 'DocumentContent',
    data() {
        return {
            id: null,
            document: null
        }
    },
    components: {
        LayoutFile,
        AppIcon,
        AppButton,
        AppRow,
        AppPage,
        AppBox
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