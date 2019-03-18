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

    const auth = router.app.$options.store.state.auth;

    if (titled) {
        document.title = titled.meta.title
    }

    if (!to.matched.every(record => record.meta.public) && !auth.token) {
        next({name: 'login', query: { redirect: to.fullPath }})
    } else {
        next()
    }
})

export default router