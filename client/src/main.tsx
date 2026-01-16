import { createRoot } from 'react-dom/client'
import { RouterProvider } from 'react-router'
import { QueryClientProvider } from '@tanstack/react-query'
import queryClient from './config/queryClient'
import router from './App'
import './index.css'
import { Provider } from './components/ui/provider'
import { Toaster } from './components/ui/toaster'

createRoot(document.getElementById('root')!).render(
  <Provider>
    <Toaster />
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={router} />
    </QueryClientProvider>
  </Provider>
)
