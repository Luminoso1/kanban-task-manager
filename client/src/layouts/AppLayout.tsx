import Boards from '@/components/boards/Boards'
import Header from '@/components/Header'
import useAuth from '@/hooks/useAuth'
import { Center, Flex, Spinner } from '@chakra-ui/react'
import { Navigate, Outlet } from 'react-router'

function AppLayout() {
  const { user, isLoading, isError } = useAuth()

  if (isLoading) {
    return (
      <Center h='100vh'>
        <Spinner size='xl' />
      </Center>
    )
  }
  if (!user || isError) return <Navigate to={'/sign-in'} />

  return (
    <Flex>
      <Header />
      <Boards />
      <Outlet />
    </Flex>
  )
}

export default AppLayout
