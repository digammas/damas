import Vue from 'vue'
import App from '@/app.vue'
import router from '@/router'
import store from '@/store'

import 'material-design-lite/material.min.css'
import 'material-design-lite/material.min'
import 'material-design-icons/iconfont/material-icons.css'

Vue.config.productionTip = false
Vue.config.devtools = true

new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')
