// Requisito: Uso de TypeScript - contratos de la API REST
export type Rol = 'ADMIN' | 'CLIENTE'

export interface Usuario {
  id: number
  nombre: string
  email: string
  rol: Rol
}

export interface AuthRespuesta {
  token: string
  expiraEn: number
  usuario: Usuario
}

export type EstadoCotizacion = 'PENDIENTE' | 'ATENDIDA' | 'RECHAZADA'

export interface Cotizacion {
  id: number
  servicioId: number
  servicioNombre: string
  usuarioNombre: string
  usuarioEmail: string
  mensaje: string
  estado: EstadoCotizacion
  creadaEn: string
}

export interface AvisoEstado {
  ok: boolean
  texto: string
}
