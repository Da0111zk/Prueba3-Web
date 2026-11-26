// Requisito: Organismo - CRUD de servicios desde el frontend (rol ADMIN)
import { useState } from 'react'
import { useApi } from '../../hooks/useApi'
import { mensajeDeError } from '../../services/api'
import { serviciosApi } from '../../services/endpoints'
import type { AvisoEstado } from '../../types/Api'
import type { Servicio } from '../../types/Servicio'
import { servicioInicial, type DatosServicio } from '../../utils/validaciones'
import Boton from '../atoms/Boton'
import Aviso from '../molecules/Aviso'
import EstadoCarga from '../molecules/EstadoCarga'
import FormularioServicio from './FormularioServicio'

const AdminServicios = () => {
  const { datos, cargando, error, recargar } = useApi(() => serviciosApi.listar())
  const [editando, setEditando] = useState<Servicio | null>(null)
  const [aviso, setAviso] = useState<AvisoEstado | null>(null)

  const inicial: DatosServicio = editando
    ? {
        nombre: editando.nombre,
        descripcion: editando.descripcion,
        categoria: editando.categoria,
        tamano: editando.tamano,
        imagen: editando.imagen ?? '',
      }
    : servicioInicial

  const guardar = async (formulario: DatosServicio): Promise<boolean> => {
    const cuerpo = { ...formulario, imagen: formulario.imagen.trim() }
    try {
      if (editando) {
        await serviciosApi.actualizar(editando.id, cuerpo)
      } else {
        await serviciosApi.crear(cuerpo)
      }
      setAviso({ ok: true, texto: editando ? 'Servicio actualizado.' : 'Servicio creado.' })
      setEditando(null)
      recargar()
      return true
    } catch (e) {
      setAviso({ ok: false, texto: mensajeDeError(e) })
      return false
    }
  }

  const eliminar = async (servicio: Servicio) => {
    if (!window.confirm(`¿Eliminar el servicio "${servicio.nombre}"?`)) return
    try {
      await serviciosApi.eliminar(servicio.id)
      setAviso({ ok: true, texto: 'Servicio eliminado.' })
      if (editando?.id === servicio.id) setEditando(null)
      recargar()
    } catch (e) {
      setAviso({ ok: false, texto: mensajeDeError(e) })
    }
  }

  return (
    <>
      <Aviso aviso={aviso} />
      <FormularioServicio
        key={editando ? editando.id : 'nuevo'}
        inicial={inicial}
        modoEdicion={editando !== null}
        onGuardar={guardar}
        onCancelar={() => setEditando(null)}
      />
      <EstadoCarga cargando={cargando} error={error}>
        <div className="table-responsive mt-4">
          <table className="tabla-me">
            <thead>
              <tr>
                <th>Nombre</th>
                <th>Categoría</th>
                <th>Tamaño</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {(datos ?? []).map((s) => (
                <tr key={s.id}>
                  <td>{s.nombre}</td>
                  <td>{s.categoria}</td>
                  <td>{s.tamano}</td>
                  <td>
                    <div className="acciones-tabla">
                      <Boton chico onClick={() => setEditando(s)}>
                        Editar
                      </Boton>
                      <Boton chico variante="rojo" onClick={() => eliminar(s)}>
                        Eliminar
                      </Boton>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </EstadoCarga>
    </>
  )
}

export default AdminServicios
