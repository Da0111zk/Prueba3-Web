// Requisito: Hook reutilizable - estado, validación y envío de formularios
import { useState, type FormEvent } from 'react'

type Validador<T> = (campo: string, valor: string, datos: T) => string
type AlExito<T> = (datos: T) => void | boolean | Promise<void | boolean>

export const useFormulario = <T extends Record<string, string>>(inicial: T, validar: Validador<T>) => {
  const [datos, setDatos] = useState<T>(inicial)
  const [tocados, setTocados] = useState<Record<string, boolean>>({})

  const errores: Record<string, string> = {}
  Object.keys(datos).forEach((campo) => {
    const mensaje = validar(campo, datos[campo], datos)
    if (mensaje) errores[campo] = mensaje
  })

  const props = (id: string) => ({
    id,
    valor: datos[id],
    error: errores[id],
    tocado: !!tocados[id],
    onCambio: (valor: string) => setDatos((previo) => ({ ...previo, [id]: valor }) as T),
    onBlur: () => setTocados((previo) => ({ ...previo, [id]: true })),
  })

  const enviar = (alExito: AlExito<T>) => async (e: FormEvent) => {
    e.preventDefault()
    const todos: Record<string, boolean> = {}
    Object.keys(datos).forEach((campo) => {
      todos[campo] = true
    })
    setTocados(todos)
    if (Object.keys(errores).length === 0) {
      const resultado = await alExito(datos)
      if (resultado !== false) {
        setDatos(inicial)
        setTocados({})
      }
    }
  }

  return { props, enviar }
}
