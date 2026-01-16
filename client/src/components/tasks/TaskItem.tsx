import { useTaskStore } from '@/hooks/useTaskStore'
import type { Task } from '@/types'
import { Text, VStack } from '@chakra-ui/react'

function TaskItem({ task }: { task: Task }) {
  const { title, description } = task
  const { openModal } = useTaskStore()
  return (
    <VStack
      bg='darkAlt'
      w='full'
      minH='80px'
      alignItems='start'
      pt='4'
      px='4'
      rounded='md'
      mb='4'
      cursor='pointer'
      as='button'
      position='relative'
      onClick={() => openModal(task)}
    >
      <Text
        as='h4'
        fontSize='md'
        fontWeight='bold'
        color='white'
        userSelect='none'
      >
        {title}
      </Text>
      <Text fontSize='sm' lineHeight='sm' fontWeight='bold' userSelect='none'>
        {description}
      </Text>
    </VStack>
  )
}

export default TaskItem
