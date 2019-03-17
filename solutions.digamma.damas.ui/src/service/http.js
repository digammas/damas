import axios from 'axios'

export const BASE_URL = "http://localhost:8080/dms/rest/"

class HttpClient {

    get(path) {
        return axios({
            method: "GET",
            url: path,
            baseURL: BASE_URL
        })
    }

    post(path, data) {
        return axios({
            method: "post",
            url: path,
            baseURL: BASE_URL,
            responseType: 'json',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
            },
            data: data
        })
    }
}

export default new HttpClient()