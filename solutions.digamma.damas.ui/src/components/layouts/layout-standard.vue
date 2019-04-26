<template>
    <app-layout title="Damas DMS" short-title="Damas">
        <template #links>
            <a href="https://github.com/digammas/damas">GitHub</a>
            <a href="https://digamma.co">Digamma Solution</a>
        </template>
        <template #navigation>
            <router-link :to="{name: 'content'}" href="">Content</router-link>
            <router-link :to="{name: 'users'}" href="">Users</router-link>
        </template>
        <template>
            <slot></slot>
        </template>
    </app-layout>
</template>

<script>
import { mapState } from 'vuex'
import user from '@/service/user'
import AppLayout from "../widgets/app-layout";

export default {
    name: 'LayoutStandard',
    data() {
        return {}
    },
    components: {AppLayout},
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