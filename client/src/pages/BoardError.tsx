// src/pages/errors/BoardError.tsx
import {
  Box,
  Heading,
  Text,
  VStack,
  Link as ChakraLink,
  Center
} from '@chakra-ui/react'
import { useRouteError } from 'react-router'
import { Link } from 'react-router'

const BoardError = () => {
  const error = useRouteError()

  return (
    <Center
      minH='100vh'
      w='full'
      display='flex'
      alignItems='center'
      justifyContent='center'
      bg='gray.100'
      px={4}
    >
      <VStack borderSpacing={5} textAlign='center' maxW='md'>
        <Heading as='h2' size='xl' color='white' fontWeight='bold'>
          No se pudo cargar el tablero
        </Heading>
        <Text fontSize='md' color='gray.600'>
          {(error as Error)?.message ||
            'Puede que el tablero no exista o hubo un problema de red.'}
        </Text>
        <ChakraLink asChild colorScheme='orange' variant='solid'>
          <Link to='/'> Volver al inicio</Link>
        </ChakraLink>
      </VStack>
    </Center>
  )
}

export default BoardError
