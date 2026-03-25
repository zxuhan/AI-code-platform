// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** No description provided by the backend GET /static/${param0}/&#42;&#42; */
export async function serveStaticResource(
  // Generated Param type for non-body parameters (Swagger does not produce an object for these by default)
  params: API.serveStaticResourceParams,
  options?: { [key: string]: any }
) {
  const { deployKey: param0, ...queryParams } = params
  return request<string>(`/static/${param0}/**`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}
