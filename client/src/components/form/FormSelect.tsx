import { Field, NativeSelect } from '@chakra-ui/react'
import { Controller, useFormContext } from 'react-hook-form'

interface FormSelectProps {
  label: string
  id: string
  name: string
  options: string[]
  rules?: any
}

export const FormSelect = ({
  label,
  id,
  name,
  options,
  rules
}: FormSelectProps) => {
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
          <NativeSelect.Root {...field}>
            <NativeSelect.Field id={id}>
              {options.map((option, index) => (
                <option key={index} value={option}>
                  {option}
                </option>
              ))}
            </NativeSelect.Field>
            <NativeSelect.Indicator />
          </NativeSelect.Root>
        )}
      />
      <Field.ErrorText>{error?.message?.toString()}</Field.ErrorText>
    </Field.Root>
  )
}
