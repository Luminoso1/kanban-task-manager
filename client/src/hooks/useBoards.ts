import {
  createBoard as createBoardRequest,
  deleteBoard as deleteBoardRequest,
  editBoard as editBoardRequest,
  getAllBoards
} from '@/api/boards'
import { toaster } from '@/components/ui/toaster'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'

export const useBoards = () => {
  const queryClient = useQueryClient()

  const {
    data: boards,
    isLoading,
    isError
  } = useQuery({
    queryKey: ['boards'],
    staleTime: 1000 * 60 * 5,
    queryFn: getAllBoards
  })

  const createBoard = useMutation({
    mutationFn: createBoardRequest,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['boards'] }),
    onError: (error) => {
      toaster.create({
        title: 'Error',
        description: error.message || 'An error occurred',
        type: 'error'
      })
    }
  })

  const editBoard = useMutation({
    mutationFn: editBoardRequest,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['boards'] })
  })

  const deleteBoard = useMutation({
    mutationFn: deleteBoardRequest,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['boards'] }),
    onError: (error) => {
      toaster.create({
        title: 'Error',
        description: error.message || 'An error occurred',
        type: 'error'
      })
    }
  })

  return {
    boards,
    isLoading,
    isError,
    createBoard,
    editBoard,
    deleteBoard
  }
}
