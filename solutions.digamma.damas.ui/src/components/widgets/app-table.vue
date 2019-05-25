<template>
    <table
            class="mdl-data-table mdl-js-data-table"
            :class="classes"
            ref="table">
        <thead>
        <tr>
            <slot></slot>
            <th v-if="hasActions">Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(item, i) in items" :key="i">
            <td v-for="(column, j) in columns" :key="j">{{$_getCell(item, column)}}</td>
            <td v-if="hasActions">
                <slot name="actions" :item="item"></slot>
            </td>
        </tr>
        </tbody>
    </table>
</template>

<script>
export default {
    name: "AppTable",
    data() {
        return {
            columns: []
        }
    },
    provide() {
        return {
            addColumn: this.addColumn
        }
    },
    props: {
        selectable: Boolean,
        shadow: Boolean,
        items: Array
    },
    computed: {
        classes() {
            return {
                'mdl-shadow--2dp': this.shadow
            }
        },
        hasActions() {
            return !!this.$slots.default
        }
    },
    mounted() {
        window.componentHandler.upgradeElement(this.$refs.table)
    },
    methods: {
        $_getCell(item, column) {
            if (column.field) {
                return item[column.field]
            } else {
                return item.expression(item)
            }
        },
        addColumn(column) {
            this.columns.push(column)
        }
    }
}
</script>

<style scoped>

.mdl-data-table {
    width: 100%;
}

.mdl-data-table th {
    text-align: inherit;
}

.mdl-data-table td {
    text-align: inherit;
}

</style>