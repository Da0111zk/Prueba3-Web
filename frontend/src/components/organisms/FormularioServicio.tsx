// Requisito: Organismo - alta y edición de servicios (panel de administración)
import { useFormulario } from '../../hooks/useFormulario'
import { validarCampo, type DatosServicio } from '../../utils/validaciones'
import Boton from '../atoms/Boton'
import CampoFormulario from '../molecules/CampoFormulario'

interface FormularioServicioProps {
  inicial: DatosServicio
  modoEdicion: boolean
  onGuardar: (datos: DatosServicio) => Promise<boolean>
  onCancelar: () => void
}

const FormularioServicio = ({ inicial, modoEdicion, onGuardar, onCancelar }: FormularioServicioProps) => {
  const { props, enviar } = useFormulario<DatosServicio>(inicial, validarCampo)

  return (
    <form onSubmit={enviar(onGuardar)} noValidate className="formulario-me">
      <h3>{modoEdicion ? 'Editar servicio' : 'Nuevo servicio'}</h3>
      <CampoFormulario {...props('nombre')} etiqueta="Nombre" />
      <CampoFormulario {...props('descripcion')} etiqueta="Descripción" tipo="textarea" />
      <CampoFormulario {...props('categoria')} etiqueta="Categoría" />
      <CampoFormulario {...props('tamano')} etiqueta="Tamaño" placeholder="Ej: 1.5mt x 1.5mt" />
      <CampoFormulario {...props('imagen')} etiqueta="Imagen (opcional)" placeholder="/img/locker.jpg" />
      <Boton type="submit" bloque>
        {modoEdicion ? 'Guardar cambios' : 'Crear servicio'}
      </Boton>
      {modoEdicion && (
        <div className="mt-2">
          <Boton variante="rojo" bloque onClick={onCancelar}>
            Cancelar edición
          </Boton>
        </div>
      )}
    </form>
  )
}

export default FormularioServicio
