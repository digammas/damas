import Vue from "vue"

export default {
    name: 'AppPage',
    props: {
        layout: {
            type: String,
            required: true,
            validator: [].includes.bind(["standard", "empty"])
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
        if (this.$parent.layout !== component) {
            this.$parent.$emit('update:layout', component);
        }
    },
    render(h) {
        return this.$slots.default ? this.$slots.default[0] : h();
    },
}

const components = {
    "standard": () => import("./layout-standard"),
    "empty": () => import("./layout-empty")
}
