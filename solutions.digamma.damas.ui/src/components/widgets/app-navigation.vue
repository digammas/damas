<template>
    <nav class="mdl-navigation" ref="navigation">
        <slot></slot>
    </nav>
</template>

<script>
export default {
    name: "AppNavigation",
    mounted() {
        this.update()
    },
    updated() {
        this.update()
    },
    methods: {
        update() {
            for (let el of this.$refs.navigation.querySelectorAll("a")) {
                el.classList.add("mdl-navigation__link")
            }
        }
    }
}
</script>

<style scoped>

</style>