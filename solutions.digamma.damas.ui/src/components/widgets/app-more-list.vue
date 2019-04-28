<template>
    <div>
        <button
                class="mdl-button mdl-js-button mdl-js-ripple-effect"
                :class="classes"
                :id="id"
                ref="button">
            <app-icon symbol="ellipsis-h" solid size="small" />
        </button>
        <ul class="mdl-menu mdl-js-menu mdl-js-ripple-effect mdl-menu--bottom-right" :for="id" ref="list">
            <slot></slot>
        </ul>
    </div>
</template>

<script>
import AppIcon from "./app-icon";

export default {
    name: "AppMoreList",
    components: {AppIcon},
    props: {
        id: {
            type: String,
            default: () => `more-list-${Math.random().toString(36).substr(2, 10)}`
        },
        symbol: String,
        solid: Boolean,
        floating: Boolean
    },
    computed: {
        classes() {
            return {
                'mdl-button--icon': !this.floating,
                'mdl-button--fab': this.floating

            }
        }
    },
    mounted() {
        for (let el of this.$refs.list.querySelectorAll("a")) {
            el.classList.add("mdl-menu__item")
        }
        componentHandler.upgradeElement(this.$refs.button)
        componentHandler.upgradeElement(this.$refs.list)
    }
}
</script>