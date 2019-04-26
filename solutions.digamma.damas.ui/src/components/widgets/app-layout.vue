<template>
    <div class="mdl-layout mdl-js-layout mdl-layout--fixed-header" ref="layout">
        <header class="mdl-layout__header">
            <div class="mdl-layout__drawer-button">
                <app-icon symbol="bars" solid size="small" />
            </div>
            <div class="mdl-layout__header-row">
                <span class="mdl-layout-title">{{title}}</span>
                <div class="mdl-layout-spacer"></div>
                <app-navigation>
                    <slot name="links"></slot>
                </app-navigation>
            </div>
        </header>
        <div class="mdl-layout__drawer">
            <span class="mdl-layout-title">{{shortTitle || title}}</span>
            <app-navigation>
                <slot name="navigation"></slot>
            </app-navigation>
        </div>
        <main class="mdl-layout__content">
            <slot></slot>
        </main>
    </div>
</template>

<script>
import AppIcon from "./app-icon";
import AppNavigation from "./app-navigation";

export default {
    name: "AppLayout",
    components: {AppNavigation, AppIcon},
    props: {
        title: String,
        shortTitle: String
    },
    mounted() {
        componentHandler.upgradeElement(this.$refs.layout)
    }
}
</script>

<style scoped>

</style>