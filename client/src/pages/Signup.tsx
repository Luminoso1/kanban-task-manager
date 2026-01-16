import { signUp } from "@/api/auth";
import { toaster } from "@/components/ui/toaster";
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
  Button,
} from "@chakra-ui/react";
import { useMutation } from "@tanstack/react-query";
import { useState } from "react";
import { Link, useNavigate } from "react-router";

function Signup() {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const { mutate: createAccount, isPending } = useMutation({
    mutationFn: signUp,
    onSuccess: () => {
      navigate("/", { replace: true });
    },
    onError: (error) => {
      toaster.create({
        title: "Error",
        description: error.message || "An error occurred",
        type: "error",
        id: "signup-error",
      });
    },
  });

  return (
    <Flex minH="100vh" align="center" justify="center">
      <Container mx="auto" maxW="md" py={12} px={6} textAlign="center">
        <Heading fontSize="4xl" mb={6}>
          Create an account
        </Heading>
        <Box rounded="lg" bg="darkAlt" boxShadow="lg" p={8}>
          <Stack borderSpacing={4}>
            <Field.Root mb="2" id="email">
              <Field.Label>Email address</Field.Label>
              <Input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                autoFocus
              />
            </Field.Root>

            <Field.Root mb="2" id="password">
              <Field.Label>Password</Field.Label>
              <Input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </Field.Root>

            <Field.Root mb="2" id="confirmPassword">
              <Field.Label>Confirm Password</Field.Label>
              <Input
                type="password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
              />
            </Field.Root>

            <Button
              size="lg"
              loading={isPending}
              disabled={
                !email || password.length < 6 || password !== confirmPassword
              }
              onClick={() => createAccount({ email, password })}
            >
              Sign Up
            </Button>

            <Text textAlign="center" color="gray" fontSize="sm">
              Already have an account?{" "}
              <ChakraLink asChild>
                <Link to="/sign-in">Sign in</Link>
              </ChakraLink>
            </Text>
          </Stack>
        </Box>
      </Container>
    </Flex>
  );
}

export default Signup;
