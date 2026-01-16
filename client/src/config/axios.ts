import axios from 'axios'

const options = {
  baseURL: import.meta.env.VITE_SERVER_URI,
  withCredentials: true
}

const TokenRefreshClient = axios.create(options)

TokenRefreshClient.interceptors.response.use((response) => response.data)

const api = axios.create(options)

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const data = error?.response?.data

    let message = 'Unknown error'

    if (typeof data?.message === 'string') {
      message = data.message
    } else if (typeof data === 'object' && data !== null) {
      const firstField = Object.keys(data)[0]
      const firstMessage = data[firstField]

      if (typeof firstMessage === 'string') {
        message = firstMessage
      } else if (Array.isArray(firstMessage)) {
        message = firstMessage[0]
      }
    }

    return Promise.reject(new Error(message))
  }
)

// api.interceptors.response.use(
//   (response) => response.data,
//   async (error) => {
//     const { config, response } = error
//     const { status, data } = response || {}

//     if (status === 401 && data?.errorCode === 'InvalidAccessToken') {
//       try {
//         await TokenRefreshClient.get('/auth/refresh-token')

//         return TokenRefreshClient(config)
//       } catch (error) {
//         console.log('Error: ', error)
//         queryClient.clear()
//         // navigate('/login', {
//         //   state: {
//         //     redirectUrl: window.location.pathname
//         //   }
//         // })
//       }
//     }
//     return Promise.reject({ status, ...data })
//   }
// )

export default api
