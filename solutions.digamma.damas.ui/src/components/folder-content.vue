<template>
    <app-page layout="standard">
        <div v-if="folder">
            <app-box shadow>
                <path-breadcrumb :path="folder && folder.path" :link="folder.parentId"/>
                <app-spacer />
                <app-more-list>
                    <a class="mdl-menu__item">Add Folder</a>
                    <a class="mdl-menu__item">Add File</a>
                    <a class="mdl-menu__item">Add Annotation</a>
                </app-more-list>
            </app-box>
            <app-box shadow>
                <file-icon
                        v-if="folder.parentId"
                        key="has-parent"
                        text="parent"
                        theme="dark"
                        symbol="arrow-alt-circle-up"
                        :link="folder.parentId" />
                <file-icon
                        v-for="subfolder in folder.content.folders"
                        :key="subfolder.id"
                        theme="dark"
                        symbol="folder"
                        :text="subfolder.name"
                        :link="subfolder.id" />
                <app-cell :span="12" />

                <file-icon
                        v-for="document in folder.content.documents"
                        :key="document.id"
                        theme="dark"
                        symbol="file"
                        :text="document.name"
                        :link="{name: 'document', params: {id: document.id}}" />
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
    </app-page>
</template>

<script>
import folderService from '@/service/folder'

import AppPage from "./layouts/app-page";
import AppButton from "./widgets/app-button";
import AppRow from "./widgets/app-row";
import AppCell from "./widgets/app-cell";
import AppIcon from "./widgets/app-icon";
import AppBox from "./widgets/app-box";
import AppMoreList from "./widgets/app-more-list";
import AppSpacer from "./widgets/app-spacer";
import AddContentDialog from "./add-content-dialog";
import PathBreadcrumb from "./path-breadcrumb";
import FileIcon from "./file-icon";

export default {
    name: 'FolderContent',
    data() {
        return {
            id: null,
            folder: null
        }
    },
    components: {
        FileIcon,
        PathBreadcrumb,
        AddContentDialog,
        AppSpacer,
        AppMoreList,
        AppIcon,
        AppCell,
        AppRow,
        AppButton,
        AppPage,
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