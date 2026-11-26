// Requisito: Organismo - solicitud de cotización enviada a la API (requiere sesión)
import { useState } from 'react'
import { Link, useSearchParams } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext'
import { useApi } from '../../hooks/useApi'
import { useFormulario } from '../../hooks/useFormulario'
import { mensajeDeError } from '../../services/api'
import { cotizacionesApi, serviciosApi } from '../../services/endpoints'
import type { AvisoEstado } from '../../types/Api'
import { cotizacionInicial, validarCampo, type DatosCotizacion } from '../../utils/validaciones'
import Boton from '../atoms/Boton'
import Aviso from '../molecules/Aviso'
import CampoFormulario from '../molecules/CampoFormulario'
import EstadoCarga from '../molecules/EstadoCarga'

const FormularioCotizacion = () => {
  const [params] = useSearchParams()
  const { usuario } = useAuth()
  const { datos: servicios, cargando, error } = useApi(() => serviciosApi.listar())
  const idParam = params.get('servicio') ?? ''
  const preseleccion = /^\d+$/.test(idParam) ? idParam : ''
  const { props, enviar } = useFormulario<DatosCotizacion>({ ...cotizacionInicial, servicioId: preseleccion }, validarCampo)
  const [aviso, setAviso] = useState<AvisoEstado | null>(null)

  const opciones = (servicios ?? []).map((s) => ({ valor: String(s.id), texto: `${s.nombre} (${s.tamano})` }))

  const solicitar = async (datos: DatosCotizacion) => {
    setAviso(null)
    try {
      const cotizacion = await cotizacionesApi.crear({ servicioId: Number(datos.servicioId), mensaje: datos.mensaje })
      setAviso({ ok: true, texto: `Cotización enviada para ${cotizacion.servicioNombre}. Estado: ${cotizacion.estado}.` })
    } catch (e) {
      setAviso({ ok: false, texto: mensajeDeError(e) })
      return false
    }
  }

  return (
    <EstadoCarga cargando={cargando} error={error}>
      <Aviso aviso={aviso} />
      <form onSubmit={enviar(solicitar)} noValidate className="formulario-me">
        <p className="mb-3">
          Cotizando como <strong>{usuario?.nombre}</strong> ({usuario?.email})
        </p>
        <CampoFormulario {...props('servicioId')} etiqueta="Servicio" tipo="select" opciones={opciones} />
        <CampoFormulario {...props('mensaje')} etiqueta="Escriba su mensaje" tipo="textarea" placeholder="Cuéntanos qué necesitas almacenar" />
        <Boton type="submit" bloque>
          Solicitar cotización
        </Boton>
        <p className="mt-3 text-center">
          <Link to="/mis-cotizaciones" className="enlace-plano fw-bold">
            Ver mis cotizaciones
          </Link>
        </p>
      </form>
    </EstadoCarga>
  )
}

export default FormularioCotizacion
