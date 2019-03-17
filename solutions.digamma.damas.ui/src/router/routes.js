const routes = [{
        path: '/',
        name: 'home',
        component: () => import("@/views/home.vue"),
        meta: {
            title: "Home"
        }
    }, {
        path: '/about',
        name: 'about',
        component: () => import('@/views/about.vue'),
        meta: {
            title: "About"
        }
    }, {
        path: '/login',
        name: 'login',
        component: () => import('@/views/login.vue'),
        meta: {
            title: "Login",
            public: true
        }
    }
]

export default routes