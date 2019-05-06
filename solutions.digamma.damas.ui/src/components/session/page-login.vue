<template>
    <app-page layout="empty">
        <div>
            <h2>User Login</h2>
            <app-flash-message ref="errorMessage" />
            <div>
                <app-row align="center">
                    <app-text-input
                            id="username"
                            label="Username"
                            required="required"
                            v-model="username" />
                </app-row>
                <app-row align="center">
                    <app-text-input
                            id="password"
                            label="Password"
                            required="required"
                            type="password"
                            v-model="password" />
                </app-row>
                <app-row align="center" gutter>
                    <app-checkbox id="remember" text="Remember me" />
                </app-row>
                <app-row align="center">
                    <app-button @click="submit">Submit</app-button>
                </app-row>
            </div>
        </div>
    </app-page>
</template>

<script>
import auth from "@/service/auth"
import AppTextInput from "@/components/widgets/app-text-input";
import AppButton from "@/components/widgets/app-button"
import AppRow from "@/components/widgets/app-row";
import AppCheckbox from "@/components/widgets/app-checkbox";
import AppFlashMessage from "@/components/widgets/app-flash-message";
import AppPage from "@/components/layouts/app-page"

export default {
    name: "PageLogin",
    components: {AppPage, AppFlashMessage, AppCheckbox, AppRow, AppButton, AppTextInput},
    data() {
        return {
            username: "",
            password: "",
            error: false
        }
    },
    methods: {
        submit() {
            this.error = false
            let redirect = this.$route.query.redirect
            auth.authenticate(
                this.username,
                this.password
            ).then(token => {
                if (token) {
                    this.$router.push(redirect || {name: "content"})
                } else {
                    this.error = "Bad username or password"
                }
            }).catch(reason => {
                this.error = reason.message
            })
        }
    },
    watch :{
        error() {
            if (this.error) {
                this.$refs.errorMessage.show(this.error)
            }
        }
    }
}
</script>

<style scoped>
</style>