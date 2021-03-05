<template>
    <component :is="outerTag">
        <component :id="contentId" :is="innerTag">
            <slot></slot>
        </component>
        <div
                ref="hint"
                class="mdl-tooltip hint-box"
                :for="contentId">
            {{text}}
        </div>
    </component>
</template>

<script>
export default {
    name: "AppHint",
    props: {
        outerTag: {
            type: String,
            default: "span"
        },
        innerTag: {
            type: String,
            default: "span"
        },
        text: String
    },
    computed: {
        contentId() { return `hint-content-${this._uid}` },
    },
    mounted() {
        window.componentHandler.upgradeElement(this.$refs.hint)
    }
}
</script>

<style scoped>

.hint-box {
    max-width: inherit;
}

.mdl-tooltip {
    font-size: 14px;
    padding: 6px;
}

</style>
