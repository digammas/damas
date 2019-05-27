<template>
    <app-page layout="standard">
        <main-content-file v-if="folder" :file="folder">
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
            <message-clipboard :destination="folder" @change="retrieve"/>
        </main-content-file>
    </app-page>
</template>

<script>
import folderService from '@/service/folder'
import DialogAddContent from './dialog-add-content'
import IconFile from './icon-file'
import MessageClipboard from './message-clipboard'
import MainContentFile from './main-content-file'

export default {
    name: 'PageFolder',
    data() {
        return {
            id: null,
            folder: null
        }
    },
    components: {
        MessageClipboard,
        MainContentFile,
        IconFile,
        DialogAddContent
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