import {
  Box,
  Heading,
  Text,
  VStack,
  Link as ChakraLink
} from '@chakra-ui/react'
import { Link, useRouteError, isRouteErrorResponse } from 'react-router'

const GeneralError = () => {
  const error = useRouteError()
  const errorMessage = isRouteErrorResponse(error)
    ? `${error.status} - ${error.statusText}`
    : 'Ha ocurrido un error inesperado.'

  return (
    <Box
      minH='100vh'
      display='flex'
      alignItems='center'
      justifyContent='center'
      bg='gray.50'
      px={4}
    >
      <VStack borderSpacing={4} textAlign='center'>
        <Heading color='red.500'>¡Ups! Algo salió mal</Heading>
        <Text fontSize='lg' color='gray.600'>
          {errorMessage}
        </Text>
        <ChakraLink asChild colorScheme='teal' fontSize='md'>
          <Link to='/'>Volver al inicio</Link>
        </ChakraLink>
      </VStack>
    </Box>
  )
}

export default GeneralError
