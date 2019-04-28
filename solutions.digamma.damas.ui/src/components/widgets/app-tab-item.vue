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
            default() {
                this.$utils.randomId("tab-item-")
            }
        },
        title: {
            type: String,
            required: true
        },
        selected: Boolean
    },
    created() {
        this.addTabItem(this.id, this.title)
    },
    methods: {
        isSelected() {
            return this.$refs.panel.classList.contains("is-active")
        }
    }
}
</script>