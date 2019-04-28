<template>
    <div class="mdl-tabs__panel" :class="{'is-active' : selected}" :id="id" ref="panel">
        <slot></slot>
    </div>
</template>

<script>
export default {
    name: "AppTabItem",
    inject: ['addTabItem'],
    props: {
        id: {
            type: String,
            default: () => `tab-item-${Math.random().toString(36).substr(2, 10)}`
        },
        title: {
            type: String,
            required: true
        },
        selected: Boolean
    },
    created() {
        this.addTabItem(this.id, this.title)
    }
}
</script>