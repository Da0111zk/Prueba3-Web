// Requisito: Molécula - estados de carga y error al consumir la API
import type { ReactNode } from 'react'
import type { ErrorApi } from '../../services/api'

interface EstadoCargaProps {
  cargando: boolean
  error: ErrorApi | null
  children: ReactNode
}

const EstadoCarga = ({ cargando, error, children }: EstadoCargaProps) => {
  if (cargando) return <p>Cargando...</p>
  if (error) {
    return (
      <div className="alerta-error" role="alert">
        {error.message}
      </div>
    )
  }
  return <>{children}</>
}

export default EstadoCarga
