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

    if (titled) {
        document.title = titled.meta.title
    }

    if (!to.matched.some(record => record.meta.public)) {
        next({path: '/login', query: { redirect: to.fullPath }})
    }
    next()
})

export default router