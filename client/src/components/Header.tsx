import { Avatar, Button, Flex, HStack, Icon, Popover } from '@chakra-ui/react'
import { LogoIcon } from './icons/logo'
import CreateModal from './modals/CreateModal'
import ButtonCreate from './kanban/ButtonCreate'
import TaskForm from './form/TaskForm'
import ShowOptions from './kanban/ShowOptions'
import DeleteModal from './modals/DeleteModal'
import { useState } from 'react'
import BoardForm from './form/BoardForm'
import { useNavigate, useParams } from 'react-router'
import { useBoards } from '@/hooks/useBoards'
import type { Task } from '@/types'
import { useTasks } from '@/hooks/useTasks'
import useAuth from '@/hooks/useAuth'
import UserIcon from '../assets/avatar-winter-custome-5-svgrepo-com.svg'

function Header() {
  // necesito el board actual -> cuando está en la ruta /board/:id -> obtener ese board especifico
  const { id } = useParams()
  const navigate = useNavigate()

  const [openCreateModal, setOpenCreateModal] = useState(false)
  const [openEditModal, setOpenEditModal] = useState(false)
  const [openDeleteModal, setOpenDeleteModal] = useState(false)

  const { boards, editBoard, deleteBoard } = useBoards()
  const { signOut } = useAuth()

  const currentBoard = boards?.find((board) => board.id == Number(id))

  const handleEditBoard = async (updatedBoardName: string) => {
    if (currentBoard) {
      await editBoard.mutateAsync({ ...currentBoard, name: updatedBoardName })
      setOpenEditModal(false)
    }
  }

  const handleDeleteBoard = async () => {
    if (currentBoard) {
      await deleteBoard.mutateAsync(currentBoard.id)
      setOpenDeleteModal(false)
      if (window.history.length > 1) {
        navigate(-1)
      } else {
        navigate('/', { replace: true })
      }
    }
  }

  const { create } = useTasks()

  const handleCreateTask = async (task: Omit<Task, 'id, userId'>) => {
    await create.mutateAsync({
      ...task,
      boardId: Number(currentBoard?.id)
    })
    setOpenCreateModal(false)
  }

  return (
    <HStack
      as='header'
      bg='darkAlt'
      w='full'
      justifyContent='space-between'
      alignItems='center'
      position='fixed'
      top='0'
      py='4'
      px='4'
      boxShadow='#3E3F4E 1px 1px 1px'
      zIndex='20'
    >
      <Icon position='relative' zIndex='30'>
        <LogoIcon />
      </Icon>

      <Flex alignItems='center' gap='6'>
        <ButtonCreate
          size='xs'
          px='8'
          fontSize='md'
          disabled={!currentBoard}
          onClick={() => setOpenCreateModal(true)}
        >
          + Add New Task
        </ButtonCreate>

        {/* Avatar */}
        <Popover.Root>
          <Popover.Trigger>
            <Avatar.Root bg='dark/50'>
              <Avatar.Fallback />
              <Avatar.Image src={UserIcon} />
            </Avatar.Root>
          </Popover.Trigger>
          <Popover.Positioner>
            <Popover.Content w='180px'>
              <Popover.CloseTrigger />
              <Popover.Body bg='dark'>
                <Button
                  variant='plain'
                  px='0'
                  bg=''
                  fontSize='md'
                  onClick={() => {
                    navigate('/settings', { replace: true })
                  }}
                >
                  Account Settings
                </Button>
                <Button
                  variant='plain'
                  px='0'
                  bg=''
                  fontSize='md'
                  onClick={() => {
                    navigate('/sign-in', { replace: true })
                    signOut()
                  }}
                >
                  Cerrar Sesión
                </Button>
              </Popover.Body>
            </Popover.Content>
          </Popover.Positioner>
        </Popover.Root>

        <ShowOptions>
          <Button
            fontSize='md'
            variant='plain'
            px='3'
            fontWeight='initial'
            onClick={() => setOpenEditModal(true)}
          >
            Edit Board
          </Button>

          <Button
            fontSize='md'
            variant='plain'
            px='3'
            fontWeight='initial'
            color='red'
            onClick={() => setOpenDeleteModal(true)}
          >
            Delete Board
          </Button>
        </ShowOptions>
      </Flex>

      {/* Edit Board modal */}
      <CreateModal
        open={openEditModal}
        onOpenChange={setOpenEditModal}
        title='Edit this board'
      >
        <BoardForm onSubmit={handleEditBoard} initialValues={currentBoard} />
      </CreateModal>

      {/* Delete Board Modal */}
      <DeleteModal
        open={openDeleteModal}
        onOpenChange={setOpenDeleteModal}
        title='Delete this board?'
        onDelete={handleDeleteBoard}
      >
        Are you sure you want to delete the ‘Platform Launch’ board? This action
        will remove all columns and tasks and cannot be reversed.
      </DeleteModal>

      {/* Create Task modal */}
      <CreateModal
        open={openCreateModal}
        onOpenChange={setOpenCreateModal}
        title='Add New Task'
      >
        <TaskForm onSubmit={handleCreateTask} />
      </CreateModal>
    </HStack>
  )
}

export default Header
