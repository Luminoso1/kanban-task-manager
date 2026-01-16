import type { Task } from '@/types'
import { create } from 'zustand'

interface TaskStore {
  selectedTask: Task | null
  isModalOpen: boolean
  openModal: (task: Task) => void
  closeModal: () => void
}

export const useTaskStore = create<TaskStore>((set) => ({
  selectedTask: null,
  isModalOpen: false,
  openModal: (task) => set({ selectedTask: task, isModalOpen: true }),
  closeModal: () => set({ isModalOpen: false, selectedTask: null })
}))
