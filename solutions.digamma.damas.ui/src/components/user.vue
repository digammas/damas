<template>
    <div>
        <h1>Welcome {{user}}</h1>
        <div>
            <router-view></router-view>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'user',
        data: () => ({
        }),
        components: {},
        computed: {
            user() {
                return this.$store.state.auth.username
            }
        }
    }
</script>
