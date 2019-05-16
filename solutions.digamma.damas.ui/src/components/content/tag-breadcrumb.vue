<template>
    <app-tag
            icon="folder-open">
        <app-icon symbol="chevron-right" solid /> root
        <span v-for="(element, index) in pathElements" :key="index">
            <app-icon symbol="chevron-right" solid /> {{element}}
        </span>
    </app-tag>
</template>

<script>
export default {
    name: "TagBreadcrumb",
    props: {
        path: String,
        link: [String, Object]
    },
    computed: {
        pathElements() {
            return this.path ? this.path.split("/").filter(Boolean) : []
        }
    }
}
</script>