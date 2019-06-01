<template>
    <app-dialog
            ref="dialogBox"
            :title="title">
        <template>
            <app-text-input
                    id="username"
                    label="Username"
                    v-model="user.login"/>
            <app-text-input
                    id="first-name"
                    label="First Name"
                    v-model="user.firstName"/>
            <app-text-input
                    id="last-name"
                    label="Last Name"
                    v-model="user.lastName"/>
            <app-row>
                <app-text-input
                    id="groups"
                    label="Groups"
                    action="plus solid"
                    @action="addToGroup"/>
            </app-row>
            <app-row>
                <app-tag
                        v-for="group in user.memberships"
                        action="times solid"
                        @action="removeFromGroup(group)">
                    {{group}}
                </app-tag>
            </app-row>
        </template>
        <template #actions>
            <button
                    type="button"
                    class="mdl-button"
                    @click="submit">
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
import groupService from '@/service/group'

const DEFAULT_USER = {
    login: "",
    firstName: "",
    lastName: "",
    memberships: []
}

export default {
    name: "DialogUser",
    data() {
        return {
            user: DEFAULT_USER
        }
    },
    computed: {
        title() {
            return this.user.id ? "Edit User" : "Add User"
        }
    },
    methods: {
        show(user) {
            if (user) this.user = {...user}
            this.$refs.dialogBox.show()
        },
        hide() {
            this.user = DEFAULT_USER
            this.$refs.dialogBox.hide()
        },
        submit() {
            if (this.user.id) {
                this.update()
            } else {
                this.create()
            }
        },
        create() {
            service.create({
                password: "p@55w0Rd",
                ...this.user
            }).then(user => {
                this.$emit('create', user)
                this.$bus$emit('success', "User created successfully.")
            })
            this.hide()
        },
        update() {
            service.update(this.user).then(user => {
                this.$emit('update', user)
                this.$bus$emit('success', "User updated successfully.")
            })
            this.hide()
        },
        async addToGroup(name) {
            let result = await groupService.list({ name })
            if (result.length) {
                this.user.memberships.push(name)
            } else {
                this.$bus$emit('error', "Group name doesn't exist.")
            }
        },
        removeFromGroup(name) {
            [name, ...this.user.memberships] = this.user.memberships
        }
    }
}
</script>