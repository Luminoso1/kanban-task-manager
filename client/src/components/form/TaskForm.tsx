import { FormProvider, useForm } from 'react-hook-form'
import { VStack, Button } from '@chakra-ui/react'
import { FormInput } from './FormInput'
import { FormTextarea } from './FormTextarea'
import { FormSelect } from './FormSelect'
import type { NewTask, Task } from '@/types'

interface TaskFormProps {
  onSubmit: (data: NewTask) => void
  initialValues?: Task
}

const TaskForm = ({ onSubmit, initialValues }: TaskFormProps) => {
  const methods = useForm<NewTask>({
    defaultValues: initialValues
  })

  const { handleSubmit } = methods

  return (
    <FormProvider {...methods}>
      <VStack
        as='form'
        onSubmit={handleSubmit(onSubmit)}
        wordSpacing={4}
        align='stretch'
      >
        <FormInput
          label='Títle'
          id='title'
          name='title'
          rules={{
            required: 'El título es obligatorio',
            minLength: { value: 3, message: 'Mínimo 3 caracteres' }
          }}
        />

        <FormTextarea label='Description' id='description' name='description' />

        <FormSelect
          label='State'
          id='status'
          name='status'
          options={['TODO', 'DOING', 'DONE']}
        />

        <FormSelect
          label='Prioridad'
          id='priority'
          name='priority'
          options={['LOW', 'MEDIUM', 'HIGH', 'URGENT']}
        />

        <FormInput label='Due Date' id='dueDate' name='dueDate' type='date' />

        <Button type='submit' mt='4'>
          Save
        </Button>
      </VStack>
    </FormProvider>
  )
}

export default TaskForm
