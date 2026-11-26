// Requisito: Página Reseñas - lectura pública desde la API; publicar y eliminar según sesión y rol
import { useState } from 'react'
import { Link } from 'react-router-dom'
import Aviso from '../components/molecules/Aviso'
import EstadoCarga from '../components/molecules/EstadoCarga'
import FormularioResena from '../components/organisms/FormularioResena'
import TablaResenas from '../components/organisms/TablaResenas'
import { useAuth } from '../context/AuthContext'
import { useApi } from '../hooks/useApi'
import { mensajeDeError } from '../services/api'
import { resenasApi } from '../services/endpoints'
import type { AvisoEstado } from '../types/Api'
import type { Resena } from '../types/Contenido'

const Resenas = () => {
  const { usuario, esAdmin } = useAuth()
  const { datos, cargando, error, recargar } = useApi(() => resenasApi.listar())
  const [aviso, setAviso] = useState<AvisoEstado | null>(null)

  const puedeEliminar = (resena: Resena) => esAdmin || (usuario !== null && resena.usuarioId === usuario.id)

  const eliminar = async (id: number) => {
    if (!window.confirm('¿Eliminar esta reseña?')) return
    try {
      await resenasApi.eliminar(id)
      setAviso({ ok: true, texto: 'Reseña eliminada.' })
      recargar()
    } catch (e) {
      setAviso({ ok: false, texto: mensajeDeError(e) })
    }
  }

  return (
    <>
      <h2>Reseñas</h2>
      <p>Aquí encontrarás opiniones y comentarios de nuestros clientes sobre su experiencia con Mi Espacio.</p>
      <Aviso aviso={aviso} />
      <EstadoCarga cargando={cargando} error={error}>
        <TablaResenas resenas={datos ?? []} puedeEliminar={puedeEliminar} onEliminar={eliminar} />
      </EstadoCarga>
      {usuario ? (
        <FormularioResena onPublicada={recargar} />
      ) : (
        <p className="mt-4">
          <Link to="/login" state={{ desde: '/resenas' }} className="enlace-plano fw-bold">
            Inicia sesión
          </Link>{' '}
          para dejar tu reseña.
        </p>
      )}
    </>
  )
}

export default Resenas
