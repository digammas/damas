import http from '@/service/http'
import store from '@/store'

class ContentService {

    load() {
        this.retrieveAt("/").then((data) => {
            store.dispatch("content/update", {
                currentFolderId: data.id
            })
        })
    }

    retrieve(id, depth = null, full = false) {
        return new Promise((resolve, reject) => {
            http.get(`/folders/${id}`, {
                full: full,
                depth: depth
            }).then(response => {
                resolve(response.data)
            }).catch(reason => {
                if (!reason.response) {
                    reject(new Error("Unexpected client error"))
                } else if (reason.response.data && reason.response.data.message){
                    reject(new Error(response.data.message))
                } else {
                    reject(new Error(`Unexpected server error: ${reason.response.status}.`))
                }
            })
        })
    }

    retrieveAt(path) {
        return new Promise((resolve, reject) => {
            http.get(`/folders/at${path}`).then(response => {
                resolve(response.data)
            }).catch(reason => {
                if (!reason.response) {
                    reject(new Error("Unexpected client error"))
                } else if (reason.response.data && reason.response.data.message){
                    reject(new Error(response.data.message))
                } else {
                    reject(new Error(`Unexpected server error: ${reason.response.status}.`))
                }
            })
        })
    }
}

export default new ContentService()