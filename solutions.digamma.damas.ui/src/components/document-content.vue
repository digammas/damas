<template>
    <app-layout profile="standard">
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
    </app-layout>
</template>

<script>
import documentService from '@/service/document'

import AppLayout from "./layouts/app-layout";
import AppButton from "./widgets/app-button";
import AppRow from "./widgets/app-row";
import AppCell from "./widgets/app-cell";
import AppIcon from "./widgets/app-icon";
import AppBox from "./widgets/app-box";
import AppMoreList from "./widgets/app-more-list";
import AppSpacer from "./widgets/app-spacer";
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
        AppSpacer,
        AppMoreList,
        AppIcon,
        AppCell,
        AppRow,
        AppButton,
        AppLayout,
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