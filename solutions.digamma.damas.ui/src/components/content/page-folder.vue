<template>
    <app-page layout="standard">
        <layout-file v-if="folder" :file="folder">
            <template #options>
                <a
                        class="mdl-menu__item"
                        href
                        @click="openRenameDialog($event)">
                    Rename Folder
                </a>
                <a
                        class="mdl-menu__item"
                        href
                        @click="openDeleteDialog($event)">
                    Delete Folder
                </a>
            </template>
            <template>
                <app-box shadow>
                    <icon-file
                            v-if="folder.parentId"
                            key="has-parent"
                            text="parent"
                            theme="dark"
                            symbol="arrow-alt-circle-up"
                            :link="folder.parentId" />
                    <icon-file
                            v-for="subfolder in folder.content.folders"
                            :key="subfolder.id"
                            theme="dark"
                            symbol="folder"
                            :text="subfolder.name"
                            :link="subfolder.id" />
                    <app-cell :span="12" />
                    <icon-file
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
                <dialog-add-content
                        ref="addContentDialog"
                        :parentId="id"
                        @change="retrieve"/>
                <dialog-rename-file
                        ref="renameFileDialog"
                        :file="folder"
                        @change="retrieve"/>
                <dialog-delete-file
                        ref="deleteFileDialog"
                        :file="folder"/>
            </template>
        </layout-file>
    </app-page>
</template>

<script>
import folderService from '@/service/folder'

import AppPage from "@/components/layouts/app-page";
import AppButton from "@/components/widgets/app-button";
import AppRow from "@/components/widgets/app-row";
import AppCell from "@/components/widgets/app-cell";
import AppIcon from "@/components/widgets/app-icon";
import AppBox from "@/components/widgets/app-box";
import DialogAddContent from "@/components/content/dialog-add-content";
import DialogRenameFile from "@/components/content/dialog-rename-file";
import DialogDeleteFile from "@/components/content/dialog-delete-file";
import IconFile from "@/components/content/icon-file";
import LayoutFile from "@/components/content/layout-file";

export default {
    name: 'PageFolder',
    data() {
        return {
            id: null,
            folder: null
        }
    },
    components: {
        LayoutFile,
        IconFile,
        DialogAddContent,
        DialogRenameFile,
        DialogDeleteFile,
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