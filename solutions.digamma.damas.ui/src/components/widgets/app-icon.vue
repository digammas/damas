<template>
    <i :class="classes"></i>
</template>

<script>
let sizes = {
    small: "fa-sm",
    normal: "fa-lg",
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
        /**
         * A string of the following form:
         * icon-symbol [solid|regular [inactive]]
         * where solid, regular and inactive are keywords.
         */
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
                this.isSolid() ? "fas" : "far",
                `fa-${this.getSymbol()}`,
                sizes[this.size],
                themes[this.theme],
                this.isInactive() ? "inactive" : ""
            ]

        }
    },
    methods: {
        getSymbol() {
            return this.symbol && this.symbol.split(" ")[0]
        },
        isSolid() {
            return this.solid || (this.symbol && this.symbol.split(" ")[1] === "solid")
        },
        isInactive() {
            return this.inactive || (this.symbol && this.symbol.split(" ")[2] == "inactive")
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