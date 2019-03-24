<template>
    <div class="home">
        <h1>Content Home</h1>
        <p>Current folder ID {{currentId}}</p>
        <p>Current folder Path {{path}}</p>
    </div>
</template>

<script>
    import { mapState } from 'vuex'
    import content from '@/service/content'

    export default {
        name: 'content',
        data() {
            return {
                folder: null
            }
        },
        components: {
        },
        computed: {
            ...mapState({
                currentId: (state) => state.content.currentFolderId
            }),
            path() {
                return this.folder ? this.folder.path : "?"
            }
        },
        mounted() {
            content.load()
        },
        watch: {
            currentId() {
                content.retrieve(this.currentId).then(data => {
                    this.folder = data
                })
            },
            '$store.state.auth.token' () {
                content.load()
            }
        }
    }
</script>
