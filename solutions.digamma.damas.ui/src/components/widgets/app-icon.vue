<template>
    <i :class="classes"></i>
</template>

<script>
let sizes = {
    small: "fa-lg",
    normal: "fa-3x",
    big: "fa-4x",
    huge: "fa-5x"
}

let themes = {
    dark: "dark",
    light: "light"
}

export default {
    name: "AppIcon",
    props: {
        symbol: {
            type: String,
            required: true
        },
        size: {
            type: String,
            default: "normal",
            validator: v => v in sizes
        },
        theme: {
            type: String,
            default: "dark",
            validator: v => v in themes
        },
        inactive: Boolean,
        solid: Boolean
    },
    computed: {
        classes() {
            return [
                this.solid ? "fas" : "far",
                `fa-${this.symbol}`,
                sizes[this.size],
                themes[this.theme],
                this.inactive ? "inactive" : ""
            ]

        }
    }
}
</script>

<style scoped>
.dark { color: rgba(0, 0, 0, 0.54); }
.dark.inactive { color: rgba(0, 0, 0, 0.26); }

.light { color: rgba(255, 255, 255, 1); }
.light.inactive { color: rgba(255, 255, 255, 0.3); }
</style>