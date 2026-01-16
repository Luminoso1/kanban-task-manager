import { useBoards } from '@/hooks/useBoards'
import { Center, Heading, Spinner, Text } from '@chakra-ui/react'
import { useEffect } from 'react'
import { useNavigate } from 'react-router'

function Home() {
  const { boards, isLoading, isError } = useBoards()
  const navigate = useNavigate()

  useEffect(() => {
    if (!isLoading && boards && boards.length > 0) {
      navigate(`/board/${boards[0].id}`)
    }
  }, [boards, isLoading, navigate])

  if (isLoading) {
    return <Spinner />
  }

  if (isError || !boards) {
    return <Text>No boards found</Text>
  }

  return (
    <Center minH='100dvh' w='full' flexDir='column'>
      <Heading as='h2' color='white' fontWeight='bold'>
        Looks like you don't have any board yet
      </Heading>
      <Text fontSize='lg' my='2'>
        start creating your first Board and add Tasks
      </Text>
    </Center>
  )
}

export default Home
