import axios from 'axios'
import store from '@/store'

export const BASE_URL = "http://localhost:8080/dms/rest/"

class HttpClient {

    get(path, params = {}) {
        return http({
            method: "GET",
            url: path,
            baseURL: BASE_URL,
            params: params,
            headers: headers()
        })
    }

    post(path, data) {
        return http({
            method: "post",
            url: path,
            baseURL: BASE_URL,
            responseType: 'json',
            headers: headers(),
            data: data
        })
    }
}

function http() {
    return wrapPromise(axios.apply(null, arguments))
}

function wrapPromise(promise) {
    return new Promise((resolve, reject) => {
        promise.then(resolve).catch(reason => reject(wrapError(reason)))
    })
}

function wrapError(reason) {
    if (!reason.response) {
        return new HttpError("Http client error.")
    }
    if (reason.response.data && reason.response.data.message){
        return new HttpError(reason.response.data.message, reason.response.status)
    }
    return new HttpError(`Http server error: ${reason.response.status}.`, reason.response.status)
}

function headers() {
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

class HttpError extends Error {

    constructor(message, statusCode) {
        super(message)
        this.statusCode = statusCode
    }
}

export default new HttpClient()