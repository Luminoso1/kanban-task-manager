import { login } from '@/api/auth'
import { toaster } from '@/components/ui/toaster'
import {
  Box,
  Container,
  Field,
  Flex,
  Heading,
  Input,
  Stack,
  Text,
  Link as ChakraLink,
  Button
} from '@chakra-ui/react'
import { useMutation } from '@tanstack/react-query'
import { useState } from 'react'
import { Link, useLocation, useNavigate } from 'react-router'

function Signin() {
  const location = useLocation()
  const navigate = useNavigate()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')

  const redirectUrl = location.state?.redirectUrl || '/'

  const { mutate: signIn, isPending } = useMutation({
    mutationFn: login,
    onSuccess: () => {
      navigate(redirectUrl, { replace: true })
    },
    onError: (error) => {
      toaster.create({
        title: 'Error',
        description: error.message || 'An error occurred',
        type: 'error',
        id: "signin-error",
      })
    }
  })

  return (
    <Flex minH='100vh' align='center' justify='center'>
      <Container mx='auto' maxW='md' py={12} px={6} textAlign='center'>
        <Heading as='h1' fontSize='4xl' mb={6}>
          Sign in to your account
        </Heading>

        <Box rounded='lg' bg='darkAlt' boxShadow='lg' p={8}>
          <Stack borderSpacing={4}>
            <Field.Root mb='2' id='email'>
              <Field.Label>Email address</Field.Label>
              <Input
                type='email'
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                autoFocus
              />
            </Field.Root>

            <Field.Root mb='2' id='password'>
              <Field.Label>Password</Field.Label>
              <Input
                type='password'
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </Field.Root>

            <ChakraLink
              asChild
              fontSize='sm'
              textAlign={{ base: 'center', sm: 'right' }}
            >
              <Link to='/forget-password'>Forgot password?</Link>
            </ChakraLink>

            <Button
              size='lg'
              my={2}
              loading={isPending}
              disabled={!email || password.length < 6}
              onClick={() => signIn({ email, password })}
            >
              Sign In
            </Button>

            <Text textAlign='center' color='gray'>
              Don’t have an account?{' '}
              <ChakraLink asChild>
                <Link to='/sign-up'>Sign Up</Link>
              </ChakraLink>
            </Text>
          </Stack>
        </Box>
      </Container>
    </Flex>
  )
}

export default Signin
