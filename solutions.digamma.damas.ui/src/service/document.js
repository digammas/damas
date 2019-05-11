import http from '@/service/http'

const BASE_URL = "/documents"

class DocumentService {

    async retrieve(id, full = false) {
        return (await http.get(`${BASE_URL}/${id}`, { full })).data
    }

    async create(parentId, name, mimeType) {
        return (await http.post(`${BASE_URL}`, { parentId, name, mimeType })).data
    }

    async upload(id, binary) {
        await http.put(`${BASE_URL}/${id}/upload`, binary, {}, 'blob')
    }

    async download(id) {
        return (await http.get(`${BASE_URL}/${id}/download`, {}, 'blob')).data
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

export default new DocumentService()