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
}

export default new DocumentService()