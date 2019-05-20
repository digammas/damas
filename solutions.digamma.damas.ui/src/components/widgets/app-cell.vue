<template>
    <component class="mdl-cell" :class="classes" :is="tag">
        <div :id="hintedId">
            <slot></slot>
        </div>
        <div
                ref="hint"
                v-if="hint"
                class="mdl-tooltip mdl-tooltip--large hint-box"
                :for="hintedId">
            {{hint}}
        </div>
    </component>
</template>

<script>
export default {
    name: "AppCell",
    props: {
        span: {
            type: Number,
            validator: v => Array.from({length: 12}, (_, i) => i + 1).includes(v),
            default: 4
        },
        desktopSpan: {
            type: Number,
            validator: v => Array.from({length: 12}, (_, i) => i + 1).includes(v)
        },
        tabletSpan: {
            type: Number,
            validator: v => Array.from({length: 8}, (_, i) => i + 1).includes(v)
        },
        mobileSpan: {
            type: Number,
            validator: v => Array.from({length: 6}, (_, i) => i + 1).includes(v)
        },
        tag: {
            type: String,
            default: "div"
        },
        align: {
            type: String,
            validator(value) {
                return ["middle", "top", "bottom"].includes(value)
            }
        },
        hint: String
    },
    computed: {
        classes() {
            return {
                [`mdl-cell--${this.align}`]: this.align,
                [`mdl-cell--${this.span}-col`]: this.span,
                [`mdl-cell--${this.desktopSpan}-col`]: this.desktopSpan,
                [`mdl-cell--${this.tabletSpan}-col`]: this.tabletSpan,
                [`mdl-cell--${this.mobileSpan}-col`]: this.mobileSpan
            }
        },
        hintedId() {
            return this.$utils.randomId("inner-cell-")
        }
    },
    mounted() {
        if (this.hint) {
            window.componentHandler.upgradeElement(this.$refs.hint)
        }
    }
}

</script>

<style scoped>

.hint-box {
    max-width: inherit;
}

</style>