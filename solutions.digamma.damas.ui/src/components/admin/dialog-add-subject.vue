<template>
    <app-dialog
            ref="dialogBox"
            title="Add Subject">
        <template>
                <app-text-input
                        id="username"
                        label="Username"
                        v-model="username"/>
            <app-text-input
                    id="first-name"
                    label="First Name"
                    v-model="firstName"/>
            <app-text-input
                    id="last-name"
                    label="Last Name"
                    v-model="lastName"/>
        </template>
        <template #actions>
            <button
                    type="button"
                    class="mdl-button"
                    @click="create">
                OK
            </button>
            <button
                    type="button"
                    class="mdl-button"
                    @click="hide">
                Cancel
            </button>
        </template>
    </app-dialog>
</template>

<script>
import service from '@/service/user'

export default {
    name: "DialogAddSubject",
    props: {
    },
    data() {
        return {
            username: null,
            firstName: null,
            lastName: null
        }
    },
    methods: {
        show() {
            this.$refs.dialogBox.show()
        },
        hide() {
            this.username = this.firstName = this.lastName = ""
            this.$refs.dialogBox.hide()
        },
        create() {
            let user = {
                login: this.username,
                firstName: this.firstName,
                lastName: this.lastName,
                password: "p@55w0Rd"
            }
            service.create(user).then(user => {
                this.fireChange(user)
                this.username = null
                this.$emit('change', user)
            }).then(() => this.$bus$emit('success', "User created successfully."))
            this.hide()
        },
        fireChange(subject) {
            this.$emit('change', subject)
        }
    }
}
</script>