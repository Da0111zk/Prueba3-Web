// Requisito: Molécula - mensaje de éxito o error de una operación contra la API
import type { AvisoEstado } from '../../types/Api'

interface AvisoProps {
  aviso: AvisoEstado | null
}

const Aviso = ({ aviso }: AvisoProps) => {
  if (!aviso) return null
  return (
    <div className={aviso.ok ? 'exito' : 'alerta-error'} role="alert">
      {aviso.texto}
    </div>
  )
}

export default Aviso
