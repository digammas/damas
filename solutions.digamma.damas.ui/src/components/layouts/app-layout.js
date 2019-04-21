export default {
    name: 'AppLayout',
    props: {
        component: {
            type: String,
            required: true
        }
    },
    created() {
        this.$parent.$emit('update:layout', this.component);
    },
    render(h) {
        return this.$slots.default ? this.$slots.default[0] : h();
    },
};