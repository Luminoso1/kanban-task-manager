export type Board = {
  id: number
  name: string
}

export type NewBoard = Omit<Board, 'id'>

type Status = 'TODO' | 'DOING' | 'DONE'

type Priority = 'LOW' | 'MEDIUM' | 'HIGH' | 'URGENT'

export type Task = {
  id: number
  boardId: number
  title: string
  description?: string
  status?: Status
  dueDate?: Date
  priority?: Priority
}

export type NewTask = Omit<Task, 'id' | 'boardId'>
