<template>
    <app-page layout="standard">
        <app-tab-container>
            <app-tab-item title="Users" id="tab-users" selected>
                <app-row align="center" gutter>
                    <app-table selectable shadow>
                        <thead>
                            <tr>
                                <th>Username</th>
                                <th>First Name</th>
                                <th>Last Name</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="item in users" :key="item.login">
                                <td>{{item.login}}</td>
                                <td>{{item.firstName}}</td>
                                <td>{{item.lastName}}</td>
                            </tr>
                        </tbody>
                    </app-table>
                </app-row>
            </app-tab-item>
            <app-row align="right" gutter>
                <app-button @click="$_openAddSubjectDialog" floating>
                    <app-icon symbol="plus" solid />
                </app-button>
            </app-row>
            <dialog-add-subject
                    ref="addUserDialog"
                    @change="$_load"/>
            <app-tab-item title="Groups" id="tab-groups">

            </app-tab-item>
        </app-tab-container>
    </app-page>
</template>

<script>
import AppTabContainer from "@/components/widgets/app-tab-container";
import AppTabItem from "@/components/widgets/app-tab-item";
import service from "@/service/user";
import DialogAddSubject from "./dialog-add-subject";
import AppPage from "../layouts/app-page";
import AppRow from "../widgets/app-row";
import AppButton from "../widgets/app-button";
import AppIcon from "../widgets/app-icon";
import AppTable from "../widgets/app-table";
import AppCell from "../widgets/app-cell";

export default {
    name: 'PageUsers',
    data() {
        return {
            users: []
        }
    },
    components: {
        AppCell,
        AppTable,
        AppIcon,
        AppButton,
        AppRow,
        AppPage,
        DialogAddSubject,
        AppTabItem,
        AppTabContainer
    },
    mounted() {
        this.$_load()
    },
    methods: {
        async $_load() {
            this.users = await service.list()
        },
        $_openAddSubjectDialog() {
            this.$refs.addUserDialog.show()
        }
    }
}
</script>

<style>

.mdl-tabs__tab {
    width: 100%;
}

</style>