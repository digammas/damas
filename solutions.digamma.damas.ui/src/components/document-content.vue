<template>
    <app-page layout="standard">
        <div v-if="document">
            <app-box shadow>
                <path-breadcrumb :path="document && document.path" />
            </app-box>
            <app-box shadow>
                <file-icon
                        theme="dark"
                        symbol="arrow-alt-circle-up"
                        :link="{name: 'content', params: {id: document.parentId}}"
                        text="parent" />
                <file-icon
                        theme="dark"
                        symbol="download"
                        solid
                        @click="download"
                        text="download" />
            </app-box>
        </div>
    </app-page>
</template>

<script>
import documentService from '@/service/document'

import AppPage from "./layouts/app-page";
import AppRow from "./widgets/app-row";
import AppBox from "./widgets/app-box";
import PathBreadcrumb from "./path-breadcrumb";
import FileIcon from "./file-icon";

export default {
    name: 'DocumentContent',
    data() {
        return {
            id: null,
            document: null
        }
    },
    components: {
        FileIcon,
        PathBreadcrumb,
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
        }
    }
}
</script>