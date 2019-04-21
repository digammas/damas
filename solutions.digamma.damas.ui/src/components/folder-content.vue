<template>
    <app-layout profile="standard">
        <div v-if="folder" class="home">
            <h3>Content Home</h3>
            <div>Current folder ID {{id}}</div>
            <div>Current folder Path {{folder.path}}</div>
            <div v-if="folder.parentId" key="has-parent">
                <router-link :to="folder.parentId">Parent Directory</router-link>
            </div>
            <div v-if="folder.content.folders.length != 0" key="has-subfolders">
                <h2>List of subfolders</h2>
                <ul v-for="subfolder in folder.content.folders" :key="subfolder.id">
                    <li><router-link :to="subfolder.id">{{subfolder.name}}</router-link></li>
                </ul>
            </div>
            <div v-else key="has-subfolders">
                <span>No subfolfers in this directory</span>
            </div>
            <app-button
                    text="New folder"
                    @click="showNewFolderDialog"/>
            <app-dialog
                    ref="dialogBox"
                    title="New folder">
                <app-dialog-content>
                    <app-text-input
                            id="folder-name"
                            ref="folderName"
                            required="required"
                            label="Folder's name"/>
                </app-dialog-content>
                <app-dialog-actions>
                    <button
                            type="button"
                            class="mdl-button">
                        Create
                    </button>
                    <button
                            type="button"
                            class="mdl-button"
                            @click="hideNewFolderDialog">
                        Cancel
                    </button>
                </app-dialog-actions>
            </app-dialog>
        </div>
    </app-layout>
</template>

<script>
import content from '@/service/content'
import AppLayout from "./layouts/app-layout";
import AppButton from "./widgets/app-button";
import AppTextInput from "./widgets/app-text-input";
import AppDialog from "./widgets/app-dialog";
import AppDialogContent from "./widgets/app-dialog-content";
import AppDialogActions from "./widgets/app-dialog-actions";

export default {
    name: 'FolderContent',
    data() {
        return {
            id: null,
            folder: null
        }
    },
    computed: {
    },
    components: {
        AppDialogActions,
        AppDialogContent,
        AppDialog,
        AppTextInput,
        AppButton,
        AppLayout
    },
    mounted() {
        this.load(this.$route.params.id)
    },
    watch: {
        id() {
            if (!this.id) {
                this.folder = null
                return
            }
            content.retrieve(this.id, 1, true).then(data => {
                this.folder = data
            })
        },
        '$store.state.auth.token' () {
            content.load()
        },
        '$route' (to, from) {
            this.load(to.params.id)
        }
    },
    methods: {
        load(id) {
            if (id) {
                this.id = id
            } else {
                /* No folder ID provided, fetch ID for root */
                content.retrieveAt("/").then(folder => this.id = folder.id)
            }
        },
        showNewFolderDialog() {
            this.$refs.dialogBox.show()
        },
        hideNewFolderDialog() {
            this.$refs.dialogBox.hide()
        }
    }
}
</script>
