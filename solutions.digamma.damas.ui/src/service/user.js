import http from '@/service/http'
import store from '@/store'

const BASE_URL = "/users"

class UserService {

    async load() {
        if (!store.state.user.valid && store.state.auth.username) {
            let user = {
                username: store.state.auth.username
            }
            try {
                user = await this.retrieve(user.username)
                user.fetched = true
            } catch (e) {
                if (e.statusCode !== 404) throw e
            }
            await store.dispatch("user/update", {...user, valid: true})
        }
    }

    async retrieve(username) {
        return (await http.get(`${BASE_URL}/${username}`)).data
    }
}

export default new UserService()