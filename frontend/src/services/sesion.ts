// Requisito: Gestión de sesión en el frontend - persistencia del token y del usuario con control de expiración
import type { Usuario } from '../types/Api'

export const CLAVE_SESION = 'mi-espacio.sesion'

export interface Sesion {
  token: string
  expiraEn: number
  usuario: Usuario
}

export const leerSesion = (): Sesion | null => {
  try {
    const crudo = localStorage.getItem(CLAVE_SESION)
    if (!crudo) return null
    const sesion = JSON.parse(crudo) as Sesion
    if (!sesion.token || !sesion.usuario || sesion.expiraEn <= Date.now()) {
      localStorage.removeItem(CLAVE_SESION)
      return null
    }
    return sesion
  } catch {
    localStorage.removeItem(CLAVE_SESION)
    return null
  }
}

export const guardarSesion = (sesion: Sesion): void => {
  localStorage.setItem(CLAVE_SESION, JSON.stringify(sesion))
}

export const borrarSesion = (): void => {
  localStorage.removeItem(CLAVE_SESION)
}
