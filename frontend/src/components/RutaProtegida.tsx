// Requisito: Rutas protegidas por sesión y por rol
import type { ReactNode } from 'react'
import { Navigate, useLocation } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import type { Rol } from '../types/Api'
import Boton from './atoms/Boton'

interface RutaProtegidaProps {
  roles?: Rol[]
  children: ReactNode
}

const RutaProtegida = ({ roles, children }: RutaProtegidaProps) => {
  const { usuario, verificando } = useAuth()
  const ubicacion = useLocation()

  if (verificando) return <p>Verificando sesión...</p>
  if (!usuario) {
    return <Navigate to="/login" replace state={{ desde: ubicacion.pathname + ubicacion.search }} />
  }
  if (roles && !roles.includes(usuario.rol)) {
    return (
      <div className="caja text-center">
        <h2>Acceso denegado</h2>
        <p>No tienes permisos para ver esta sección.</p>
        <Boton to="/">Ir al inicio</Boton>
      </div>
    )
  }
  return <>{children}</>
}

export default RutaProtegida
