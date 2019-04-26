<template>
    <div class="mdl-layout mdl-js-layout mdl-layout--fixed-header" ref="layout">
        <header class="mdl-layout__header">
            <div class="mdl-layout__drawer-button">
                <app-icon symbol="bars" solid size="small" />
            </div>
            <div class="mdl-layout__header-row">
                <span class="mdl-layout-title">{{title}}</span>
                <div class="mdl-layout-spacer"></div>
                <nav class="mdl-navigation mdl-layout--large-screen-only" ref="links">
                    <slot name="links"></slot>
                </nav>
            </div>
        </header>
        <div class="mdl-layout__drawer">
            <span class="mdl-layout-title">{{shortTitle || title}}</span>
            <nav class="mdl-navigation" ref="navigation">
                <slot name="navigation"></slot>
            </nav>
        </div>
        <main class="mdl-layout__content">
            <slot></slot>
        </main>
    </div>
</template>

<script>
import AppIcon from "./app-icon";

export default {
    name: "AppLayout",
    components: {AppIcon},
    props: {
        title: String,
        shortTitle: String
    },
    mounted() {
        let addNavLinkClass = item => {
            for (let el of item.querySelectorAll("a")) {
                el.classList.add("mdl-navigation__link")
            }
        }
        addNavLinkClass(this.$refs.links)
        addNavLinkClass(this.$refs.navigation)
        componentHandler.upgradeElement(this.$refs.layout)
    }
}
</script>

<style scoped>

</style>