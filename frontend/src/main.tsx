// Requisito: Punto de entrada React + Vite + Bootstrap + BrowserRouter + proveedor de sesión
import 'bootstrap/dist/css/bootstrap.min.css'
import './styles/mi-espacio.css'
import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'
import App from './App'
import { ProveedorAuth } from './context/AuthContext'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <BrowserRouter>
      <ProveedorAuth>
        <App />
      </ProveedorAuth>
    </BrowserRouter>
  </StrictMode>,
)
