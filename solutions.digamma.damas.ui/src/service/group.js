import http from '@/service/http'

const BASE_URL = "/groups"

class GroupService {

    async retrieve(id) {
        return (await http.get(`${BASE_URL}/${id}`)).data
    }

    async list(offset) {
        return (await http.get(`${BASE_URL}`, { offset })).data.objects
    }

    async create(group) {
        return (await http.post(`${BASE_URL}`, group)).data
    }

    async remove(id) {
        return (await http.delete(`${BASE_URL}/${id}`)).data
    }

    async update(group) {
        return (await http.put(`${BASE_URL}/${group.id}`, {label: group.label})).data
    }
}

export default new GroupService()