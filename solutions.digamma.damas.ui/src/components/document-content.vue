<template>
    <app-page layout="standard">
        <div v-if="document">
            <app-box shadow>
                <path-breadcrumb
                        :path="document && document.path"
                        :link="{name: 'content', params: {id: document.parentId}}"/>
                <app-spacer />
                <app-button
                        toolbar
                        flat
                        @click="goToParent">
                    <app-icon symbol="arrow-up" solid size="small"/>
                </app-button>
                <app-more-list>
                    <a class="mdl-menu__item">Add Folder</a>
                    <a class="mdl-menu__item">Add File</a>
                    <a class="mdl-menu__item">Add Annotation</a>
                </app-more-list>
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
import AppSpacer from "./widgets/app-spacer";
import AppMoreList from "./widgets/app-more-list";

export default {
    name: 'DocumentContent',
    data() {
        return {
            id: null,
            document: null
        }
    },
    components: {
        AppMoreList,
        AppSpacer,
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
        },
        goToParent() {
            this.document && this.$router.push({name: 'content', params: {id: this.document.parentId}})
        }
    }
}
</script>