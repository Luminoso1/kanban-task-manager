import { Button, Dialog, HStack, Text } from '@chakra-ui/react'

interface DeleteModalProps {
  open: boolean
  onOpenChange: React.Dispatch<React.SetStateAction<boolean>>
  title: string
  onDelete: (id: number) => void
  children: React.ReactNode
}

function DeleteModal({
  open,
  onOpenChange,
  title,
  onDelete,
  children
}: DeleteModalProps) {
  return (
    <Dialog.Root
      placement='center'
      lazyMount
      open={open}
      onOpenChange={(e) => onOpenChange(e.open)}
    >
      <Dialog.Backdrop />
      <Dialog.Positioner>
        <Dialog.Content bg='darkAlt'>
          <Dialog.CloseTrigger />
          <Dialog.Header>
            <Dialog.Title color='white' fontSize='lg' fontWeight='bold'>
              {title}
            </Dialog.Title>
          </Dialog.Header>
          <Dialog.Body>
            <Text fontSize='md'>{children}</Text>
          </Dialog.Body>
          <Dialog.Footer>
            <HStack w='full'>
              <Button variant='destructive' w='1/2' onClick={() => onDelete()}>
                Delete
              </Button>

              <Dialog.ActionTrigger asChild w='1/2'>
                <Button color='primary' bg='white'>
                  Cancel
                </Button>
              </Dialog.ActionTrigger>
            </HStack>
          </Dialog.Footer>
        </Dialog.Content>
      </Dialog.Positioner>
    </Dialog.Root>
  )
}

export default DeleteModal
