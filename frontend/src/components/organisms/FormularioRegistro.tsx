// Requisito: Organismo - registro de cliente contra la API con validación y confirmación de contraseña
import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext'
import { useFormulario } from '../../hooks/useFormulario'
import { mensajeDeError } from '../../services/api'
import { authApi } from '../../services/endpoints'
import type { AvisoEstado } from '../../types/Api'
import { registroInicial, validarCampo, type DatosRegistro } from '../../utils/validaciones'
import Boton from '../atoms/Boton'
import Aviso from '../molecules/Aviso'
import CampoFormulario from '../molecules/CampoFormulario'

const FormularioRegistro = () => {
  const { props, enviar } = useFormulario<DatosRegistro>(registroInicial, validarCampo)
  const { iniciarSesion } = useAuth()
  const navegar = useNavigate()
  const [aviso, setAviso] = useState<AvisoEstado | null>(null)

  const registrar = async (datos: DatosRegistro) => {
    setAviso(null)
    try {
      const respuesta = await authApi.registro({ nombre: datos.nombre, email: datos.email, password: datos.password })
      iniciarSesion(respuesta)
      navegar('/', { replace: true })
    } catch (e) {
      setAviso({ ok: false, texto: mensajeDeError(e) })
      return false
    }
  }

  return (
    <>
      <Aviso aviso={aviso} />
      <form onSubmit={enviar(registrar)} noValidate className="formulario-me">
        <CampoFormulario {...props('nombre')} etiqueta="Nombre completo" placeholder="Tu nombre" />
        <CampoFormulario {...props('email')} etiqueta="Correo electrónico" tipo="email" placeholder="tucorreo@ejemplo.com" />
        <CampoFormulario {...props('password')} etiqueta="Contraseña" tipo="password" placeholder="••••••••" />
        <CampoFormulario {...props('confirmacion')} etiqueta="Confirmar contraseña" tipo="password" placeholder="••••••••" />
        <Boton type="submit" bloque>
          Crear cuenta
        </Boton>
      </form>
    </>
  )
}

export default FormularioRegistro
