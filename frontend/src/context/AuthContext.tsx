// Requisito: Gestión de sesiones en el frontend - estado global de autenticación persistente (token JWT en localStorage)
import { createContext, useCallback, useContext, useEffect, useMemo, useState, type ReactNode } from 'react'
import { EVENTO_SESION_EXPIRADA, ErrorApi } from '../services/api'
import { authApi } from '../services/endpoints'
import { CLAVE_SESION, borrarSesion, guardarSesion, leerSesion, type Sesion } from '../services/sesion'
import type { AuthRespuesta, Usuario } from '../types/Api'

interface ValorAuth {
  usuario: Usuario | null
  esAdmin: boolean
  verificando: boolean
  iniciarSesion: (respuesta: AuthRespuesta) => void
  cerrarSesion: () => void
}

const ContextoAuth = createContext<ValorAuth | null>(null)

export const ProveedorAuth = ({ children }: { children: ReactNode }) => {
  const [sesion, setSesion] = useState<Sesion | null>(() => leerSesion())
  const [verificando, setVerificando] = useState<boolean>(() => leerSesion() !== null)

  const cerrarSesion = useCallback(() => {
    borrarSesion()
    setSesion(null)
  }, [])

  const iniciarSesion = useCallback((respuesta: AuthRespuesta) => {
    const nueva: Sesion = { token: respuesta.token, expiraEn: respuesta.expiraEn, usuario: respuesta.usuario }
    guardarSesion(nueva)
    setSesion(nueva)
  }, [])

  useEffect(() => {
    if (leerSesion() === null) {
      setVerificando(false)
      return
    }
    let activo = true
    authApi
      .perfil()
      .then((usuario) => {
        if (!activo) return
        setSesion((previa) => {
          if (!previa) return previa
          const actualizada = { ...previa, usuario }
          guardarSesion(actualizada)
          return actualizada
        })
      })
      .catch((e) => {
        if (activo && e instanceof ErrorApi && e.estado === 401) cerrarSesion()
      })
      .finally(() => {
        if (activo) setVerificando(false)
      })
    return () => {
      activo = false
    }
  }, [cerrarSesion])

  useEffect(() => {
    if (!sesion) return
    const restante = sesion.expiraEn - Date.now()
    if (restante <= 0) {
      cerrarSesion()
      return
    }
    const temporizador = window.setTimeout(cerrarSesion, Math.min(restante, 2_147_483_647))
    return () => window.clearTimeout(temporizador)
  }, [sesion, cerrarSesion])

  useEffect(() => {
    const alExpirar = () => setSesion(null)
    const alCambiarAlmacenamiento = (e: StorageEvent) => {
      if (e.key === CLAVE_SESION) setSesion(leerSesion())
    }
    window.addEventListener(EVENTO_SESION_EXPIRADA, alExpirar)
    window.addEventListener('storage', alCambiarAlmacenamiento)
    return () => {
      window.removeEventListener(EVENTO_SESION_EXPIRADA, alExpirar)
      window.removeEventListener('storage', alCambiarAlmacenamiento)
    }
  }, [])

  const valor = useMemo<ValorAuth>(
    () => ({
      usuario: sesion?.usuario ?? null,
      esAdmin: sesion?.usuario.rol === 'ADMIN',
      verificando,
      iniciarSesion,
      cerrarSesion,
    }),
    [sesion, verificando, iniciarSesion, cerrarSesion],
  )

  return <ContextoAuth.Provider value={valor}>{children}</ContextoAuth.Provider>
}

export const useAuth = (): ValorAuth => {
  const contexto = useContext(ContextoAuth)
  if (!contexto) throw new Error('useAuth debe usarse dentro de ProveedorAuth')
  return contexto
}
