import api from '@/config/axios'
import type { Board } from '@/types'

export const getAllBoards = async () => {
  const { data } = await api.get<Board[]>('/boards')
  return data
}

export const getBoardById = async (id: number) => {
  const { data } = await api.get(`/boards/${id}`)
  return data
}

export const createBoard = async (name: string) => {
  const { data } = await api.post('/boards', { name })
  return data
}

export const editBoard = async (board: Board) => {
  const { data } = await api.put(`/boards/${board.id}`, board.name)
  return data
}

export const deleteBoard = async (id: number) => {
  const { data } = await api.delete(`/boards/${id}`)
  return data
}
