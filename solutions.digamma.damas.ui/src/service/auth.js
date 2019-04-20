import http from '@/service/http'
import store from '@/store'

class AuthService {

    authenticate(username, password) {
        return new Promise((resolve, reject) => {
            http.post("/login", {
                username: username,
                password: password }
            ).then(response => {
                let token = response.data.secret
                store.dispatch("auth/update", {
                    username: username,
                    token: token
                }).then(() => resolve(token))
            }).catch(reason => {
                if (reason.statusCode === 401) {
                    store.dispatch("auth/clear")
                    resolve(null)
                } else {
                    reject(reason)
                }
            })
        })
    }
}

export default new AuthService()