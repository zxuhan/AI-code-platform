import { useLoginUserStore } from '@/stores/loginUser'
import { message } from 'ant-design-vue'
import router from '@/router'

// Whether this is the first fetch of the logged-in user
let firstFetchLoginUser = true

/**
 * Global authorization check.
 */
router.beforeEach(async (to, from, next) => {
  const loginUserStore = useLoginUserStore()
  let loginUser = loginUserStore.loginUser
  // On a hard refresh, wait for the backend to return user info before checking access
  if (firstFetchLoginUser) {
    await loginUserStore.fetchLoginUser()
    loginUser = loginUserStore.loginUser
    firstFetchLoginUser = false
  }
  const toUrl = to.fullPath
  if (toUrl.startsWith('/admin')) {
    if (!loginUser || loginUser.userRole !== 'admin') {
      message.error('Not authorized')
      next(`/user/login?redirect=${to.fullPath}`)
      return
    }
  }
  next()
})
