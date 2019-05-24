<template>
    <app-dialog
            ref="dialogBox"
            :title="title">
        <template>
            <app-text-input
                    id="group-name"
                    label="Group name"
                    v-model="group.name"
                    :readonly="group.id" />
            <app-text-input
                    id="group-label"
                    label="Group Label"
                    v-model="group.label" />
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
import service from '@/service/group'

const DEFAULT_GROUP = {
    name: "",
    label: ""
}

export default {
    name: "DialogUser",
    data() {
        return {
            group: DEFAULT_GROUP
        }
    },
    computed: {
        title() {
            return this.group.id ? "Edit Group" : "Add Group"
        }
    },
    methods: {
        show(group = DEFAULT_GROUP) {
            this.group = {...group}
            this.$refs.dialogBox.show()
        },
        hide() {
            this.group = DEFAULT_GROUP
            this.$refs.dialogBox.hide()
        },
        submit() {
            if (this.group.id) {
                this.update()
            } else {
                this.create()
            }
        },
        create() {
            service.create({ ...this.group }).then(group => {
                this.$emit('create', group)
                this.$bus$emit('success', "Group created successfully.")
            })
            this.hide()
        },
        update() {
            service.update(this.group).then(group => {
                this.$emit('update', group)
                this.$bus$emit('success', "Group updated successfully.")
            })
            this.hide()
        }
    }
}
</script>