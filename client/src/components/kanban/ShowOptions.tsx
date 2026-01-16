import { IconButton, Popover, VStack } from '@chakra-ui/react'
import { DotsIcon } from '../icons/dots'

function ShowOptions({ children }: { children: React.ReactNode }) {
  return (
    <Popover.Root>
      <Popover.Trigger px='0'>
        <IconButton variant='ghost'>
          <DotsIcon />
        </IconButton>
      </Popover.Trigger>
      <Popover.Positioner>
        <Popover.Content w='180px'>
          <Popover.Body p='2' bg='dark'>
            <VStack alignItems='start'>
              <Popover.CloseTrigger>
                <VStack alignItems='start'>{children}</VStack>
              </Popover.CloseTrigger>
            </VStack>
          </Popover.Body>
        </Popover.Content>
      </Popover.Positioner>
    </Popover.Root>
  )
}

export default ShowOptions
