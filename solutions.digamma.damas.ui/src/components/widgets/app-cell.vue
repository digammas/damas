<template>
    <component
            class="mdl-cell"
            :text="hintText"
            :outerTag="tag"
            :class="classes"
            :is="rootType">
        <slot></slot>
    </component>
</template>

<script>
import AppHint from './app-hint'

export default {
    name: "AppCell",
    components: {AppHint},
    data() {
        return {
            dynamicHint: null
        }
    },
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
        rootType() {
            return this.hintText ? 'AppHint' : this.tag
        },
        hintText() {
            return this.hint || this.dynamicHint
        }
    },
    methods: {
        setHint(value) {
            this.dynamicHint = value
        }
    }
}
</script>

<style scoped>

.hint-box {
    max-width: inherit;
}

</style>
