const routes = [
    {
        path: '/',
        redirect: "/content"
    }, {
        component: () => import("@/components/the-user-panel.vue"),
        path: '/user',
        meta: {
            title: "Home"
        },
        children: [
            {
                path: "/content/:id?",
                name: "content",
                component: () => import("@/components/folder-content.vue")
            }
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
        component: () => import('@/components/login-form.vue'),
        meta: {
            title: "Login",
            public: true
        }
    }
]

export default routes