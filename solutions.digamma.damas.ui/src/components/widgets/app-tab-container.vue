<template>
    <div
            class="mdl-tabs mdl-js-tabs mdl-js-ripple-effect"
            ref="container">
        <div
                class="mdl-tabs__tab-bar"
                ref="bar">
            <a
                    v-for="item of items"
                    :href="`#${item.id}`"
                    class="mdl-tabs__tab"
                    :class="{'is-active': item.selected}"
                    :key="item.id">
                {{item.title}}
            </a>
        </div>
        <slot></slot>
    </div>
</template>

<script>
export default {
    name: "AppTabContainer",
    data() {
        return {
            items: []
        }
    },
    provide() {
        return {
            addTabItem: this.addTabItem
        }
    },
    updated() {
        this.$refs.container.dataset.upgraded = null
        window.componentHandler.upgradeElement(this.$refs.container)
    },
    methods: {
        addTabItem(id, title, selected) {
            this.items.push({ id, title, selected })
        }
    }
}
</script>

<style scoped>

.mdl-tabs__tab {
    width: 100%;
}

</style>
