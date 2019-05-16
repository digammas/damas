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
            validator: validate.bind(12),
            default: 4
        },
        desktopSpan: {
            type: Number,
            validator: validate.bind(12)
        },
        tabletSpan: {
            type: Number,
            validator: validate.bind(8)
        },
        mobileSpan: {
            type: Number,
            validator: validate.bind(6)
        },
        tag: {
            type: String,
            default: "div"
        },
        hint: String
    },
    computed: {
        classes() {
            let classes = [`mdl-cell--${this.span}-col`]
            addClassForDevice(classes, this.desktopSpan, "desktop")
            addClassForDevice(classes, this.tabletSpan, "tablet")
            addClassForDevice(classes, this.mobileSpan, "mobile")
            return classes
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

function addClassForDevice(list, span, device) {
    if (span) {
        list.push(`mdl-cell--${span}-col-${device}`)
    }
}

function validate(value) {
    return value > 0 && value <= this
}
</script>

<style scoped>

.hint-box {
    max-width: inherit;
}

</style>