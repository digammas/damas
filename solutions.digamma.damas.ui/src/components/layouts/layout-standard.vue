<template>
    <div>
        <h6>Welcome {{commonName}}</h6>
        <div>
            <slot/>
        </div>
    </div>
</template>

<script>
import { mapState } from 'vuex'
import user from '@/service/user'

export default {
    name: 'LayoutStandard',
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