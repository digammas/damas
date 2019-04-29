import http from '@/service/http'
import store from '@/store'

class UserService {


    load() {
        if (!store.state.user.valid) {
            this.retrieve(store.state.auth.username)
        }
    }

    async retrieve(username) {
        try {
            let response = await http.get(`/users/${username}`)
            await store.dispatch("user/update", {...response.data, valid: true})
            return response.data
        } catch (e) {
            if (e.statusCode === 404) {
                store.dispatch("user/clear")
                return null
            } else {
                throw e
            }
        }
    }
}

export default new UserService()