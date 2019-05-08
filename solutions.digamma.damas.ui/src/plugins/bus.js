export default {
    install(Vue) {
        Vue.prototype.$bus = new Vue()
        Vue.prototype.$bus$emit = function(event, ...args) {
            this.$bus.$emit(event, ...args)
        }
        Vue.prototype.$bus$on = function(event, callback) {
            this.$bus.$on(event, callback)
            this.$once('hook:beforeDestroy', () => {
                this.$bus.$off(event, callback)
            })
        }
    }
}