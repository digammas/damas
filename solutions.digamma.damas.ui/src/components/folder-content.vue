<template>
    <app-layout profile="standard">
        <div v-if="folder">
            <app-box shadow>
                <app-tag icon="folder-open">
                    <app-icon symbol="chevron-right" solid /> root
                    <span v-for="(element, index) in pathElements" :key="index">
                        <app-icon symbol="chevron-right" solid /> {{element}}
                    </span>
                </app-tag>
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
            </app-box>
            <app-row align="right" gutter>
                <app-button @click="showNewFolderDialog" floating>
                    <app-icon symbol="plus" solid />
                </app-button>
            </app-row>
            <app-dialog
                    ref="dialogBox"
                    title="New file">
                <template>
                    <app-tab-container>
                        <app-tab-item id="create-folder" title="Folder" selected ref="createFolderTab">
                            <app-text-input
                                    id="folder-name"
                                    required="required"
                                    label="Folder's name"
                                    v-model="newFolderName"/>
                        </app-tab-item>
                        <app-tab-item id="upload-file" title="Document" ref="uploadDocumentTab">
                        </app-tab-item>
                    </app-tab-container>
                </template>
                <template #actions>
                    <button
                            type="button"
                            class="mdl-button"
                            @click="create">
                        OK
                    </button>
                    <button
                            type="button"
                            class="mdl-button"
                            @click="hideNewFolderDialog">
                        Cancel
                    </button>
                </template>
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
import AppRow from "./widgets/app-row";
import AppCell from "./widgets/app-cell";
import AppIcon from "./widgets/app-icon";
import AppTag from "./widgets/app-tag";
import AppBox from "./widgets/app-box";
import AppMoreList from "./widgets/app-more-list";
import AppSpacer from "./widgets/app-spacer";
import AppTabContainer from "./widgets/app-tab-container";
import AppTabItem from "./widgets/app-tab-item";

export default {
    name: 'FolderContent',
    data() {
        return {
            id: null,
            folder: null,
            newFolderName: null
        }
    },
    computed: {
        pathElements() {
            return this.folder ? this.folder.path.split("/").filter(Boolean) : []
        }
    },
    components: {
        AppTabItem,
        AppTabContainer,
        AppSpacer,
        AppMoreList,
        AppTag,
        AppIcon,
        AppCell,
        AppRow,
        AppDialog,
        AppTextInput,
        AppButton,
        AppLayout,
        AppBox
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
            this.retrieve();
        },
        '$store.state.auth.token' () {
            content.load()
        },
        '$route' (to) {
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
        retrieve() {
            content.retrieve(this.id, 1, true).then(data => {
                this.folder = data
            })
        },
        showNewFolderDialog() {
            this.$refs.dialogBox.show()
        },
        hideNewFolderDialog() {
            this.$refs.dialogBox.hide()
        },
        create() {
            if (this.$refs.createFolderTab.isSelected()) {
                content.create(this.id, this.newFolderName).then(() => {
                    this.retrieve()
                    this.newFolderName = null
                })
            }
            this.hideNewFolderDialog()
        }
    }
}
</script>