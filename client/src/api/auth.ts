import api from '../config/axios'

type AuthRequest = {
  email: string
  password: string
}

export const signUp = async (request: AuthRequest) => {
  const { data } = await api.post('/auth/signup', request)
  return data
}

export const login = async (request: AuthRequest) => {
  const { data } = await api.post('/auth/signin', request)
  return data
}

export const logout = async () => {
  const { data } = await api.get('/auth/signout')
  return data
}
