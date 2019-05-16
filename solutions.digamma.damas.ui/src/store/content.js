import {CLEAR, UPDATE} from './common'

const defaults = {
    clipboard: null
}

export default {
    namespaced: true,

    state: Object.assign({}, defaults),

    mutations: {
        [UPDATE] (state, data) {
            Object.assign(state, defaults, data)
        },
        [CLEAR] (state) {
            Object.assign(state, defaults)
        }
    },

    actions: {
        clear(context) {
            context.commit(CLEAR)
        },
        update(context, data) {
            context.commit(UPDATE, data)
        }
    }
}