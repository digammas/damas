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
import AppTag from "@/components/widgets/app-tag";
import AppIcon from "@/components/widgets/app-icon";

export default {
    name: "TagBreadcrumb",
    components: {AppIcon, AppTag},
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