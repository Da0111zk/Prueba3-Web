// Requisito: Integración frontend-backend - cliente HTTP único que adjunta el JWT y normaliza errores
import { API_URL } from '../config'
import { borrarSesion, leerSesion } from './sesion'

export const EVENTO_SESION_EXPIRADA = 'mi-espacio:sesion-expirada'

export class ErrorApi extends Error {
  estado: number

  constructor(estado: number, mensaje: string) {
    super(mensaje)
    this.name = 'ErrorApi'
    this.estado = estado
  }
}

export const mensajeDeError = (e: unknown): string =>
  e instanceof ErrorApi ? e.message : 'Ocurrió un error inesperado.'

const solicitar = async <T>(ruta: string, metodo: string, cuerpo?: unknown): Promise<T> => {
  const cabeceras: Record<string, string> = {}
  if (cuerpo !== undefined) cabeceras['Content-Type'] = 'application/json'
  const sesion = leerSesion()
  if (sesion) cabeceras.Authorization = `Bearer ${sesion.token}`

  let respuesta: Response
  try {
    respuesta = await fetch(`${API_URL}${ruta}`, {
      method: metodo,
      headers: cabeceras,
      body: cuerpo === undefined ? undefined : JSON.stringify(cuerpo),
    })
  } catch {
    throw new ErrorApi(0, 'No se pudo conectar con el servidor. Verifica que el backend esté en ejecución.')
  }

  if (respuesta.status === 204) return undefined as T
  const datos = await respuesta.json().catch(() => null)

  if (!respuesta.ok) {
    if (respuesta.status === 401 && sesion) {
      borrarSesion()
      window.dispatchEvent(new Event(EVENTO_SESION_EXPIRADA))
    }
    const detalle = datos?.errores ? Object.values(datos.errores as Record<string, string>).join(' ') : ''
    throw new ErrorApi(respuesta.status, detalle || datos?.mensaje || 'Ocurrió un error inesperado.')
  }
  return datos as T
}

export const api = {
  get: <T>(ruta: string) => solicitar<T>(ruta, 'GET'),
  post: <T>(ruta: string, cuerpo: unknown) => solicitar<T>(ruta, 'POST', cuerpo),
  put: <T>(ruta: string, cuerpo: unknown) => solicitar<T>(ruta, 'PUT', cuerpo),
  eliminar: (ruta: string) => solicitar<void>(ruta, 'DELETE'),
}
