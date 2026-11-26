// Requisito: Hook reutilizable - carga de datos desde la API con estados de carga y error
import { useEffect, useState } from 'react'
import { ErrorApi } from '../services/api'

export const useApi = <T>(funcion: () => Promise<T>, dependencias: unknown[] = []) => {
  const [datos, setDatos] = useState<T | null>(null)
  const [cargando, setCargando] = useState(true)
  const [error, setError] = useState<ErrorApi | null>(null)
  const [version, setVersion] = useState(0)

  useEffect(() => {
    let activo = true
    setCargando(true)
    setError(null)
    funcion()
      .then((resultado) => {
        if (activo) setDatos(resultado)
      })
      .catch((e) => {
        if (activo) setError(e instanceof ErrorApi ? e : new ErrorApi(0, 'Ocurrió un error inesperado.'))
      })
      .finally(() => {
        if (activo) setCargando(false)
      })
    return () => {
      activo = false
    }
  }, [...dependencias, version])

  return { datos, cargando, error, recargar: () => setVersion((v) => v + 1) }
}
