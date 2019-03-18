const defaults = {
    token: null,
    username: null
}

export default {
    namespaced: true,

    state: Object.assign({}, defaults),

    mutations: {
        update(state, data) {
            state = Object.assign(state, defaults, data)
        },
        clear(state) {
            state = Object.assign(state, defaults)
        }
    },

    actions: {
        clear(context) {
            context.commit('clear')
        },
        update(context, data) {
            context.commit('update', data)
        }
    }
}