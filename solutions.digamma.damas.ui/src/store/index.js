import Vue from 'vue'
import Vuex from 'vuex'
import common from './common'
import auth from './auth'
import user from './user'
import content from './content'

Vue.use(Vuex)

export default new Vuex.Store({
    modules: { common, auth, user, content }
})
