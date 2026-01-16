import type { Board } from '@/types'
import { Flex, Icon, Text, Link as ChakraLink } from '@chakra-ui/react'
import { Link, useLocation } from 'react-router'
import { BoardIcon } from '../icons/board'

export const BoardLink = ({ board }: { board: Board }) => {
  const location = useLocation()
  const isActive = location.pathname === `/board/${board.id}`

  return (
    <ChakraLink
      asChild
      pl='4'
      bg={isActive ? 'primary' : 'transparent'}
      color={isActive ? 'white' : 'gray'}
      _hover={{ bg: 'white', color: 'primary' }}
      transitionDuration='0.3s'
      borderRightRadius='full'
      outline='none'
    >
      <Link to={`board/${board.id}`}>
        <Flex alignItems='center' gap='4' py='3'>
          <Icon>
            <BoardIcon fill={isActive ? 'white' : 'gray'} />
          </Icon>

          <Text fontSize='md' fontWeight='bold'>
            {board.name}
          </Text>
        </Flex>
      </Link>
    </ChakraLink>
  )
}
