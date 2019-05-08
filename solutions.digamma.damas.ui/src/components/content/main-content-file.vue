<template>
    <div v-if="file">
        <app-box shadow>
            <path-breadcrumb :path="file && file.path"/>
            <app-spacer />
            <app-button
                    toolbar
                    flat
                    @click="goToParent">
                <app-icon symbol="arrow-up" solid size="small"/>
            </app-button>
            <app-more-list>
                <slot name="options"></slot>
                <a
                        href
                        @click="openRenameDialog">
                    Rename
                </a>
                <a
                        href
                        @click="openDeleteDialog">
                    Delete
                </a>
                <a
                        href
                        @click="$_copyFile">
                    Copy
                </a>
                <a
                        href
                        @click="$_moveFile">
                    Move
                </a>
            </app-more-list>
        </app-box>
        <slot></slot>
        <dialog-rename-file
                ref="renameFileDialog"
                :file="file"
                @change="onNameChanged"/>
        <dialog-delete-file
                ref="deleteFileDialog"
                :file="file"/>
    </div>
</template>

<script>
import AppBox from "@/components/widgets/app-box";
import AppSpacer from "@/components/widgets/app-spacer";
import AppButton from "@/components/widgets/app-button";
import AppIcon from "@/components/widgets/app-icon";
import AppMoreList from "@/components/widgets/app-more-list";
import DialogRenameFile from "./dialog-rename-file";
import PathBreadcrumb from "./tag-breadcrumb";
import DialogDeleteFile from "./dialog-delete-file";

export default {
    name: "MainContentFile",
    components: {
        DialogDeleteFile,
        DialogRenameFile,
        AppMoreList,
        AppIcon,
        AppButton,
        AppSpacer,
        PathBreadcrumb,
        AppBox
    },
    props: {
        file: Object
    },
    methods: {
        goToParent() {
            this.file && this.file.parentId && this.$router.push({name: 'content', params: {id: this.file.parentId}})
        },
        openRenameDialog(event) {
            this.$refs.renameFileDialog.show()
            event.preventDefault()
        },
        openDeleteDialog(event) {
            this.$refs.deleteFileDialog.show()
            event.preventDefault()
        },
        onNameChanged(file) {
            this.file.name = file.name
        },
        $_copyFile(event) {
            if (!this.file) return
            this.$store.dispatch("content/update", {
                clipboard: {
                    type: 'copy',
                    file: this.file
                }
            })
            event.preventDefault()
        },
        $_moveFile(event) {
            if (!this.file) return
            this.$store.dispatch("content/update", {
                clipboard: {
                    type: 'move',
                    file: this.file
                }
            })
            event.preventDefault()
        }
    }
}
</script>