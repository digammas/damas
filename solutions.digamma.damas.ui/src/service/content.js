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
            }).catch(reject)
        })
    }

    retrieveAt(path) {
        return new Promise((resolve, reject) => {
            http.get(`/folders/at${path}`).then(response => {
                resolve(response.data)
            }).catch(reject)
        })
    }
}

export default new ContentService()