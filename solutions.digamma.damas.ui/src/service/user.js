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

    async list(offset) {
        return (await http.get(`${BASE_URL}`, { offset })).data.objects
    }

    async create(user) {
        return (await http.post(`${BASE_URL}`, user)).data
    }

    async remove(id) {
        return (await http.delete(`${BASE_URL}/${id}`)).data
    }

    async update(user) {
        return (await http.put(`${BASE_URL}/${user.id}`, user)).data
    }
}

export default new UserService()