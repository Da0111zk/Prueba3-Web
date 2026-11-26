// Requisito: Página protegida - cotizaciones del usuario autenticado (GET /api/cotizaciones/mis)
import { useState } from 'react'
import EstadoCarga from '../components/molecules/EstadoCarga'
import Aviso from '../components/molecules/Aviso'
import TablaCotizaciones from '../components/organisms/TablaCotizaciones'
import Boton from '../components/atoms/Boton'
import { useApi } from '../hooks/useApi'
import { mensajeDeError } from '../services/api'
import { cotizacionesApi } from '../services/endpoints'
import type { AvisoEstado, Cotizacion } from '../types/Api'

const MisCotizaciones = () => {
  const { datos, cargando, error, recargar } = useApi(() => cotizacionesApi.mias())
  const [aviso, setAviso] = useState<AvisoEstado | null>(null)

  const eliminar = async (cotizacion: Cotizacion) => {
    if (!window.confirm('¿Eliminar esta cotización?')) return
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
      <h2>Mis cotizaciones</h2>
      <Aviso aviso={aviso} />
      <EstadoCarga cargando={cargando} error={error}>
        <TablaCotizaciones cotizaciones={datos ?? []} onEliminar={eliminar} />
      </EstadoCarga>
      <div className="mt-3">
        <Boton to="/cotiza" variante="rojo">
          Nueva cotización
        </Boton>
      </div>
    </>
  )
}

export default MisCotizaciones
