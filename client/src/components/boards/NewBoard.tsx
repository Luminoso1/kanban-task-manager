import { Box, Button, Dialog, Field, Input, Stack } from '@chakra-ui/react'
import { useState } from 'react'

function NewBoard() {
  const [name, setName] = useState('')
  return (
    <Dialog.Root placement='center'>
      <Dialog.Trigger>
        <Button color='primary' variant='plain' px='0' border='none'>
          + Create New Board
        </Button>
      </Dialog.Trigger>
      <Dialog.Backdrop />
      <Dialog.Positioner>
        <Dialog.Content bg='darkAlt'>
          <Dialog.CloseTrigger />
          <Dialog.Header>
            <Dialog.Title color='white' fontSize='lg' fontWeight='bold'>
              Add New Board
            </Dialog.Title>
          </Dialog.Header>
          <Dialog.Body px='0'>
            <Box rounded='lg' boxShadow='lg' p={8}>
              <Stack borderSpacing={4}>
                <Field.Root mb='2' id='email' color='white'>
                  <Field.Label fontSize='md'>Board Name</Field.Label>
                  <Input
                    type='email'
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    autoFocus
                    fontSize='md'
                    placeholder='e.g. Web Design'
                  />
                </Field.Root>
                <Button
                  fontSize='md'
                  my={2}
                  loading={false}
                  disabled={!name || name.length <= 3}
                  onClick={() => {}}
                >
                  Create new Board
                </Button>
              </Stack>
            </Box>
          </Dialog.Body>
          <Dialog.Footer />
        </Dialog.Content>
      </Dialog.Positioner>
    </Dialog.Root>
  )
}

export default NewBoard
