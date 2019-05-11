import http from '@/service/http'

class ContentService {

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

    async move(id, parentId) {
        return (await http.put(`/folders/${id}`, { parentId })).data
    }

    async copy(id, parentId) {
        return (await http.post(`/folders/${id}/copy`, { parentId })).data
    }
}

export default new ContentService()