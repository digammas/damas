import http from '@/service/http'
import store from '@/store'

class UserService {


    load() {
        if (!store.state.user.valid) {
            this.retrieve(store.state.auth.username)
        }
    }

    retrieve(username) {
        return new Promise((resolve, reject) => {
            http.get(`/users/${username}`).then(response => {
                store.dispatch("user/update", Object.assign({
                    valid: true
                }, response.data)).then(() => resolve(response.data))
            }).catch(reason => {
                if (reason.statusCode === 404) {
                    store.dispatch("user/clear").then(() => resolve(null))
                } else {
                    reject(reason)
                }
            })
        })
    }
}

export default new UserService()