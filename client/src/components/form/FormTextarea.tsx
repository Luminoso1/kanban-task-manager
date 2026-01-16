import { Field, Textarea } from '@chakra-ui/react'
import { Controller, useFormContext } from 'react-hook-form'

interface FormTextareaProps {
  label: string
  id: string
  name: string
  rules?: any
}

export const FormTextarea = ({ label, id, name, rules }: FormTextareaProps) => {
  const {
    control,
    formState: { errors }
  } = useFormContext()

  const error = errors?.[name]
  return (
    <Field.Root invalid={!!error}>
      <Field.Label htmlFor={id}>{label}</Field.Label>
      <Controller
        name={name}
        control={control}
        rules={rules}
        render={({ field }) => <Textarea {...field} id={id} />}
      />
      <Field.ErrorText>{error?.message?.toString()}</Field.ErrorText>
    </Field.Root>
  )
}
