<template>
    <component class="mdl-cell" :class="classes" :is="tag">
        <slot/>
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
        }
    },
    computed: {
        classes() {
            let classes = [`mdl-cell--${this.span}-col`]
            addClassForDevice(classes, this.desktopSpan, "desktop")
            addClassForDevice(classes, this.tabletSpan, "tablet")
            addClassForDevice(classes, this.mobileSpan, "mobile")
            console.log("CLASSES" + classes)
            return classes
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

</style>