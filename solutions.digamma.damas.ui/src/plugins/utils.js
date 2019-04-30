class Utils {
    randomId(prefix) {
        return (prefix || "").concat(Math.random().toString(36).substr(2, 10))
    }
}

export default {
    install(Vue, options) {
        Vue.prototype.$utils = new Utils()
    }
}