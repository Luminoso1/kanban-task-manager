import { FormProvider, useForm } from 'react-hook-form'
import { VStack, Button } from '@chakra-ui/react'
import { FormInput } from './FormInput'
import type { Board, NewBoard } from '@/types'

interface BoardFormProps {
  onSubmit: (data: NewBoard) => void
  initialValues?: Board
}

const BoardForm = ({ onSubmit, initialValues }: BoardFormProps) => {
  const methods = useForm<NewBoard>({
    defaultValues: initialValues
  })

  const { handleSubmit } = methods

  return (
    <FormProvider {...methods}>
      <VStack
        as='form'
        onSubmit={handleSubmit(onSubmit)}
        borderSpacing={4}
        align='stretch'
      >
        <FormInput
          label='Board Name'
          id='name'
          name='name'
          rules={{
            required: 'Board name is mandatory',
            minLength: { value: 3, message: 'Minimum 3 characters' }
          }}
        />

        <Button type='submit' colorScheme='blue' mt='4'>
          Save
        </Button>
      </VStack>
    </FormProvider>
  )
}

export default BoardForm
