import { Button, Dialog, HStack } from '@chakra-ui/react'

interface ModalDeleteProps {
  name: string
  delete: () => void
  children: React.ReactNode
}

function ModalDelete(props: ModalDeleteProps) {
  const { name, delete: removeFunction, children: trigger } = props

  return (
    <Dialog.Root placement='center'>
      <Dialog.Trigger>{trigger}</Dialog.Trigger>
      <Dialog.Backdrop />
      <Dialog.Positioner>
        <Dialog.Content bg='darkAlt'>
          <Dialog.CloseTrigger />
          <Dialog.Header>
            <Dialog.Title color='red' fontSize='lg' fontWeight='bold'>
              {name}
            </Dialog.Title>
          </Dialog.Header>
          <Dialog.Body px='0'>
            <HStack>
              <Button onClick={removeFunction}>Delete</Button>
              <Button>Cancel</Button>
            </HStack>
          </Dialog.Body>
          <Dialog.Footer />
        </Dialog.Content>
      </Dialog.Positioner>
    </Dialog.Root>
  )
}

export default ModalDelete
