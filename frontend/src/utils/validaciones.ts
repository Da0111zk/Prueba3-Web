// Requisito: Formularios con validación - reglas por campo
export type DatosCotizacion = { servicioId: string; mensaje: string }
export type DatosContacto = { nombre: string; email: string; mensaje: string }
export type DatosLogin = { email: string; password: string }
export type DatosRegistro = { nombre: string; email: string; password: string; confirmacion: string }
export type DatosServicio = { nombre: string; descripcion: string; categoria: string; tamano: string; imagen: string }
export type DatosResena = { evaluacion: string; texto: string }

export const cotizacionInicial: DatosCotizacion = { servicioId: '', mensaje: '' }
export const contactoInicial: DatosContacto = { nombre: '', email: '', mensaje: '' }
export const loginInicial: DatosLogin = { email: '', password: '' }
export const registroInicial: DatosRegistro = { nombre: '', email: '', password: '', confirmacion: '' }
export const servicioInicial: DatosServicio = { nombre: '', descripcion: '', categoria: '', tamano: '', imagen: '' }
export const resenaInicial: DatosResena = { evaluacion: '', texto: '' }

const regexEmail = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/

const requerido = (texto: string, etiqueta: string, max: number): string => {
  if (!texto) return `${etiqueta} es obligatorio.`
  if (texto.length > max) return `${etiqueta} no puede superar ${max} caracteres.`
  return ''
}

export const validarCampo = (campo: string, valor: string, datos: Record<string, string>): string => {
  const texto = valor.trim()
  switch (campo) {
    case 'nombre':
      if (!texto) return 'El nombre es obligatorio.'
      if (texto.length < 3) return 'El nombre debe tener al menos 3 caracteres.'
      if (texto.length > 100) return 'El nombre no puede superar 100 caracteres.'
      return ''
    case 'email':
      if (!texto) return 'El correo es obligatorio.'
      if (!regexEmail.test(texto)) return 'Ingresa un correo válido.'
      return ''
    case 'servicioId':
      return texto ? '' : 'Selecciona un servicio.'
    case 'mensaje':
      if (!texto) return 'El mensaje es obligatorio.'
      if (texto.length < 10) return 'El mensaje debe tener al menos 10 caracteres.'
      if (texto.length > 200) return 'El mensaje no puede superar los 200 caracteres.'
      return ''
    case 'password':
      if (!valor) return 'La contraseña es obligatoria.'
      if (valor.length < 6) return 'La contraseña debe tener al menos 6 caracteres.'
      if (valor.length > 72) return 'La contraseña no puede superar 72 caracteres.'
      return ''
    case 'confirmacion':
      if (!valor) return 'Confirma tu contraseña.'
      if (valor !== datos.password) return 'Las contraseñas no coinciden.'
      return ''
    case 'descripcion':
      return requerido(texto, 'La descripción', 500)
    case 'categoria':
      return requerido(texto, 'La categoría', 100)
    case 'tamano':
      return requerido(texto, 'El tamaño', 50)
    case 'imagen':
      return texto.length > 200 ? 'La ruta de la imagen no puede superar 200 caracteres.' : ''
    case 'evaluacion': {
      const n = Number(texto)
      return Number.isInteger(n) && n >= 1 && n <= 5 ? '' : 'Elige una evaluación entre 1 y 5.'
    }
    case 'texto':
      if (!texto) return 'La reseña es obligatoria.'
      if (texto.length < 10) return 'La reseña debe tener al menos 10 caracteres.'
      if (texto.length > 500) return 'La reseña no puede superar los 500 caracteres.'
      return ''
    default:
      return ''
  }
}
