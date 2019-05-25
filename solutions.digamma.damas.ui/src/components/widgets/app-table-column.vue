<template>
    <th>{{title}}</th>
</template>

<script>
export default {
    name: "AppTableColumn",
    props: {
        title: String,
        field: String,
        expression: Function
    },
    inject: ['addColumn'],
    mounted() {
        this.addColumn({
            title: this.title,
            field: this.field,
            expression: this.expression
        });
    }
}
</script>