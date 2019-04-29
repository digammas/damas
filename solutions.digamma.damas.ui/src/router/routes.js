const routes = [
    {
        path: '/',
        redirect: "/content"
    }, {
        name: "content",
        path: "/content/:id?",
        meta: {
            title: "Content"
        },
        component: () => import("@/components/folder-content.vue")
    }, {
        name: "document",
        path: "/document/:id",
        meta: {
            title: "Document"
        },
        component: () => import("@/components/document-content.vue")
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