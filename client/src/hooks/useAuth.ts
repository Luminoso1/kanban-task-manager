import { logout } from '@/api/auth'
import { getProfile } from '@/api/user'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'

const useAuth = (opts = {}) => {
  const queryClient = useQueryClient()

  const { data: user, ...res } = useQuery({
    queryKey: ['auth'],
    queryFn: getProfile,
    staleTime: Infinity,
    ...opts
  })

  const { mutate: signOut } = useMutation({
    mutationFn: logout,
    onSettled: () => {
      queryClient.clear()
    }
  })

  return {
    user,
    signOut,
    ...res
  }
}

export default useAuth
