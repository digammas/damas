import Vue from 'vue'
import App from '@/app.vue'
import router from '@/router'
import store from '@/store'
import Utils from "@/plugins/utils"
import EventBus from "@/plugins/bus"

import 'material-design-lite/material.min.css'
import 'material-design-lite/material'
import '@fortawesome/fontawesome-free/js/all'

Vue.config.productionTip = false
Vue.config.devtools = true

Vue.use(Utils)
Vue.use(EventBus)

new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')
