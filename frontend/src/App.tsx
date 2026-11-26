// Requisito: React Router - rutas públicas, dinámica y protegidas por sesión y rol
import { Route, Routes } from 'react-router-dom'
import RutaProtegida from './components/RutaProtegida'
import BarraNavegacion from './components/organisms/BarraNavegacion'
import PiePagina from './components/organisms/PiePagina'
import Acerca from './pages/Acerca'
import Admin from './pages/Admin'
import Contacto from './pages/Contacto'
import Cotiza from './pages/Cotiza'
import DetalleServicio from './pages/DetalleServicio'
import Faq from './pages/Faq'
import Inicio from './pages/Inicio'
import Login from './pages/Login'
import MisCotizaciones from './pages/MisCotizaciones'
import NoEncontrada from './pages/NoEncontrada'
import Registro from './pages/Registro'
import Resenas from './pages/Resenas'
import Servicios from './pages/Servicios'
import Sucursales from './pages/Sucursales'

const App = () => (
  <>
    <BarraNavegacion />
    <main className="contenido">
      <Routes>
        <Route path="/" element={<Inicio />} />
        <Route path="/acerca" element={<Acerca />} />
        <Route path="/contacto" element={<Contacto />} />
        <Route path="/servicios" element={<Servicios />} />
        <Route path="/servicios/:id" element={<DetalleServicio />} />
        <Route
          path="/cotiza"
          element={
            <RutaProtegida>
              <Cotiza />
            </RutaProtegida>
          }
        />
        <Route
          path="/mis-cotizaciones"
          element={
            <RutaProtegida>
              <MisCotizaciones />
            </RutaProtegida>
          }
        />
        <Route
          path="/admin"
          element={
            <RutaProtegida roles={['ADMIN']}>
              <Admin />
            </RutaProtegida>
          }
        />
        <Route path="/faq" element={<Faq />} />
        <Route path="/resenas" element={<Resenas />} />
        <Route path="/sucursales" element={<Sucursales />} />
        <Route path="/login" element={<Login />} />
        <Route path="/registro" element={<Registro />} />
        <Route path="*" element={<NoEncontrada />} />
      </Routes>
    </main>
    <PiePagina />
  </>
)

export default App
