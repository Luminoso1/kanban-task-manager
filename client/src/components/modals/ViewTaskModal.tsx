import type { Task } from '@/types'
import {
  Box,
  Button,
  Dialog,
  Field,
  HStack,
  NativeSelect,
  Text,
  VStack
} from '@chakra-ui/react'
import ShowOptions from '../kanban/ShowOptions'
import { useState } from 'react'
import DeleteModal from './DeleteModal'
import CreateModal from './CreateModal'
import TaskForm from '../form/TaskForm'
import { useTasks } from '@/hooks/useTasks'

interface ViewTaskModalProps {
  task: Task
  isOpen: boolean
  onClose: () => void
}

function ViewTaskModal({ task, isOpen, onClose }: ViewTaskModalProps) {
  const { title, description, dueDate, priority, status } = task
  const [openDeleteModal, setOpenDeleteModal] = useState(false)
  const [openEditModal, setOpenEditModal] = useState(false)

  const { edit, remove } = useTasks()

  const handleDeleteTask = async () => {
    if (task) {
      await remove.mutateAsync(task.id)
      setOpenDeleteModal(false)
      onClose()
    }
  }

  const handleEditTask = async (task: Task) => {
    await edit.mutateAsync(task)
    setOpenEditModal(false)
    onClose()
  }

  return (
    <>
      <Dialog.Root
        placement='center'
        lazyMount
        open={isOpen && !openEditModal && !openDeleteModal}
        onOpenChange={() => onClose()}
      >
        <Dialog.Backdrop />
        <Dialog.Positioner>
          <Dialog.Content bg='darkAlt'>
            <Dialog.CloseTrigger />
            <Dialog.Header>
              <HStack
                justifyContent='space-between'
                alignItems='center'
                w='full'
              >
                <Dialog.Title color='white' fontSize='lg' fontWeight='bold'>
                  {title}
                </Dialog.Title>
                <ShowOptions>
                  <Button
                    fontSize='md'
                    variant='plain'
                    px='3'
                    onClick={() => setOpenEditModal(true)}
                  >
                    Edit task
                  </Button>

                  <Button
                    fontSize='md'
                    variant='plain'
                    px='3'
                    fontWeight='initial'
                    color='red'
                    onClick={() => setOpenDeleteModal(true)}
                  >
                    Delete task
                  </Button>
                </ShowOptions>
              </HStack>
            </Dialog.Header>
            <Dialog.Body fontSize='md' color='white'>
              <VStack alignItems='stretch' spaceY='2'>
                <Box>
                  <Text>{description}</Text>
                </Box>

                <Box spaceY='1'>
                  <Field.Root>
                    <Field.Label>Current Status</Field.Label>
                  </Field.Root>
                  <NativeSelect.Root disabled>
                    <NativeSelect.Field>
                      <option value={status}>{status}</option>
                    </NativeSelect.Field>
                    <NativeSelect.Indicator />
                  </NativeSelect.Root>
                </Box>

                <Box spaceY='1'>
                  <Field.Root>
                    <Field.Label>Priority</Field.Label>
                  </Field.Root>
                  <NativeSelect.Root disabled>
                    <NativeSelect.Field>
                      <option value={priority}>{priority}</option>
                    </NativeSelect.Field>
                    <NativeSelect.Indicator />
                  </NativeSelect.Root>
                </Box>

                <Box spaceY='1'>
                  <Field.Root>
                    <Field.Label>Due Date</Field.Label>
                  </Field.Root>
                  {dueDate &&
                    new Date(dueDate)?.toLocaleDateString('es-ES', {
                      weekday: 'long',
                      year: 'numeric',
                      month: 'long',
                      day: 'numeric'
                    })}
                </Box>
              </VStack>
            </Dialog.Body>
          </Dialog.Content>
        </Dialog.Positioner>
      </Dialog.Root>

      {/* Delete Task Modal */}
      <DeleteModal
        open={openDeleteModal}
        onOpenChange={setOpenDeleteModal}
        title='Delete this task?'
        onDelete={handleDeleteTask}
      >
        Are you sure you want to delete the {title} board? This action will
        remove all columns and tasks and cannot be reversed.
      </DeleteModal>

      {/* Edit Task Modal */}
      <CreateModal
        open={openEditModal}
        onOpenChange={setOpenEditModal}
        title='Edit Task'
      >
        <TaskForm onSubmit={handleEditTask} initialValues={task} />
      </CreateModal>
    </>
  )
}

export default ViewTaskModal
