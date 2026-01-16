import {
  Button,
  Flex,
  Icon,
  Spinner,
  Stack,
  Switch,
  Text
} from '@chakra-ui/react'
import { BoardLink } from './BoardLink'
import { SunIcon } from '../icons/sun'
import { MoonIcon } from '../icons/moon'
import { EyeIcon } from '../icons/eye'
import CreateModal from '../modals/CreateModal'
import ButtonCreate from '../kanban/ButtonCreate'
import BoardForm from '../form/BoardForm'
import { useState } from 'react'
import { useBoards } from '@/hooks/useBoards'
import type { Board } from '@/types'
import { OpenEye } from '../icons/open'
import { useNavigate } from 'react-router'

function Boards() {
  const navigate = useNavigate()
  const [sidebarVisible, setSidebarVisible] = useState(true)
  const [openCreateModal, setOpenCreateModal] = useState(false)

  // obtener todos los boards acá -> del loader de react router con tnastack query
  const { boards, isLoading, createBoard } = useBoards()

  const handleSubmitCreateBoard = async ({ name }: { name: string }) => {
    const data = await createBoard.mutateAsync(name)
    navigate(`board/${data.id}`)
    setOpenCreateModal(false)
  }

  return (
    <>
      <Flex
        width={sidebarVisible ? 'full' : '0'}
        minW={sidebarVisible ? '300px' : '0'}
        maxW={sidebarVisible ? '300px' : '0'}
        transition='width 0.4s ease'
        overflow='hidden'
        direction='column'
        justifyContent='space-between'
        bg='darkAlt'
        minH='100vh'
        px='0'
        pt='28'
        pb='8'
        boxShadow='#3E3F4E 1px 1px 1px'
      >
        <Stack
          w='full'
          pr='8'
          opacity={sidebarVisible ? 1 : 0}
          transition='opacity 0.2s'
        >
          <Text fontSize='sm' pl='4' fontWeight='bold' letterSpacing='2px'>
            ALL BOARDS ({boards?.length || 0})
          </Text>

          {isLoading && <Spinner />}

          {!isLoading &&
            boards?.map((board: Board) => (
              <BoardLink key={board.id} board={board} />
            ))}
          <ButtonCreate
            color='primary'
            variant='plain'
            px='0'
            border='none'
            onClick={() => setOpenCreateModal(true)}
          >
            + Create New Board
          </ButtonCreate>
        </Stack>
        <Stack mx='4'>
          <Flex
            py='4'
            justifyContent='space-evenly'
            alignItems='center'
            bg='dark'
            rounded='md'
          >
            <Icon>
              <SunIcon />
            </Icon>
            <Switch.Root>
              <Switch.HiddenInput />
              <Switch.Control bg='primary' />
              <Switch.Label />
            </Switch.Root>
            <Icon>
              <MoonIcon />
            </Icon>
          </Flex>
          <Button
            variant='plain'
            p='0'
            w='fit'
            mt='2'
            onClick={() => setSidebarVisible(false)}
          >
            <Icon>
              <EyeIcon />
            </Icon>
            <Text>Hide Sidebar</Text>
          </Button>
        </Stack>
      </Flex>

      {!sidebarVisible && (
        <Button
          position='fixed'
          left='-6'
          bottom='4'
          w='80px'
          h='60px'
          onClick={() => setSidebarVisible(true)}
        >
          <Icon size='xl'>
            <OpenEye fill='white' />
          </Icon>
        </Button>
      )}

      <CreateModal
        open={openCreateModal}
        onOpenChange={setOpenCreateModal}
        title='Add New Board'
      >
        <BoardForm onSubmit={handleSubmitCreateBoard} />
      </CreateModal>
    </>
  )
}

export default Boards
