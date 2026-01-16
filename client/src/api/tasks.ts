import api from '@/config/axios'
import type { Task } from '@/types'

export const createTask = async (task: Omit<Task, 'id'>) => {
  const { data } = await api.post('/tasks', task)
  return data
}

export const editTask = async (task: Task) => {
  console.log("Taks: ", task);
  const { data } = await api.patch('/tasks', task)
  return data
}

export const deleteTask = async (id: number) => {
  const { data } = await api.delete(`/tasks/${id}`)
  return data
}
