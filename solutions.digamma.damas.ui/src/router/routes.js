const routes = [{
        path: '/',
        name: 'home',
        component: () => import("@/components/home.vue"),
        meta: {
            title: "Home"
        }
    }, {
        path: '/about',
        name: 'about',
        component: () => import('@/components/about.vue'),
        meta: {
            title: "About"
        }
    }, {
        path: '/login',
        name: 'login',
        component: () => import('@/components/login.vue'),
        meta: {
            title: "Login",
            public: true
        }
    }
]

export default routes