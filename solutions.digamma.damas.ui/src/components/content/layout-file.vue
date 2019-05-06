<template>
    <div v-if="file">
        <app-box shadow>
            <path-breadcrumb :path="file && file.path"/>
            <app-spacer />
            <app-button
                    toolbar
                    flat
                    @click="goToParent">
                <app-icon symbol="arrow-up" solid size="small"/>
            </app-button>
            <app-more-list>
                <slot name="options"></slot>
            </app-more-list>
        </app-box>
        <slot></slot>
    </div>
</template>

<script>
import AppPage from "@/components/layouts/app-page";
import AppBox from "@/components/widgets/app-box";
import PathBreadcrumb from "./tag-breadcrumb";
import AppSpacer from "@/components/widgets/app-spacer";
import AppButton from "@/components/widgets/app-button";
import AppIcon from "@/components/widgets/app-icon";
import AppMoreList from "@/components/widgets/app-more-list";

export default {
    name: "LayoutFile",
    components: {AppMoreList, AppIcon, AppButton, AppSpacer, PathBreadcrumb, AppBox, AppPage},
    props: {
        file: Object
    },
    methods: {
        goToParent() {
            this.file && this.file.parentId && this.$router.push({name: 'content', params: {id: this.file.parentId}})
        }
    }
}
</script>