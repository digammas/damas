import http from '@/service/http'
import store from '@/store'

class ContentService {

    async load() {
        let currentFolderId = (await this.retrieveAt("/")).id
        store.dispatch("content/update", { currentFolderId })
    }

    async retrieve(id, depth = null, full = false) {
        return (await http.get(`/folders/${id}`, { full, depth })).data
    }

    async retrieveAt(path) {
        return (await http.get(`/folders/at${path}`)).data
    }

    async create(parentId, name) {
        return (await http.post("/folders", { parentId, name })).data
    }

    async rename(id, name) {
        return (await http.put(`/folders/${id}`, { name })).data
    }

    async remove(id, name) {
        return (await http.delete(`/folders/${id}`, { name })).data
    }
}

export default new ContentService()