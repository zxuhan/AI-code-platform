// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** No description provided by the backend GET /health/ */
export async function healthCheck(options?: { [key: string]: any }) {
  return request<API.BaseResponseString>('/health/', {
    method: 'GET',
    ...(options || {}),
  })
}
