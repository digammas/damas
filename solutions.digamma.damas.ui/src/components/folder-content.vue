<template>
    <app-layout profile="standard">
        <div v-if="folder">
            <app-box shadow>
                <path-breadcrumb :path="folder && folder.path" />
                <app-spacer />
                <app-more-list>
                    <a class="mdl-menu__item">Add Folder</a>
                    <a class="mdl-menu__item">Add File</a>
                    <a class="mdl-menu__item">Add Annotation</a>
                </app-more-list>
            </app-box>
            <app-box shadow>
                <app-cell :span="2" v-if="folder.parentId" key="has-parent">
                    <app-row align="center">
                        <app-icon size="big" theme="dark" symbol="arrow-alt-circle-up" />
                    </app-row>
                    <app-row align="center">
                        <router-link :to="folder.parentId">
                            parent
                        </router-link>
                    </app-row>
                </app-cell>
                <app-cell :span="2" v-for="subfolder in folder.content.folders" :key="subfolder.id">
                    <app-row align="center">
                        <app-icon size="big" theme="dark" symbol="folder" />
                    </app-row>
                    <app-row align="center">
                        <router-link :to="subfolder.id">
                            {{subfolder.name}}
                        </router-link>
                    </app-row>
                </app-cell>
                <app-cell :span="12" />
                <app-cell :span="2" v-for="document in folder.content.documents" :key="document.id">
                    <app-row align="center">
                        <app-icon size="big" theme="dark" symbol="file" />
                    </app-row>
                    <app-row align="center">
                        <router-link :to="{name: 'document', params: {id: document.id}}">
                            {{document.name}}
                        </router-link>
                    </app-row>
                </app-cell>
            </app-box>
            <app-row align="right" gutter>
                <app-button @click="openAddContentDialog" floating>
                    <app-icon symbol="plus" solid />
                </app-button>
            </app-row>
            <add-content-dialog
                    ref="addContentDialog"
                    :parentId="id"
                    @change="retrieve"/>
        </div>
    </app-layout>
</template>

<script>
import folderService from '@/service/folder'

import AppLayout from "./layouts/app-layout";
import AppButton from "./widgets/app-button";
import AppRow from "./widgets/app-row";
import AppCell from "./widgets/app-cell";
import AppIcon from "./widgets/app-icon";
import AppTag from "./widgets/app-tag";
import AppBox from "./widgets/app-box";
import AppMoreList from "./widgets/app-more-list";
import AppSpacer from "./widgets/app-spacer";
import AddContentDialog from "./add-content-dialog";
import PathBreadcrumb from "./path-breadcrumb";

export default {
    name: 'FolderContent',
    data() {
        return {
            id: null,
            folder: null
        }
    },
    components: {
        PathBreadcrumb,
        AddContentDialog,
        AppSpacer,
        AppMoreList,
        AppTag,
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
            if (!this.id) {
                this.folder = null
            } else {
                this.retrieve();
            }
        },
        '$store.state.auth.token' () {
            folderService.load()
        },
        '$route' (to) {
            this.load(to.params.id)
        }
    },
    methods: {
        load(id) {
            if (!id) {
                /* No folder ID provided, fetch ID for root */
                this.redirect()
            } else {
                this.id = id
            }
        },
        redirect(path = "/") {
            folderService.retrieveAt(path).then(f => {
                this.$router.push({name: "content", params: {id: f.id }})
            })
        },
        retrieve() {
            folderService.retrieve(this.id, 1, true).then(data => {
                this.folder = data
            })
        },
        openAddContentDialog() {
            this.$refs.addContentDialog.show()
        }
    }
}
</script>