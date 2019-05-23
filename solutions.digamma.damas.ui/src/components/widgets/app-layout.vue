<template>
    <div class="mdl-layout mdl-js-layout mdl-layout--fixed-header" ref="layout">
        <header class="mdl-layout__header">
            <div class="mdl-layout__drawer-button">
                <app-icon symbol="bars" solid size="small" />
            </div>
            <div class="mdl-layout__header-row">
                <span class="mdl-layout-title">{{title}}</span>
                <div class="mdl-layout-spacer"></div>
                <slot name="header"></slot>
            </div>
        </header>
        <div class="mdl-layout__drawer" v-if="hasSidebar" ref="sidebar">
            <span class="mdl-layout-title">{{shortTitle || title}}</span>
            <slot name="sidebar"></slot>
        </div>
        <main class="mdl-layout__content">
            <slot></slot>
        </main>
    </div>
</template>

<script>
export default {
    name: "AppLayout",
    props: {
        title: {
            type: String,
            required: true
        },
        shortTitle: String
    },
    computed: {
        hasSidebar() {
            return this.$slots.sidebar
        }
    },
    mounted() {
        window.componentHandler.upgradeElement(this.$refs.layout)
    },
    watch: {
        '$route' () {
            if (this.hasSidebar && this.$refs.sidebar) {
                if (this.$refs.sidebar.classList.contains("is-visible")) {
                    this.$refs.layout.MaterialLayout.toggleDrawer()
                }
            }
        }
    }
}
</script>

<style scoped>

</style>