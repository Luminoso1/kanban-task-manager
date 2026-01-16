import { Field, Input } from '@chakra-ui/react'
import { Controller, useFormContext } from 'react-hook-form'

interface FormInputProps {
  label: string
  id: string
  name: string
  type?: string
  rules?: any
}

export const FormInput = ({
  label,
  id,
  name,
  type = 'text',
  rules
}: FormInputProps) => {
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
        render={({ field }) => (
          <Input {...field} ref={field.ref} id={id} name={name} type={type} />
        )}
      />
      <Field.ErrorText>{error?.message?.toString()}</Field.ErrorText>
    </Field.Root>
  )
}
