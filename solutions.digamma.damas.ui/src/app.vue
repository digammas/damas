<template>
    <component id="app" :is="layout">
        <router-view :layout.sync="layout" />
    </component>
</template>
<script>

export default {
    name: "App",
    data() {
        return {
            layout: "div"
        }
    }
}
</script>