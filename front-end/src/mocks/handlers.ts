import { http, HttpResponse, StrictResponse } from 'msw'

export const handlers = [
    http.get('/api/', () => {
        return HttpResponse.json({ message: 'Hello, World!' })
    }),
]