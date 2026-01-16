import api from '@/config/axios'

export const getProfile = async () => {
  await new Promise((resolve) => setTimeout(resolve, 1000))
  const { data } = await api.get('/users/me')
  return data
}
