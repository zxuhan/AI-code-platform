import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getLoginUser } from '@/api/userController.ts'

/**
 * Logged-in user store.
 */
export const useLoginUserStore = defineStore('loginUser', () => {
  // Default value
  const loginUser = ref<API.LoginUserVO>({
    userName: 'Not logged in',
  })

  // Fetch the logged-in user
  async function fetchLoginUser() {
    const res = await getLoginUser()
    if (res.data.code === 0 && res.data.data) {
      loginUser.value = res.data.data
    }
  }

  // Update the logged-in user
  function setLoginUser(newLoginUser: any) {
    loginUser.value = newLoginUser
  }

  return { loginUser, fetchLoginUser, setLoginUser }
})
