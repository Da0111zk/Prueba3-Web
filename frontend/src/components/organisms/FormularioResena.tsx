// Requisito: Organismo - publicación de reseñas contra la API (requiere sesión)
import { useState } from 'react'
import { useFormulario } from '../../hooks/useFormulario'
import { mensajeDeError } from '../../services/api'
import { resenasApi } from '../../services/endpoints'
import type { AvisoEstado } from '../../types/Api'
import { resenaInicial, validarCampo, type DatosResena } from '../../utils/validaciones'
import Boton from '../atoms/Boton'
import Aviso from '../molecules/Aviso'
import CampoFormulario from '../molecules/CampoFormulario'

interface FormularioResenaProps {
  onPublicada: () => void
}

const opcionesEvaluacion = [1, 2, 3, 4, 5].map((n) => ({ valor: String(n), texto: '⭐'.repeat(n) }))

const FormularioResena = ({ onPublicada }: FormularioResenaProps) => {
  const { props, enviar } = useFormulario<DatosResena>(resenaInicial, validarCampo)
  const [aviso, setAviso] = useState<AvisoEstado | null>(null)

  const publicar = async (datos: DatosResena) => {
    setAviso(null)
    try {
      await resenasApi.crear({ texto: datos.texto, evaluacion: Number(datos.evaluacion) })
      setAviso({ ok: true, texto: 'Gracias por tu reseña.' })
      onPublicada()
    } catch (e) {
      setAviso({ ok: false, texto: mensajeDeError(e) })
      return false
    }
  }

  return (
    <>
      <Aviso aviso={aviso} />
      <form onSubmit={enviar(publicar)} noValidate className="formulario-me">
        <h3>Deja tu reseña</h3>
        <CampoFormulario {...props('evaluacion')} etiqueta="Evaluación" tipo="select" opciones={opcionesEvaluacion} />
        <CampoFormulario {...props('texto')} etiqueta="Tu opinión" tipo="textarea" />
        <Boton type="submit" bloque>
          Publicar reseña
        </Boton>
      </form>
    </>
  )
}

export default FormularioResena
