<template>
    <div class="mdl-tabs mdl-js-tabs mdl-js-ripple-effect" ref="container">
        <div class="mdl-tabs__tab-bar" ref="bar">
            <a v-for="item of items" :href="'#'.concat(item.id)" class="mdl-tabs__tab" :key="item.id">{{item.title}}</a>
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
        componentHandler.upgradeElement(this.$refs.container)
    },
    methods: {
        addTabItem(id, title) {
            this.items.push({ id, title })
        }
    }
}
</script>
