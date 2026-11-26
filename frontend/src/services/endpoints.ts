// Requisito: Integración frontend-backend - un módulo por recurso de la API REST
import type { AuthRespuesta, Cotizacion, Usuario } from '../types/Api'
import type { Resena, Sucursal } from '../types/Contenido'
import type { Servicio } from '../types/Servicio'
import { api } from './api'

export type PeticionServicio = Omit<Servicio, 'id'>

export const authApi = {
  login: (datos: { email: string; password: string }) => api.post<AuthRespuesta>('/api/auth/login', datos),
  registro: (datos: { nombre: string; email: string; password: string }) =>
    api.post<AuthRespuesta>('/api/auth/registro', datos),
  perfil: () => api.get<Usuario>('/api/usuarios/me'),
}

export const serviciosApi = {
  listar: () => api.get<Servicio[]>('/api/servicios'),
  obtener: (id: number) => api.get<Servicio>(`/api/servicios/${id}`),
  crear: (datos: PeticionServicio) => api.post<Servicio>('/api/servicios', datos),
  actualizar: (id: number, datos: PeticionServicio) => api.put<Servicio>(`/api/servicios/${id}`, datos),
  eliminar: (id: number) => api.eliminar(`/api/servicios/${id}`),
}

export const sucursalesApi = {
  listar: () => api.get<Sucursal[]>('/api/sucursales'),
}

export const resenasApi = {
  listar: () => api.get<Resena[]>('/api/resenas'),
  crear: (datos: { texto: string; evaluacion: number }) => api.post<Resena>('/api/resenas', datos),
  eliminar: (id: number) => api.eliminar(`/api/resenas/${id}`),
}

export const cotizacionesApi = {
  crear: (datos: { servicioId: number; mensaje: string }) => api.post<Cotizacion>('/api/cotizaciones', datos),
  mias: () => api.get<Cotizacion[]>('/api/cotizaciones/mis'),
  todas: () => api.get<Cotizacion[]>('/api/cotizaciones'),
  actualizarEstado: (id: number, estado: 'ATENDIDA' | 'RECHAZADA') =>
    api.put<Cotizacion>(`/api/cotizaciones/${id}`, { estado }),
  eliminar: (id: number) => api.eliminar(`/api/cotizaciones/${id}`),
}
