// Requisito: Organismo - gestión de cotizaciones desde el frontend (rol ADMIN)
import { useState } from 'react'
import { useApi } from '../../hooks/useApi'
import { mensajeDeError } from '../../services/api'
import { cotizacionesApi } from '../../services/endpoints'
import type { AvisoEstado, Cotizacion } from '../../types/Api'
import Aviso from '../molecules/Aviso'
import EstadoCarga from '../molecules/EstadoCarga'
import TablaCotizaciones from './TablaCotizaciones'

const AdminCotizaciones = () => {
  const { datos, cargando, error, recargar } = useApi(() => cotizacionesApi.todas())
  const [aviso, setAviso] = useState<AvisoEstado | null>(null)

  const cambiarEstado = async (cotizacion: Cotizacion, estado: 'ATENDIDA' | 'RECHAZADA') => {
    try {
      await cotizacionesApi.actualizarEstado(cotizacion.id, estado)
      setAviso({ ok: true, texto: `Cotización #${cotizacion.id} marcada como ${estado.toLowerCase()}.` })
      recargar()
    } catch (e) {
      setAviso({ ok: false, texto: mensajeDeError(e) })
    }
  }

  const eliminar = async (cotizacion: Cotizacion) => {
    if (!window.confirm(`¿Eliminar la cotización #${cotizacion.id}?`)) return
    try {
      await cotizacionesApi.eliminar(cotizacion.id)
      setAviso({ ok: true, texto: 'Cotización eliminada.' })
      recargar()
    } catch (e) {
      setAviso({ ok: false, texto: mensajeDeError(e) })
    }
  }

  return (
    <>
      <Aviso aviso={aviso} />
      <EstadoCarga cargando={cargando} error={error}>
        <TablaCotizaciones cotizaciones={datos ?? []} admin onEliminar={eliminar} onCambiarEstado={cambiarEstado} />
      </EstadoCarga>
    </>
  )
}

export default AdminCotizaciones
