import Vue from "vue"

export default {
    name: 'AppLayout',
    props: {
        layout: {
            type: "standard" | "empty",
            required: true
        }
    },
    created() {
        let component = components[this.layout]
        if (!Vue.options.components[component.name]) {
            Vue.component(
                component.name,
                component,
            );
        }
        this.$parent.$emit('update:layout', component);
    },
    render(h) {
        return this.$slots.default ? this.$slots.default[0] : h();
    },
}

const components = {
    "standard": () => import("./layout-standard"),
    "empty": () => import("./layout-empty")
}