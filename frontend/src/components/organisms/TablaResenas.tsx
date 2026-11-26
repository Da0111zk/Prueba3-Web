// Requisito: Organismo - tabla de reseñas con acciones según permisos
import type { Resena } from '../../types/Contenido'
import Boton from '../atoms/Boton'

interface TablaResenasProps {
  resenas: Resena[]
  puedeEliminar: (resena: Resena) => boolean
  onEliminar: (id: number) => void
}

const TablaResenas = ({ resenas, puedeEliminar, onEliminar }: TablaResenasProps) => (
  <div className="table-responsive mt-4">
    <table className="tabla-me">
      <thead>
        <tr>
          <th>Nombre</th>
          <th>Reseña</th>
          <th>Evaluación</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        {resenas.map((r) => (
          <tr key={r.id}>
            <td>{r.autor}</td>
            <td>{r.texto}</td>
            <td>{'⭐'.repeat(r.evaluacion)}</td>
            <td>
              {puedeEliminar(r) && (
                <Boton variante="rojo" chico onClick={() => onEliminar(r.id)}>
                  Eliminar
                </Boton>
              )}
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  </div>
)

export default TablaResenas
