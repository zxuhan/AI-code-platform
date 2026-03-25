<template>
  <div id="userLoginPage">
    <h2 class="title">zxuhan AI App Generator - Login</h2>
    <div class="desc">Generate full applications without writing a line of code</div>
    <a-form :model="formState" name="basic" autocomplete="off" @finish="handleSubmit">
      <a-form-item name="userAccount" :rules="[{ required: true, message: 'Please enter your account' }]">
        <a-input v-model:value="formState.userAccount" placeholder="Account" />
      </a-form-item>
      <a-form-item
        name="userPassword"
        :rules="[
          { required: true, message: 'Please enter your password' },
          { min: 8, message: 'Password must be at least 8 characters' },
        ]"
      >
        <a-input-password v-model:value="formState.userPassword" placeholder="Password" />
      </a-form-item>
      <div class="tips">
        Don't have an account?
        <RouterLink to="/user/register">Register</RouterLink>
      </div>
      <a-form-item>
        <a-button type="primary" html-type="submit" style="width: 100%">Log in</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>
<script lang="ts" setup>
import { reactive } from 'vue'
import { userLogin } from '@/api/userController.ts'
import { useLoginUserStore } from '@/stores/loginUser.ts'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'

const formState = reactive<API.UserLoginRequest>({
  userAccount: '',
  userPassword: '',
})

const router = useRouter()
const loginUserStore = useLoginUserStore()

/**
 * Submit the form.
 * @param values form values
 */
const handleSubmit = async (values: any) => {
  const res = await userLogin(values)
  // On success, persist the login state to the store
  if (res.data.code === 0 && res.data.data) {
    await loginUserStore.fetchLoginUser()
    message.success('Logged in successfully')
    router.push({
      path: '/',
      replace: true,
    })
  } else {
    message.error('Login failed: ' + res.data.message)
  }
}
</script>

<style scoped>
#userLoginPage {
  background: white;
  max-width: 720px;
  padding: 24px;
  margin: 24px auto;
}

.title {
  text-align: center;
  margin-bottom: 16px;
}

.desc {
  text-align: center;
  color: #bbb;
  margin-bottom: 16px;
}

.tips {
  text-align: right;
  color: #bbb;
  font-size: 13px;
  margin-bottom: 16px;
}
</style>
