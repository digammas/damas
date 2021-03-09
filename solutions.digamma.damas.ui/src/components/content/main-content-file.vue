<template>
    <div v-if="file">
        <app-box shadow>
            <tag-breadcrumb
                    :path="file && file.path"
                    @navigate="onNavigate"/>
            <app-spacer />
            <app-hint
                    v-if="$_hasParent()"
                    text="Parent Folder">
                <app-button
                        toolbar
                        flat
                        @click="goToParent">
                    <app-icon symbol="arrow-up" solid size="small"/>
                </app-button>
            </app-hint>
            <app-more-list
                    v-if="$_hasParent()">
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
import DialogRenameFile from './dialog-rename-file'
import TagBreadcrumb from './tag-breadcrumb'
import DialogDeleteFile from './dialog-delete-file'

export default {
    name: "MainContentFile",
    components: {
        DialogDeleteFile,
        DialogRenameFile,
        TagBreadcrumb
    },
    props: {
        file: Object
    },
    methods: {
        goToParent() {
            this.$_hasParent() && this.$router.push({name: 'content', params: {id: this.file.parentId}})
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
        onNavigate(index) {
            const id = this.file.pathIds[index + 1]
            id !== this.file.id && this.$router.push({ name: 'content', params: { id } })
        },
        $_hasParent() {
            return this.file && this.file.parentId
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
