<template>
    <div>
        <app-table selectable shadow>
            <thead>
            <tr>
                <th>Group name</th>
                <th>Group alias</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="item in groups" :key="item.name">
                <td>{{item.name}}</td>
                <td>{{item.label}}</td>
                <td>
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
                </td>
            </tr>
            </tbody>
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

export default {
    name: "TableGroups",
    components: {
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