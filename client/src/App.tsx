import { createBrowserRouter } from 'react-router'
import AppLayout from './layouts/AppLayout'
import Home from './pages/Home'
import AuthLayout from './layouts/AuthLayout'
import Signin from './pages/Signin'
import Signup from './pages/Signup'
import Kanban from './components/kanban/Kanban'
import GeneralError from './pages/GeneralError'
import BoardError from './pages/BoardError'

const router = createBrowserRouter([
  {
    Component: AuthLayout,
    errorElement: <GeneralError />,
    children: [
      {
        path: '/sign-in',
        Component: Signin
      },
      {
        path: '/sign-up',
        Component: Signup
      }
    ]
  },
  {
    Component: AppLayout,
    errorElement: <GeneralError />,
    children: [
      {
        path: '/',
        Component: Home
      },
      {
        path: '/board/:id',
        Component: Kanban,
        errorElement: <BoardError />
      }
    ]
  }
])

export default router
