<template>
    <app-layout :title="title" short-title="Damas DMS">
        <template #header>
            <app-navigation>
                <a href="https://github.com/digammas/damas">GitHub</a>
                <a href="https://digamma.co">Digamma Solution</a>
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
import { mapState } from 'vuex'
import userService from '@/service/user'
import AppLayout from "@/components/widgets/app-layout";
import AppNavigation from "@/components/widgets/app-navigation";
import AppFlashMessage from "@/components/widgets/app-flash-message";

export default {
    name: 'LayoutStandard',
    data() {
        return {
            errorMessage: null
        }
    },
    components: {AppFlashMessage, AppNavigation, AppLayout},
    computed: {
        ...mapState({
            username: (state) => state.auth.username,
            firstName: (state) => state.user.firstName,
            lastName: (state) => state.user.lastName,
            title: (state) => state.common.title
        })
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