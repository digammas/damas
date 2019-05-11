import http from '@/service/http'

const BASE_URL = "/folders"

class ContentService {

    async retrieve(id, depth = null, full = false) {
        return (await http.get(`${BASE_URL}/${id}`, { full, depth })).data
    }

    async retrieveAt(path) {
        return (await http.get(`${BASE_URL}/at${path}`)).data
    }

    async create(parentId, name) {
        return (await http.post(`${BASE_URL}`, { parentId, name })).data
    }

    async rename(id, name) {
        return (await http.put(`${BASE_URL}/${id}`, { name })).data
    }

    async remove(id, name) {
        return (await http.delete(`${BASE_URL}/${id}`, { name })).data
    }

    async move(id, parentId) {
        return (await http.put(`${BASE_URL}/${id}`, { parentId })).data
    }

    async copy(id, parentId) {
        return (await http.post(`${BASE_URL}/${id}/copy`, { parentId })).data
    }
}

export default new ContentService()