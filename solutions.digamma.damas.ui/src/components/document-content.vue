<template>
    <app-page layout="standard">
        <div v-if="document">
            <app-box shadow>
                <path-breadcrumb :path="document && document.path" />
            </app-box>
            <app-box shadow>
                <app-cell :span="2">
                    <app-row align="center">
                        <app-icon size="big" theme="dark" symbol="arrow-alt-circle-up" />
                    </app-row>
                    <app-row align="center">
                        <router-link :to="{name: 'content', id: document.parentId}">
                            parent
                        </router-link>
                    </app-row>
                </app-cell>
                <app-cell :span="2">
                    <app-row align="center">
                        <app-icon size="big" theme="dark" symbol="download" solid />
                    </app-row>
                    <app-row align="center">
                        <a href="#" @click="download">
                            download
                        </a>
                    </app-row>

                </app-cell>
            </app-box>
        </div>
    </app-page>
</template>

<script>
import documentService from '@/service/document'

import AppPage from "./layouts/app-page";
import AppRow from "./widgets/app-row";
import AppCell from "./widgets/app-cell";
import AppIcon from "./widgets/app-icon";
import AppBox from "./widgets/app-box";
import PathBreadcrumb from "./path-breadcrumb";

export default {
    name: 'DocumentContent',
    data() {
        return {
            id: null,
            document: null
        }
    },
    components: {
        PathBreadcrumb,
        AppIcon,
        AppCell,
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
        async download() {
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