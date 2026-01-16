import { Navigate, Outlet } from 'react-router'

function AuthLayout() {
  const user = null

  if (user) return <Navigate to={'/'} />

  return (
    <>
      <Outlet />
    </>
  )
}

export default AuthLayout
