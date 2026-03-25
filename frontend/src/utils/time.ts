import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import 'dayjs/locale/en'

dayjs.extend(relativeTime)
dayjs.locale('en')

/**
 * Format a time.
 * @param time time string
 * @param format format string, default 'YYYY-MM-DD HH:mm:ss'
 * @returns formatted time string, or an empty string if input is empty
 */
export const formatTime = (time: string | undefined, format = 'YYYY-MM-DD HH:mm:ss'): string => {
  if (!time) return ''
  return dayjs(time).format(format)
}

/**
 * Format a time as a relative time.
 * @param time time string
 * @returns relative time string, e.g. "2 hours ago"
 */
export const formatRelativeTime = (time: string | undefined): string => {
  if (!time) return ''
  return dayjs(time).fromNow()
}

/**
 * Format a time as a date.
 * @param time time string
 * @returns date string, e.g. "2024-01-01"
 */
export const formatDate = (time: string | undefined): string => {
  if (!time) return ''
  return dayjs(time).format('YYYY-MM-DD')
}
