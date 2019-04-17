<template>
    <div>
        <h2>User Login</h2>
        <div v-if="error">
            <p>{{error}}</p>
        </div>
        <div>
            <app-text-input
                    id="username"
                    label="username"
                    required="required"
                    placeholder="Enter username"
                    v-model="username" />

            <app-text-input
                    id="password"
                    label="password"
                    required="required"
                    type="password"
                    placeholder="Enter password"
                    v-model="password" />
            <app-container align="center">
                <app-button text="Submit" @click="submit"/>
            </app-container>
            <app-container align="center">
                <app-checkbox id="remember" text="Remember me" />
            </app-container>

        </div>
    </div>
</template>

<script>
import auth from "@/service/auth"
import AppTextInput from "./widgets/app-text-input";
import AppButton from "./widgets/app-button"
import AppContainer from "./widgets/app-container";
import AppCheckbox from "./widgets/app-checkbox";

export default {
    name: "LoginForm",
    components: {AppCheckbox, AppContainer, AppButton, AppTextInput},
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
    }
}
</script>

<style scoped>
</style>