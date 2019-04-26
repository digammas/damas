import Vue from 'vue'
import Router from 'vue-router'
import routes from './routes'

Vue.use(Router)

const router = new Router({
    mode: 'history',
    base: process.env.BASE_URL,
    routes: routes
})

router.beforeEach((to, from, next) => {
    const titled = to.matched.slice().reverse().find(r => r.meta && r.meta.title)

    const store = router.app.$options.store;

    if (titled) {
        document.title = titled.meta.title
        store.dispatch("common/update", {title: titled.meta.title})
    }

    if (!to.matched.every(record => record.meta.public) && !store.state.auth.token) {
        next({name: 'login', query: { redirect: to.fullPath }})
    } else {
        next()
    }
})

export default router