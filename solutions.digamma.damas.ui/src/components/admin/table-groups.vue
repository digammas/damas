<template>
    <div>
        <app-table
                :items="groups"
                selectable
                shadow>
            <app-table-column
                    title="Group name"
                    field="name"/>
            <app-table-column
                    title="Group alias"
                    field="label"/>
            <template #actions="item">
                <app-row>
                    <app-cell
                            :span="4"
                            hint="delete">
                        <app-button
                                toolbar
                                flat
                                @click="$_openDeleteDialog(item)">
                            <app-icon symbol="trash" solid size="small"/>
                        </app-button>
                    </app-cell>
                    <app-cell
                            :span="4"
                            hint="edit">
                        <app-button
                                toolbar
                                flat
                                @click="$_openEditDialog(item)">
                            <app-icon symbol="pen" solid size="small"/>
                        </app-button>
                    </app-cell>
                </app-row>
        </template>
        </app-table>
        <app-row align="right" gutter>
            <app-button @click="$_openAddDialog" floating>
                <app-icon symbol="plus" solid />
            </app-button>
        </app-row>
        <dialog-group
                ref="userDialog"
                @update="$_load"
                @create="$_load"/>
        <dialog-delete-subject
                ref="deleteSubjectDialog"
                @change="$_load"/>
    </div>
</template>

<script>
import service from '@/service/group'
import DialogGroup from './dialog-group'
import DialogDeleteSubject from './dialog-delete-subject'
import AppTableColumn from '../widgets/app-table-column'

export default {
    name: "TableGroups",
    components: {
        AppTableColumn,
        DialogDeleteSubject,
        DialogGroup
    },
    data() {
        return {
            groups: []
        }
    },
    mounted() {
        this.$_load()
    },
    methods: {
        async $_load() {
            this.groups = await service.list()
        },
        $_openAddDialog() {
            this.$refs.userDialog.show()
        },
        $_openDeleteDialog(item) {
            this.$refs.deleteSubjectDialog.show(item)
        },
        $_openEditDialog(item) {
            this.$refs.userDialog.show(item)
        }
    }
}
</script>