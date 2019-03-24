const routes = [{
        path: '/',
        component: () => import("@/components/user.vue"),
        meta: {
            title: "Home"
        },
        children: [{
                path: "",
                name: "content",
                component: () => import("@/components/content.vue")
            },
        ]
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