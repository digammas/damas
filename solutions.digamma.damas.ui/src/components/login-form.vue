<template>
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
            <app-row align="center" gutter="on">
                <app-checkbox id="remember" text="Remember me" />
            </app-row>
            <app-row align="center">
                <app-button text="Submit" @click="submit"/>
            </app-row>
        </div>
    </div>
</template>

<script>
import auth from "@/service/auth"
import AppTextInput from "./widgets/app-text-input";
import AppButton from "./widgets/app-button"
import AppRow from "./widgets/app-row";
import AppCheckbox from "./widgets/app-checkbox";
import AppFlashMessage from "./widgets/app-flash-message";

export default {
    name: "LoginForm",
    components: {AppFlashMessage, AppCheckbox, AppRow, AppButton, AppTextInput},
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