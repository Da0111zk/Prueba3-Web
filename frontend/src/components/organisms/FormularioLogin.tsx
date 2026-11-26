// Requisito: Organismo - inicio de sesión contra la API (JWT) con validación
import { useState } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext'
import { useFormulario } from '../../hooks/useFormulario'
import { mensajeDeError } from '../../services/api'
import { authApi } from '../../services/endpoints'
import type { AvisoEstado } from '../../types/Api'
import { loginInicial, validarCampo, type DatosLogin } from '../../utils/validaciones'
import Boton from '../atoms/Boton'
import Aviso from '../molecules/Aviso'
import CampoFormulario from '../molecules/CampoFormulario'

const FormularioLogin = () => {
  const { props, enviar } = useFormulario<DatosLogin>(loginInicial, validarCampo)
  const { iniciarSesion } = useAuth()
  const navegar = useNavigate()
  const ubicacion = useLocation()
  const [aviso, setAviso] = useState<AvisoEstado | null>(null)

  const ingresar = async (datos: DatosLogin) => {
    setAviso(null)
    try {
      const respuesta = await authApi.login(datos)
      iniciarSesion(respuesta)
      const destino = (ubicacion.state as { desde?: string } | null)?.desde ?? '/'
      navegar(destino, { replace: true })
    } catch (e) {
      setAviso({ ok: false, texto: mensajeDeError(e) })
      return false
    }
  }

  return (
    <>
      <Aviso aviso={aviso} />
      <form onSubmit={enviar(ingresar)} noValidate className="formulario-me">
        <CampoFormulario {...props('email')} etiqueta="Correo electrónico" tipo="email" placeholder="tucorreo@ejemplo.com" />
        <CampoFormulario {...props('password')} etiqueta="Contraseña" tipo="password" placeholder="••••••••" />
        <Boton type="submit" bloque>
          Ingresar
        </Boton>
      </form>
    </>
  )
}

export default FormularioLogin
