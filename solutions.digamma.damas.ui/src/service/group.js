import http from '@/service/http'

const BASE_URL = "/groups"

class GroupService {

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

    async exists(id) {
        try {
            await http.get(`${BASE_URL}/${id}`)
            return true
        } catch (e) {
            if (e.statusCode === 404) {
                return false
            }
            throw e
        }
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