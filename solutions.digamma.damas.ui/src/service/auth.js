import http from '@/service/http'
import store from '@/store'

class AuthService {

    async authenticate(username, password) {
        try {
            let response = await http.post("/login", {username, password})
            let token = response.data.secret
            await store.dispatch("auth/update", {username, token})
            return token
        } catch (e) {
            if (e.statusCode === 401) {
                store.dispatch("auth/clear")
                return null
            } else {
                throw e
            }
        }
    }
}

export default new AuthService()