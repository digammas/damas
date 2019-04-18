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
                if (!reason.response) {
                    reject(new Error("Unexpected client error"))
                } else if (reason.response.status === 401) {
                    store.dispatch("auth/clear")
                    resolve(null)
                } else if (reason.response.data && reason.response.data.message){
                    reject(new Error(reason.response.data.message))
                } else {
                    reject(new Error(`Unexpected server error: ${reason.reason.response.status}.`))
                }
            })
        })
    }
}

export default new AuthService()