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
        component: () => import("@/components/content/page-folder.vue")
    }, {
        name: "document",
        path: "/document/:id",
        meta: {
            title: "Document"
        },
        component: () => import("@/components/content/page-document.vue")
    }, {
        path: '/login',
        name: 'login',
        component: () => import('@/components/session/page-login.vue'),
        meta: {
            title: "Login",
            public: true
        }
    }
]

export default routes