<template>
    <div v-if="folder" class="home">
        <h1>Content Home</h1>
        <div>Current folder ID {{id}}</div>
        <div>Current folder Path {{folder.path}}</div>
        <div v-if="folder.content.folders.length != 0">
            <h2>List of subfolders</h2>
            <ul v-for="subfolder in folder.content.folders">
                <li><router-link :to="subfolder.id">{{subfolder.name}}</router-link></li>
            </ul>
        </div>
    </div>
</template>

<script>
import content from '@/service/content'

export default {
    name: 'content',
    data() {
        return {
            id: null,
            folder: null
        }
    },
    components: {
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
