import Vue from 'vue'
import Router from 'vue-router'
import routes from './routes'
import store from '@/store'

Vue.use(Router)

const router = new Router({
    mode: 'history',
    base: process.env.BASE_URL,
    routes: routes
})

router.beforeEach((to, from, next) => {
    const titled = to.matched.slice().reverse().find(r => r.meta && r.meta.title)

    if (titled) {
        document.title = titled.meta.title
    }

    if (!to.matched.some(record => record.meta.public) && !store.state.auth.token) {
        next({path: '/login', query: { redirect: to.fullPath }})
    }
    next()
})

export default router