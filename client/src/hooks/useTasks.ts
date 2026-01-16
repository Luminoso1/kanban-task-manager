import { createTask, deleteTask, editTask } from '@/api/tasks'
import { toaster } from '@/components/ui/toaster'
import { useMutation, useQueryClient } from '@tanstack/react-query'

export const useTasks = () => {
  const queryClient = useQueryClient()

  const create = useMutation({
    mutationFn: createTask,
    onSuccess: () => {
      toaster.create({ title: 'Task created', type: 'success' })
      queryClient.invalidateQueries({ queryKey: ['board'] })
    },
    onError: (error) => {
      toaster.create({
        title: 'Error',
        description: error.message,
        type: 'error'
      })
    }
  })

  const edit = useMutation({
    mutationFn: editTask,
    onSuccess: () => {
      toaster.create({ title: 'Task updated', type: 'success' })
      queryClient.invalidateQueries({ queryKey: ['board'] })
    },
    onError: (error) => {
      toaster.create({
        title: 'Error',
        description: error.message,
        type: 'error'
      })
    }
  })

  const remove = useMutation({
    mutationFn: deleteTask,
    onSuccess: () => {
      toaster.create({ title: 'Task deleted', type: 'success' })
      queryClient.invalidateQueries({ queryKey: ['board'] })
    },
    onError: (error) => {
      toaster.create({
        title: 'Error',
        description: error.message,
        type: 'error'
      })
    }
  })

  return { create, edit, remove }
}
