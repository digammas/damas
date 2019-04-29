import http from '@/service/http'

class DocumentService {

    async retrieve(id, full = false) {
        return (await http.get(`/documents/${id}`, { full })).data
    }

    async create(parentId, name) {
        return (await http.post("/documents", { parentId, name })).data
    }

    async upload(id, binary) {
        await http.put("/documents", data)
    }
}

export default new DocumentService()