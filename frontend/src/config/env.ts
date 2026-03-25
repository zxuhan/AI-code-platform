/**
 * Environment variable configuration.
 */
import {CodeGenTypeEnum} from "@/utils/codeGenTypes.ts";

// Application deploy domain
export const DEPLOY_DOMAIN = import.meta.env.VITE_DEPLOY_DOMAIN || 'http://localhost'

// API base URL
export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8123/api'

// Static resource base URL
export const STATIC_BASE_URL = `${API_BASE_URL}/static`

// Get the full deploy URL of an application
export const getDeployUrl = (deployKey: string) => {
  return `${DEPLOY_DOMAIN}/${deployKey}`
}

// Get the static preview URL
export const getStaticPreviewUrl = (codeGenType: string, appId: string) => {
  const baseUrl = `${STATIC_BASE_URL}/${codeGenType}_${appId}/`
  // For Vue projects, append the dist suffix
  if (codeGenType === CodeGenTypeEnum.VUE_PROJECT) {
    return `${baseUrl}dist/index.html`
  }
  return baseUrl
}
