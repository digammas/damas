<template>
    <app-layout profile="standard">
        <div v-if="document">
            <app-box shadow>
                <path-breadcrumb :path="document && document.path" />
                <app-spacer />
                <app-more-list>
                    <a class="mdl-menu__item">Add Folder</a>
                    <a class="mdl-menu__item">Add File</a>
                    <a class="mdl-menu__item">Add Annotation</a>
                </app-more-list>
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
        }
    }
}
</script>