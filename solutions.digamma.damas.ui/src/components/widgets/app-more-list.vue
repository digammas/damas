<template>
    <div>
        <app-button
                :id="id"
                :toolbar="!floating"
                flat>
            <app-icon symbol="ellipsis-h" solid size="small" />
        </app-button>
        <ul class="mdl-menu mdl-js-menu mdl-js-ripple-effect mdl-menu--bottom-right" :for="id" ref="list">
            <slot></slot>
        </ul>
    </div>
</template>

<script>
export default {
    name: "AppMoreList",
    props: {
        id: {
            type: String,
            default() { return `more-list-${this._uid}` },
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
        window.componentHandler.upgradeElement(this.$refs.list)
    }
}
</script>
