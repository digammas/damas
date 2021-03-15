import http from '@/service/http'

const BASE_URL = "/comments"

class commentService {

    async retrieve(id) {
        return (await http.get(`${BASE_URL}/${id}`)).data
    }

    async list(filter, offset, size) {
        let params = {
            ...filter,
            size,
            offset
        }
        return (await http.get(`${BASE_URL}`, params)).data.objects
    }

    async listForFile(scope, offset, size) {
        let list = await this.list({ scope, rec: false }, offset, size)
        return list.sort((x, y) => x.creationdDate < y.creationDate ? 1 : -1)
    }

    async create(comment) {
        return (await http.post(`${BASE_URL}`, comment)).data
    }

    async remove(id) {
        return (await http.delete(`${BASE_URL}/${id}`)).data
    }

    async update(comment) {
        return (await http.put(`${BASE_URL}/${comment.id}`, comment)).data
    }
}

export default new commentService()
