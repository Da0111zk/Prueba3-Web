// Requisito: Organismo - tabla de cotizaciones reutilizada por el cliente y por el administrador
import type { Cotizacion } from '../../types/Api'
import Boton from '../atoms/Boton'

interface TablaCotizacionesProps {
  cotizaciones: Cotizacion[]
  admin?: boolean
  onEliminar: (cotizacion: Cotizacion) => void
  onCambiarEstado?: (cotizacion: Cotizacion, estado: 'ATENDIDA' | 'RECHAZADA') => void
}

const TablaCotizaciones = ({ cotizaciones, admin, onEliminar, onCambiarEstado }: TablaCotizacionesProps) => {
  if (cotizaciones.length === 0) return <p>No hay cotizaciones para mostrar.</p>
  return (
    <div className="table-responsive mt-3">
      <table className="tabla-me">
        <thead>
          <tr>
            <th>Servicio</th>
            {admin && <th>Cliente</th>}
            <th>Mensaje</th>
            <th>Estado</th>
            <th>Fecha</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {cotizaciones.map((c) => (
            <tr key={c.id}>
              <td>{c.servicioNombre}</td>
              {admin && (
                <td>
                  {c.usuarioNombre}
                  <br />
                  <small>{c.usuarioEmail}</small>
                </td>
              )}
              <td>{c.mensaje}</td>
              <td>{c.estado}</td>
              <td>{new Date(c.creadaEn).toLocaleDateString('es-CL')}</td>
              <td>
                <div className="acciones-tabla">
                  {admin && c.estado === 'PENDIENTE' && onCambiarEstado && (
                    <>
                      <Boton chico onClick={() => onCambiarEstado(c, 'ATENDIDA')}>
                        Atender
                      </Boton>
                      <Boton chico variante="rojo" onClick={() => onCambiarEstado(c, 'RECHAZADA')}>
                        Rechazar
                      </Boton>
                    </>
                  )}
                  {(admin || c.estado === 'PENDIENTE') && (
                    <Boton chico variante="rojo" onClick={() => onEliminar(c)}>
                      Eliminar
                    </Boton>
                  )}
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}

export default TablaCotizaciones
