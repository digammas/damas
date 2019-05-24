<template>
    <app-page layout="standard">
        <app-row align="center" gutter>
            <app-cell :span="10">
                <app-tab-container>
                    <app-tab-item title="Users" id="tab-users" selected>
                        <table-users />
                    </app-tab-item>
                    <app-tab-item title="Groups" id="tab-groups">
                        <table-groups />
                    </app-tab-item>
                </app-tab-container>
            </app-cell>
        </app-row>
    </app-page>
</template>

<script>
import TableUsers from './table-users'
import TableGroups from './table-groups'

export default {
    name: "PageUserManagement",
    components: {TableGroups, TableUsers}
}
</script>