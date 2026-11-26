// Requisito: Organismo - encabezado con navegación y estado de sesión
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext'
import Boton from '../atoms/Boton'
import NavItem from '../molecules/NavItem'

const BarraNavegacion = () => {
  const { usuario, esAdmin, cerrarSesion } = useAuth()
  const navegar = useNavigate()

  const salir = () => {
    cerrarSesion()
    navegar('/')
  }

  return (
    <header className="encabezado">
      <Link to="/">
        <img src="/img/logo.png" alt="Logo de Mi Espacio" className="logo" />
      </Link>
      <h1>Bienvenido a Mi Espacio</h1>
      <p className="mb-2">Nuestro espacio, tu bodega.</p>
      <nav>
        <ul>
          <NavItem to="/" texto="Inicio" />
          <NavItem to="/acerca" texto="Acerca de" />
          <NavItem to="/contacto" texto="Contacto" />
          <NavItem to="/servicios" texto="Servicios" />
          <NavItem to="/cotiza" texto="Cotiza Aquí" />
          <NavItem to="/faq" texto="Preguntas Frecuentes" />
          <NavItem to="/resenas" texto="Reseñas" />
          <NavItem to="/sucursales" texto="Sucursales" />
          {usuario && <NavItem to="/mis-cotizaciones" texto="Mis cotizaciones" />}
          {esAdmin && <NavItem to="/admin" texto="Administración" />}
        </ul>
      </nav>
      <div className="botones-auth">
        {usuario ? (
          <>
            <span className="saludo-sesion">Hola, {usuario.nombre}</span>
            <Boton variante="rojo" onClick={salir}>
              Cerrar sesión
            </Boton>
          </>
        ) : (
          <>
            <Boton to="/login">Iniciar Sesión</Boton>
            <Boton to="/registro" variante="rojo">
              Registrarse
            </Boton>
          </>
        )}
      </div>
    </header>
  )
}

export default BarraNavegacion
