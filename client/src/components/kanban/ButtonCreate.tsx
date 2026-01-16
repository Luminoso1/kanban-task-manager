import { Button, type ButtonProps } from '@chakra-ui/react'

interface ButtonCreateProps extends ButtonProps {
  children: React.ReactNode
}

const ButtonCreate = ({ children, ...props }: ButtonCreateProps) => {
  return <Button {...props}>{children}</Button>
}

export default ButtonCreate
