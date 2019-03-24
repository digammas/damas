import axios from 'axios'
import store from '@/store'

export const BASE_URL = "http://localhost:8080/dms/rest/"

class HttpClient {

    get(path, params = {}) {
        return axios({
            method: "GET",
            url: path,
            baseURL: BASE_URL,
            params: params,
            headers: this.headers()
        })
    }

    post(path, data) {
        return axios({
            method: "post",
            url: path,
            baseURL: BASE_URL,
            responseType: 'json',
            headers: this.headers(),
            data: data
        })
    }

    headers() {
        let token = store.state.auth.token
        let headers = {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        }
        if (token) {
            Object.assign(headers, {
                "Authorization": `bearer ${token}`
            })
        }
        return headers
    }
}

export default new HttpClient()