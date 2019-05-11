import http from '@/service/http'

class DocumentService {

    async retrieve(id, full = false) {
        return (await http.get(`/documents/${id}`, { full })).data
    }

    async create(parentId, name, mimeType) {
        return (await http.post("/documents", { parentId, name, mimeType })).data
    }

    async upload(id, binary) {
        await http.put(`/documents/${id}/upload`, binary, {}, 'blob')
    }

    async download(id) {
        return (await http.get(`/documents/${id}/download`, {}, 'blob')).data
    }

    async rename(id, name) {
        return (await http.put(`/documents/${id}`, { name })).data
    }

    async remove(id, name) {
        return (await http.delete(`/documents/${id}`, { name })).data
    }

    async move(id, parentId) {
        return (await http.put(`/documents/${id}`, { parentId })).data
    }

    async copy(id, parentId) {
        return (await http.post(`/documents/${id}/copy`, { parentId })).data
    }
}

export default new DocumentService()