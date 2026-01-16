import type { Task } from '@/types'
import { Flex, Icon, Text, VStack } from '@chakra-ui/react'
import TaskItem from '../tasks/TaskItem'
import { DotIcon } from '../icons/dot'

interface ColumnProps {
  name: Task['status']
  quantity: number
  tasks: Task[]
}

function Column(props: ColumnProps) {
  const { name, quantity, tasks } = props
  return (
    <VStack alignItems='start' h='full'>
      <Flex mb='2' gap='2'>
        <Icon>
          <DotIcon
            fill={
              name === 'TODO'
                ? '#49C4E5'
                : name === 'DOING'
                ? '#8471F2'
                : '#67E2AE'
            }
          />
        </Icon>
        <Text letterSpacing='2px' fontSize='sm' fontWeight='bold'>
          {name} ({quantity})
        </Text>
      </Flex>

      {tasks.map((task) => (
        <TaskItem key={task.id} task={task} />
      ))}
    </VStack>
  )
}

export default Column
