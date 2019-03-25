const defaults = {
    currentFolderId: null,
    currentFolderPath: null
}

export default {
    namespaced: true,

    state: Object.assign({}, defaults),

    mutations: {
        update(state, data) {
            Object.assign(state, defaults, data)
        },
        clear(state) {
            Object.assign(state, defaults)
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