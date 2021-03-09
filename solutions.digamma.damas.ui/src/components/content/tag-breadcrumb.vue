<template>
    <app-tag
            icon="folder-open">
        <app-icon symbol="chevron-right" solid />
        <span @click="firePath(0)" class="crumb">root</span>
        <span v-for="(element, index) in pathElements" :key="index">
            <app-icon symbol="chevron-right" solid :key="`icon-${index}`" />
            <span @click="firePath(index + 1)" class="crumb" :key="`text-${index}`">{{element}}</span>
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
    },
    methods: {
        firePath(index) {
            this.$emit('navigate', index)
        }
    }
}
</script>

<style>
.crumb {
    padding-left: .25em;
    padding-right: .25em;
}
.crumb:hover {
    text-decoration: underline;
    cursor: pointer;
}
</style>
