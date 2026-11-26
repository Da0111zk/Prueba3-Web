// Requisito: Página Sucursales - datos cargados desde la API
import EstadoCarga from '../components/molecules/EstadoCarga'
import ListaSucursales from '../components/organisms/ListaSucursales'
import { useApi } from '../hooks/useApi'
import { sucursalesApi } from '../services/endpoints'

const Sucursales = () => {
  const { datos, cargando, error } = useApi(() => sucursalesApi.listar())
  return (
    <>
      <h2>Nuestras Sucursales</h2>
      <p>Encuentra la ubicación más cercana a ti con acceso seguro las 24 horas.</p>
      <EstadoCarga cargando={cargando} error={error}>
        <ListaSucursales sucursales={datos ?? []} />
      </EstadoCarga>
    </>
  )
}

export default Sucursales
