// Requisito: Página protegida por rol ADMIN - administración de servicios y cotizaciones
import { useState } from 'react'
import Boton from '../components/atoms/Boton'
import AdminCotizaciones from '../components/organisms/AdminCotizaciones'
import AdminServicios from '../components/organisms/AdminServicios'

type Pestana = 'servicios' | 'cotizaciones'

const Admin = () => {
  const [pestana, setPestana] = useState<Pestana>('servicios')
  return (
    <>
      <h2>Administración</h2>
      <div className="d-flex justify-content-center gap-2 my-3">
        <Boton variante={pestana === 'servicios' ? 'rojo' : 'primario'} onClick={() => setPestana('servicios')}>
          Servicios
        </Boton>
        <Boton variante={pestana === 'cotizaciones' ? 'rojo' : 'primario'} onClick={() => setPestana('cotizaciones')}>
          Cotizaciones
        </Boton>
      </div>
      {pestana === 'servicios' ? <AdminServicios /> : <AdminCotizaciones />}
    </>
  )
}

export default Admin
