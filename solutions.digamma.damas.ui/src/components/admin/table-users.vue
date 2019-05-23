<template>
    <div>
        <app-table selectable shadow>
            <thead>
            <tr>
                <th>Username</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="item in users" :key="item.login">
                <td>{{item.login}}</td>
                <td>{{item.firstName}}</td>
                <td>{{item.lastName}}</td>
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
        <dialog-user
                ref="userDialog"
                @update="$_load"
                @create="$_load"/>
        <dialog-delete-subject
                ref="deleteSubjectDialog"
                @change="$_load"/>
    </div>
</template>

<script>
import service from '@/service/user'
import DialogUser from './dialog-user'
import DialogDeleteSubject from './dialog-delete-subject';

export default {
    name: "TableUsers",
    components: {
        DialogDeleteSubject,
        DialogUser
    },
    data() {
        return {
            users: []
        }
    },
    mounted() {
        this.$_load()
    },
    methods: {
        async $_load() {
            this.users = await service.list()
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