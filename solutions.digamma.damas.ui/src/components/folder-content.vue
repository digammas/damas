<template>
    <app-layout profile="standard">
        <div v-if="folder" class="home">
            <h1>Content Home</h1>
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
        </div>
    </app-layout>
</template>

<script>
import content from '@/service/content'
import AppLayout from "./layouts/app-layout";

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
                content.retrieveAt("/").then(folder => this.id = folder.id)
            }
        }
    }
}
</script>
