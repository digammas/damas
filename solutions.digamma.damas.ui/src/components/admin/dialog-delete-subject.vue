<template>
    <app-dialog
            ref="dialogBox"
            :title="`Delete ${subjectType}`">
        <template>
                Are you sure you want to permanently delete {{subjectType}} {{subjectName}}?
        </template>
        <template #actions>
            <button
                    type="button"
                    class="mdl-button"
                    @click="remove">
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
import userService from '@/service/user'
import groupService from '@/service/user'

export default {
    name: "DialogDeleteSubject",
    data() {
        return {
            subject: null
        }
    },
    computed: {
        subjectType() {
            return !this.subject || this.subject.login ? 'user' : 'group'
        },
        subjectName() {
            return this.subject && (this.subject.login ? this.subject.login : this.subject.name)
        }
    },
    methods: {
        setSubject(value) {
            this.subject = value
        },
        show(subject) {
            if (subject) {
                this.setSubject(subject)
            }
            this.$refs.dialogBox.show()
        },
        hide() {
            this.$refs.dialogBox.hide()
        },
        remove() {
            let service = this.subject.login ? userService : groupService
            service.remove(this.subject.id)
            this.$emit('change')
            this.hide()
        }
    }
}
</script>