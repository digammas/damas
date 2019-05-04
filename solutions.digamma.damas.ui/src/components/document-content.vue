<template>
    <app-page layout="standard">
        <div v-if="document">
            <app-box shadow>
                <path-breadcrumb :path="document && document.path" :link="{name: 'content', params: {id: document.parentId}}"/>
            </app-box>
            <app-row align="right" gutter>
                <app-button @click="download" floating>
                    <app-icon symbol="download" solid />
                </app-button>
            </app-row>
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
import AppButton from "./widgets/app-button";
import AppIcon from "./widgets/app-icon";

export default {
    name: 'DocumentContent',
    data() {
        return {
            id: null,
            document: null
        }
    },
    components: {
        AppIcon,
        AppButton,
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