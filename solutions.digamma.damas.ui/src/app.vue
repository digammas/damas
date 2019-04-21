<template>
    <component id="app" :is="layout">
        <router-view :layout.sync="layout" />
    </component>
</template>
<script>
import LayoutDefault from "./components/layouts/layout-default";
import LayoutEmpty from "./components/layouts/layout-empty";

export default {
    name: "App",
    components: {LayoutEmpty, LayoutDefault},
    data() {
        return {
            layout: "div"
        }
    }
}
</script>