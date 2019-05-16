import {CLEAR, UPDATE} from './common'

const defaults = {
    firstName: null,
    lastName: null,
    emailAddress: null,
    valid: false
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