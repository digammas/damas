<template>
    <app-layout :title="title" short-title="Damas DMS">
        <template #header>
            <app-navigation>
                <a href="https://github.com/digammas/damas">GitHub</a>
                <app-cell />
                <app-icon symbol="user" theme="light"/>
                <a href>{{username}}</a>
            </app-navigation>
        </template>
        <template #sidebar>
            <app-navigation>
                <router-link :to="{name: 'content'}" href="">Content</router-link>
                <router-link :to="{name: 'users'}" href="">Users</router-link>
            </app-navigation>
        </template>
        <template>
            <slot></slot>
        </template>
        <app-flash-message :message="errorMessage" />
    </app-layout>
</template>

<script>
import userService from '@/service/user'

export default {
    name: 'LayoutStandard',
    data() {
        return {
            errorMessage: null
        }
    },
    computed: {
        title() {
            return this.$store.state.common.title
        },
        username() {
            return this.$store.state.auth.username
        }
    },
    created() {
        this.$bus$on('error', message => {
            this.errorMessage = message
            return false
        })
    },
    mounted() {
        userService.load()
    },
    watch: {
        '$store.state.auth.token' () {
            userService.load()
        }
    }
}
</script>