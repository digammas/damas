<template>
    <app-page layout="standard">
        <file-content v-if="document" :file="document">
            <template #options>
                <a class="mdl-menu__item">Add Folder</a>
                <a class="mdl-menu__item">Add File</a>
                <a class="mdl-menu__item">Add Annotation</a>
            </template>
            <template>
                <app-row align="right" gutter>
                    <app-button @click="download" floating>
                        <app-icon symbol="download" solid />
                    </app-button>
                </app-row>
            </template>
        </file-content>
    </app-page>
</template>

<script>
import documentService from '@/service/document'

import AppPage from "./layouts/app-page";
import AppRow from "./widgets/app-row";
import AppBox from "./widgets/app-box";
import AppButton from "./widgets/app-button";
import AppIcon from "./widgets/app-icon";
import FileContent from "./file-content";

export default {
    name: 'DocumentContent',
    data() {
        return {
            id: null,
            document: null
        }
    },
    components: {
        FileContent,
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
        }
    }
}
</script>