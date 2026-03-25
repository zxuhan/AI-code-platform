/**
 * Code generation type enumeration.
 */
export enum CodeGenTypeEnum {
  HTML = 'html',
  MULTI_FILE = 'multi_file',
  VUE_PROJECT = 'vue_project',
}

/**
 * Code generation type configuration.
 */
export const CODE_GEN_TYPE_CONFIG = {
  [CodeGenTypeEnum.HTML]: {
    label: 'Single-file HTML',
    value: CodeGenTypeEnum.HTML,
  },
  [CodeGenTypeEnum.MULTI_FILE]: {
    label: 'Multi-file native',
    value: CodeGenTypeEnum.MULTI_FILE,
  },
  [CodeGenTypeEnum.VUE_PROJECT]: {
    label: 'Vue project',
    value: CodeGenTypeEnum.VUE_PROJECT,
  },
} as const

/**
 * Code generation type options (for dropdowns).
 */
export const CODE_GEN_TYPE_OPTIONS = Object.values(CODE_GEN_TYPE_CONFIG).map((config) => ({
  label: config.label,
  value: config.value,
}))

/**
 * Format a code generation type for display.
 * @param type code generation type
 * @returns the formatted label
 */
export const formatCodeGenType = (type: string | undefined): string => {
  if (!type) return 'Unknown type'

  const config = CODE_GEN_TYPE_CONFIG[type as CodeGenTypeEnum]
  return config ? config.label : type
}

/**
 * Get all code generation types.
 */
export const getAllCodeGenTypes = () => {
  return Object.values(CodeGenTypeEnum)
}

/**
 * Check whether the given value is a valid code generation type.
 * @param type value to check
 */
export const isValidCodeGenType = (type: string): type is CodeGenTypeEnum => {
  return Object.values(CodeGenTypeEnum).includes(type as CodeGenTypeEnum)
}
