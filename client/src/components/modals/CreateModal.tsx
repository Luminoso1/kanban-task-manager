import { Dialog } from '@chakra-ui/react'

interface CreateModalProps {
  open: boolean
  onOpenChange: React.Dispatch<React.SetStateAction<boolean>>
  title: string
  children: React.ReactNode
}

function CreateModal({
  open,
  onOpenChange,
  title,
  children
}: CreateModalProps) {
  return (
    <Dialog.Root
      placement='center'
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
          <Dialog.Body>{children}</Dialog.Body>
          <Dialog.Footer />
        </Dialog.Content>
      </Dialog.Positioner>
    </Dialog.Root>
  )
}

export default CreateModal
