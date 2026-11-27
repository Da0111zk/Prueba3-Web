# Integración frontend-backend y cumplimiento de la rúbrica

Evaluación Parcial 3 · DSY1104 Desarrollo Fullstack II

## IE3.1.1 Backend con base de datos, lógica de negocio y modelos

**Modelo de datos** (`backend/src/main/java/cl/miespacio/model`): `Usuario`, `Servicio`, `Sucursal`, `Resena` y `Cotizacion`, mapeados con JPA a las tablas `usuarios`, `servicios`, `sucursales`, `resenas` y `cotizaciones`. Una cotización relaciona a un usuario con un servicio; una reseña pertenece a un usuario (las de ejemplo no tienen dueño).

**Conexión a la base de datos:** H2 en memoria por defecto y Oracle con el perfil `oracle`. Las credenciales llegan por variables de entorno (`DB_URL`, `DB_USER`, `DB_PASSWORD`). Hibernate crea las tablas (`ddl-auto=update`) y `DatosIniciales` carga el catálogo de Mi Espacio.

**Lógica de negocio** (capa `service`):

- Un correo no puede registrarse dos veces (409).
- Las contraseñas se guardan con BCrypt; nunca en texto plano.
- Un usuario no puede tener dos cotizaciones PENDIENTES para el mismo servicio (409).
- Una cotización solo puede modificarse mientras está PENDIENTE. El cliente edita el mensaje; el administrador la marca ATENDIDA o RECHAZADA.
- Un cliente solo ve y elimina sus propias cotizaciones y solo si están pendientes.
- Cada usuario publica una sola reseña; solo su autor o un ADMIN la edita o elimina.
- No se puede eliminar un servicio con cotizaciones asociadas ni la propia cuenta de administrador.
- Validación de entrada con Bean Validation y errores 400 con el detalle por campo.

## IE3.2.1 API REST con Spring Boot y Swagger

Swagger UI: `http://localhost:8080/swagger-ui.html` (JSON en `/v3/api-docs`). Cada endpoint tiene resumen y el botón **Authorize** acepta el token JWT.

| Recurso | Método y ruta | Acceso |
| --- | --- | --- |
| Auth | `POST /api/auth/registro` · `POST /api/auth/login` | Público |
| Servicios | `GET /api/servicios` · `GET /api/servicios/{id}` | Público |
| | `POST /api/servicios` · `PUT /api/servicios/{id}` · `DELETE /api/servicios/{id}` | ADMIN |
| Sucursales | `GET /api/sucursales` · `GET /api/sucursales/{id}` | Público |
| | `POST` · `PUT /{id}` · `DELETE /{id}` | ADMIN |
| Reseñas | `GET /api/resenas` · `GET /api/resenas/{id}` | Público |
| | `POST /api/resenas` | Sesión |
| | `PUT /api/resenas/{id}` · `DELETE /api/resenas/{id}` | Autor o ADMIN |
| Cotizaciones | `GET /api/cotizaciones` | ADMIN |
| | `GET /api/cotizaciones/mis` · `POST /api/cotizaciones` | Sesión |
| | `GET /{id}` · `PUT /{id}` · `DELETE /{id}` | Dueño o ADMIN (reglas de negocio arriba) |
| Usuarios | `GET /api/usuarios/me` | Sesión |
| | `GET /api/usuarios` · `DELETE /api/usuarios/{id}` | ADMIN |

Códigos usados: 200, 201 (creación), 204 (eliminación), 400 (datos inválidos), 401 (sin sesión o credenciales incorrectas), 403 (sin permiso), 404 (no existe), 409 (conflicto de negocio).

## IE3.2.2 Integración frontend-backend

**Flujo de datos:**

1. El componente (por ejemplo `pages/Servicios.tsx`) llama a un módulo de `services/endpoints.ts`.
2. Todas las llamadas pasan por `services/api.ts`, un único cliente HTTP que agrega `Authorization: Bearer <token>`, convierte las respuestas a JSON y transforma los errores en un `ErrorApi` con estado y mensaje.
3. Spring Security (`JwtAuthFilter`) valida el token, el controlador valida los datos (`@Valid`), el servicio aplica las reglas de negocio y el repositorio JPA consulta la base de datos.
4. La respuesta vuelve como DTO JSON. El hook `useApi` entrega `datos`, `cargando` y `error`, y el componente `EstadoCarga` muestra el estado correspondiente.

**Por qué es eficiente y segura:**

- Un solo punto de comunicación (`api.ts`): la URL base viene de `VITE_API_URL` y el token se adjunta en un único lugar.
- El backend devuelve DTOs, no entidades: el hash de la contraseña nunca sale del servidor y se evitan problemas de serialización.
- Las listas se piden una vez por pantalla y el hook cancela las respuestas obsoletas al cambiar de ruta (flag `activo`).
- CORS limitado al origen del frontend y a los métodos y cabeceras necesarios.
- Los contratos TypeScript de `types/` reflejan los DTO del backend, así que un cambio de campo se detecta al compilar.
- Cada formulario valida en el cliente (respuesta inmediata) y el servidor vuelve a validar (no se confía en el cliente).

## IE3.3.1 Autenticación con roles y JWT en el backend

- `POST /api/auth/login` verifica la contraseña con BCrypt y devuelve un JWT firmado (HMAC) con el correo, el rol y la expiración (60 minutos, configurable).
- `JwtAuthFilter` lee el encabezado `Authorization`, valida firma y expiración y registra al usuario con su rol (`ROLE_ADMIN` o `ROLE_CLIENTE`).
- `SecurityConfig` define la política: sesión **stateless**, CSRF desactivado (no hay cookies de sesión) y rutas públicas solo para lectura de catálogo, sucursales, reseñas, Swagger y autenticación. Todo lo demás exige token.
- Los permisos por rol se aplican en dos niveles: rutas en `SecurityConfig` y `@PreAuthorize("hasRole('ADMIN')")` en los endpoints de administración. Las reglas por dueño (cotizaciones y reseñas) están en la capa de servicio.
- Respuestas 401 y 403 en JSON con mensaje claro.
- No hay secretos en el código: `JWT_SECRET` y `ADMIN_PASSWORD` vienen del entorno, o se generan aleatoriamente si faltan.

## IE3.3.2 Gestión de sesión en el frontend

Implementada en `context/AuthContext.tsx` y `services/sesion.ts`:

- **Persistencia:** al iniciar sesión, el token y el usuario se guardan en `localStorage`; al recargar la página, `ProveedorAuth` los restaura antes de renderizar.
- **Validación al cargar:** si hay sesión guardada, se consulta `GET /api/usuarios/me`. Si el servidor responde 401, la sesión se cierra. Si el servidor no responde, la sesión se conserva.
- **Expiración:** `leerSesion` descarta tokens vencidos y un temporizador cierra la sesión automáticamente cuando expira.
- **Cierre ante 401:** cualquier respuesta 401 en una llamada autenticada borra la sesión y actualiza la interfaz.
- **Varias pestañas:** el evento `storage` sincroniza el login y el logout entre pestañas.
- **Rutas protegidas:** `RutaProtegida` redirige a `/login` si no hay sesión (y vuelve a la página original tras ingresar) y muestra "Acceso denegado" si el rol no corresponde (`/admin` exige ADMIN).
- **Interfaz según sesión:** el encabezado muestra el nombre, "Cerrar sesión", "Mis cotizaciones" y, para ADMIN, "Administración".

**Decisión de seguridad:** el token se guarda en `localStorage` para que la sesión sobreviva a recargas y pestañas, que es lo que pide el indicador. Su riesgo es el robo mediante XSS. Se mitiga porque React escapa el contenido por defecto, no se usa `dangerouslySetInnerHTML`, el token expira en 60 minutos y el backend valida cada solicitud. La alternativa más segura sería una cookie `HttpOnly` con protección CSRF, a costa de más configuración en el servidor.
