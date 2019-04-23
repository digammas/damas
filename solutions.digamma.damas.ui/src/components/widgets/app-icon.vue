<template>
    <i class="material-icons" :class="classes">{{symbol}}</i>
</template>

<script>
let sizes = {
    small: "md-18",
    normal: "md-24",
    big: "md-36",
    huge: "md-48"
}

let themes = {
    dark: "md-dark",
    light: "md-light"
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
        inactive: {
            type: Boolean,
            default: false
        }
    },
    computed: {
        classes() {
            return [
                sizes[this.size],
                themes[this.theme],
                this.inactive ? "md-inactive" : ""
            ]

        }
    }
}
</script>

<style scoped>
    .material-icons.md-18 { font-size: 18px; }
    .material-icons.md-24 { font-size: 24px; }
    .material-icons.md-36 { font-size: 36px; }
    .material-icons.md-48 { font-size: 48px; }

    .material-icons.md-dark { color: rgba(0, 0, 0, 0.54); }
    .material-icons.md-dark.md-inactive { color: rgba(0, 0, 0, 0.26); }

    .material-icons.md-light { color: rgba(255, 255, 255, 1); }
    .material-icons.md-light.md-inactive { color: rgba(255, 255, 255, 0.3); }
</style>