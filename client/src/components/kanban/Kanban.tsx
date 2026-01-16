import { Center, Grid, GridItem, Spinner, Text } from '@chakra-ui/react'
import Column from './Column'
import { useTaskStore } from '@/hooks/useTaskStore'
import ViewTaskModal from '../modals/ViewTaskModal'
import { useParams } from 'react-router'
import { useQuery } from '@tanstack/react-query'
import { getBoardById } from '@/api/boards'

function Kanban() {
  // obtener el board -> es un objeto con el nombre del board, id, tareas
  const { isModalOpen, closeModal, selectedTask } = useTaskStore()

  const { id } = useParams()

  const {
    data: board,
    isLoading,
    isError
  } = useQuery({
    queryKey: ['board', id],
    queryFn: async () => await getBoardById(Number(id)),
    enabled: !!id
  })

  if (isLoading) {
    return (
      <Center>
        <Spinner size='xl' />
      </Center>
    )
  }

  if (isError || !board) {
    return (
      <Center>
        <Text>Something went wrong!</Text>
      </Center>
    )
  }

  return (
    <>
      <Grid
        templateColumns='repeat(3, 1fr)'
        gap='10'
        mx='4'
        mt='28'
        mb='10'
        w='full'
      >
        <GridItem>
          <Column
            name='TODO'
            quantity={board?.tasks?.todo.length || 0}
            tasks={board?.tasks?.todo}
          />
        </GridItem>

        <GridItem>
          <Column
            name='DOING'
            quantity={board?.tasks?.doing.length || 0}
            tasks={board?.tasks?.doing}
          />
        </GridItem>

        <GridItem>
          <Column
            name='DONE'
            quantity={board?.tasks?.done.length || 0}
            tasks={board?.tasks?.done}
          />
        </GridItem>
      </Grid>

      {selectedTask && (
        <ViewTaskModal
          task={selectedTask}
          isOpen={isModalOpen}
          onClose={closeModal}
        />
      )}
    </>
  )
}

export default Kanban
