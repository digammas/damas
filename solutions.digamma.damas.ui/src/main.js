import Vue from 'vue'
import App from '@/app.vue'
import router from '@/router'
import store from '@/store'
import EventBus from '@/plugins/bus'

import 'material-design-lite/material.min.css'
import 'material-design-lite/material'
import '@fortawesome/fontawesome-free/js/all'

Vue.config.productionTip = false
Vue.config.devtools = true

Vue.use(EventBus)

const widgets = require.context(
    "./components", true, /app-[a-z-]*\.(vue|js)$/
)

widgets.keys().forEach(filename => {
    const component = widgets(filename)
    const config = component.default || component

    Vue.component(config.name, config)
})

new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')
