import axios from 'axios'
import store from '@/store'

export const PORT = 8080
export const BASE_PATH = "rest/"

class HttpClient {

    get(path, params = {}, responseType = 'json') {
        return http({
            method: "GET",
            url: path,
            baseURL: baseUrl(),
            params,
            responseType,
            headers: headers()
        })
    }

    post(path, data) {
        return http({
            method: "post",
            url: path,
            baseURL: baseUrl(),
            responseType: 'json',
            headers: headers(),
            data: data
        })
    }

    put(path, data) {
        return http({
            method: "put",
            url: path,
            baseURL: baseUrl(),
            responseType: 'json',
            headers: headers(),
            data: data
        })
    }

    delete(path) {
        return http({
            method: "delete",
            url: path,
            baseURL: baseUrl(),
            responseType: 'json',
            headers: headers()
        })
    }
}

function http() {
    return wrapPromise(axios(...arguments))
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
    let headers = {}
    if (token) {
        headers["Authorization"] = `bearer ${token}`
    }
    return headers
}

function baseUrl() {
    return `${window.location.protocol}//${window.location.hostname}:${PORT}/${BASE_PATH}`
}

class HttpError extends Error {

    constructor(message, statusCode) {
        super(message)
        this.statusCode = statusCode
    }
}

export default new HttpClient()