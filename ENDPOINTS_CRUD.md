# Endpoints CRUD · Mi Espacio

- **URL base:** `http://localhost:8081`
- **Swagger:** `http://localhost:8081/swagger-ui.html`
- **Formato:** JSON (`Content-Type: application/json`)
- **Autenticación:** `Authorization: Bearer <token>`

---

## 1. Login (ADMIN)

| | |
| --- | --- |
| Método | `POST` |
| URL | `http://localhost:8081/api/auth/login` |
| Acceso | Público |

```json
{
  "email": "admin@miespacio.cl",
  "password": "8UWnspPtUU8MyH"
}
```

**Respuesta 200:**

```json
{
  "token": "eyJhbGciOi...",
  "expiraEn": 1764000000000,
  "usuario": {
    "id": 1,
    "nombre": "Administrador",
    "email": "admin@miespacio.cl",
    "rol": "ADMIN"
  }
}
```

### Guardar el token en Postman (pestaña Scripts / Tests)

```javascript
pm.test("Login correcto (200)", function () {
  pm.response.to.have.status(200);
});
const json = pm.response.json();
pm.collectionVariables.set("token", json.token);
console.log("TOKEN:", json.token);
```

Authorization → Bearer Token → `{{token}}`

### PowerShell

```powershell
$r = Invoke-RestMethod -Method Post -Uri 'http://localhost:8081/api/auth/login' -ContentType 'application/json' -Body '{"email":"admin@miespacio.cl","password":"8UWnspPtUU8MyH"}'
$r.token
```

### curl

```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"admin@miespacio.cl","password":"8UWnspPtUU8MyH"}'
```

---

## 2. Registro de cliente

`POST http://localhost:8081/api/auth/registro` · Público · Respuesta 201 (mismo formato del login)

```json
{
  "nombre": "Cliente Demo",
  "email": "cliente.demo@correo.cl",
  "password": "Cliente123"
}
```

Errores: 400 (datos inválidos), 409 (correo existente).

---

## 3. Servicios

| Operación | Método y URL | Acceso |
| --- | --- | --- |
| Listar | `GET /api/servicios` | Público |
| Obtener uno | `GET /api/servicios/{id}` | Público |
| Crear | `POST /api/servicios` | ADMIN |
| Actualizar | `PUT /api/servicios/{id}` | ADMIN |
| Eliminar | `DELETE /api/servicios/{id}` | ADMIN |

**POST / PUT:**

```json
{
  "nombre": "Bodega XL",
  "descripcion": "Bodega de gran tamaño para inventario y mercadería.",
  "categoria": "Bodega grande",
  "tamano": "5mt x 5mt",
  "imagen": "/img/bodega-grande.jpg"
}
```

```bash
curl -X POST http://localhost:8081/api/servicios \
  -H 'Authorization: Bearer TOKEN' -H 'Content-Type: application/json' \
  -d '{"nombre":"Bodega XL","descripcion":"Bodega de gran tamaño para inventario y mercadería.","categoria":"Bodega grande","tamano":"5mt x 5mt","imagen":"/img/bodega-grande.jpg"}'
```

Respuesta 201:

```json
{ "id": 5, "nombre": "Bodega XL", "descripcion": "Bodega de gran tamaño para inventario y mercadería.", "categoria": "Bodega grande", "tamano": "5mt x 5mt", "imagen": "/img/bodega-grande.jpg" }
```

`PUT` responde 200. `DELETE` responde 204 (409 si tiene cotizaciones asociadas).

---

## 4. Sucursales

| Operación | Método y URL | Acceso |
| --- | --- | --- |
| Listar | `GET /api/sucursales` | Público |
| Obtener una | `GET /api/sucursales/{id}` | Público |
| Crear | `POST /api/sucursales` | ADMIN |
| Actualizar | `PUT /api/sucursales/{id}` | ADMIN |
| Eliminar | `DELETE /api/sucursales/{id}` | ADMIN |

**POST / PUT:**

```json
{
  "nombre": "Sucursal Sur - La Cisterna",
  "direccion": "Av. Jorge Alessandri 1500, La Cisterna",
  "horario": "Lunes a Viernes 9:00 - 18:00 hrs",
  "acceso": "24/7 con tarjeta digital",
  "telefono": "+56 9 1111 2222",
  "mapaUrl": ""
}
```

`DELETE` responde 204.

---

## 5. Reseñas

| Operación | Método y URL | Acceso |
| --- | --- | --- |
| Listar | `GET /api/resenas` | Público |
| Obtener una | `GET /api/resenas/{id}` | Público |
| Crear | `POST /api/resenas` | Sesión (una por usuario) |
| Actualizar | `PUT /api/resenas/{id}` | Autor o ADMIN |
| Eliminar | `DELETE /api/resenas/{id}` | Autor o ADMIN |

**POST / PUT:**

```json
{
  "texto": "Excelente servicio, muy seguro y con buena atención.",
  "evaluacion": 5
}
```

Respuesta 201:

```json
{ "id": 4, "autor": "Cliente Demo", "texto": "Excelente servicio, muy seguro y con buena atención.", "evaluacion": 5, "fecha": "2026-11-25T10:30:00", "usuarioId": 2 }
```

Reglas: `texto` 10–500 caracteres, `evaluacion` 1–5. 409 si el usuario ya publicó una reseña. `DELETE` responde 204.

---

## 6. Cotizaciones

| Operación | Método y URL | Acceso |
| --- | --- | --- |
| Crear | `POST /api/cotizaciones` | Sesión |
| Listar las mías | `GET /api/cotizaciones/mis` | Sesión |
| Listar todas | `GET /api/cotizaciones` | ADMIN |
| Obtener una | `GET /api/cotizaciones/{id}` | Dueño o ADMIN |
| Actualizar | `PUT /api/cotizaciones/{id}` | Dueño (mensaje) o ADMIN (estado) |
| Eliminar | `DELETE /api/cotizaciones/{id}` | Dueño (solo PENDIENTE) o ADMIN |

**POST:**

```json
{
  "servicioId": 1,
  "mensaje": "Necesito guardar cajas y herramientas por 3 meses."
}
```

Respuesta 201:

```json
{
  "id": 1,
  "servicioId": 1,
  "servicioNombre": "Locker",
  "usuarioNombre": "Cliente Demo",
  "usuarioEmail": "cliente.demo@correo.cl",
  "mensaje": "Necesito guardar cajas y herramientas por 3 meses.",
  "estado": "PENDIENTE",
  "creadaEn": "2026-11-25T10:35:00"
}
```

Reglas: `mensaje` 10–200 caracteres; 409 si ya existe una cotización PENDIENTE del mismo usuario para ese servicio.

**PUT cliente** (solo PENDIENTE):

```json
{ "mensaje": "Ahora necesito el espacio por 6 meses." }
```

**PUT ADMIN** (solo PENDIENTE; valores `ATENDIDA` o `RECHAZADA`):

```json
{ "estado": "ATENDIDA" }
```

`GET /api/cotizaciones` con token de cliente responde 403. `DELETE` responde 204.

---

## 7. Usuarios

| Operación | Método y URL | Acceso |
| --- | --- | --- |
| Mi perfil | `GET /api/usuarios/me` | Sesión |
| Listar | `GET /api/usuarios` | ADMIN |
| Eliminar | `DELETE /api/usuarios/{id}` | ADMIN |

`DELETE` borra también cotizaciones y reseñas del usuario; no permite eliminar la propia cuenta (409).

---

## 8. Orden para probar el CRUD

1. Login ADMIN → guardar `token`.
2. `GET /api/servicios`.
3. Registro de cliente → guardar su token.
4. `POST /api/servicios` (ADMIN), luego `PUT` y `DELETE` sobre ese id.
5. `POST /api/cotizaciones` (cliente) → guardar `id`.
6. `GET /api/cotizaciones/mis` (cliente).
7. `GET /api/cotizaciones` (ADMIN).
8. `PUT /api/cotizaciones/{id}` (ADMIN) con `{"estado":"ATENDIDA"}`.
9. `POST /api/resenas` (cliente).
10. `GET /api/cotizaciones` con token de cliente → 403.

## 9. Códigos de respuesta

| Código | Significado |
| --- | --- |
| 200 | Operación correcta |
| 201 | Recurso creado |
| 204 | Eliminado (sin cuerpo) |
| 400 | Datos inválidos |
| 401 | Token ausente, expirado o credenciales incorrectas |
| 403 | Rol sin permiso |
| 404 | Recurso inexistente |
| 409 | Conflicto de negocio |

```json
{ "status": 409, "mensaje": "Ya tienes una cotización pendiente para este servicio." }
```
