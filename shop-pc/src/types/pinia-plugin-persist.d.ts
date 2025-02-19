declare module 'pinia-plugin-persist' {
    export interface PersistStrategy {
      key?: string
      storage?: Storage
      paths?: string[]
    }
    export interface PersistOptions {
      enabled: true
      strategies?: PersistStrategy[]
    }
    export function piniaPersist(option?: PersistOptions): PiniaPlugin
  }