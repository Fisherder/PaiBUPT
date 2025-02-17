/// <reference types="vite/client" />
declare module '*.vue' {
    import type { DefineComponent } from 'vue'
    const component: DefineComponent<{}, {}, any>
    export default component
    }
    interface ImportMetaEnv {
        readonly VITE_BASE_API: string
        // 可添加其他 VITE_ 开头的变量
      }
      
      interface ImportMeta {
        readonly env: ImportMetaEnv
      }