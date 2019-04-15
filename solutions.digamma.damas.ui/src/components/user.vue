<template>
    <div>
        <h1>Welcome {{commonName}}</h1>
        <div>
            <router-view></router-view>
        </div>
    </div>
</template>

<script>
import { mapState } from 'vuex'
import user from '@/service/user'

export default {
    name: 'user',
    data() {
        return {}
    },
    components: {},
    computed: {
        ...mapState({
            username: (state) => state.auth.username,
            firstName: (state) => state.user.firstName,
            lastName: (state) => state.user.lastName
        }),
        commonName() {
            return this.firstName || this.username
        },
    },
    mounted() {
        user.load()
    },
    watch: {
        '$store.state.auth.token' () {
            user.load()
        }
    }
}
</script>
