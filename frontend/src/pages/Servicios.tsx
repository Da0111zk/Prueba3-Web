// Requisito: Página Servicios - catálogo cargado desde la API
import CatalogoServicios from '../components/organisms/CatalogoServicios'
import EstadoCarga from '../components/molecules/EstadoCarga'
import { useApi } from '../hooks/useApi'
import { serviciosApi } from '../services/endpoints'

const Servicios = () => {
  const { datos, cargando, error } = useApi(() => serviciosApi.listar())
  return (
    <>
      <h2>Nuestros servicios</h2>
      <div className="container-xl mt-4">
        <EstadoCarga cargando={cargando} error={error}>
          <CatalogoServicios servicios={datos ?? []} />
        </EstadoCarga>
      </div>
    </>
  )
}

export default Servicios
