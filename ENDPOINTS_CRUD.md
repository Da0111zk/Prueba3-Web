# Endpoints CRUD · Mi Espacio (Postman)

> Archivo privado: incluye la contraseña del administrador. No lo subas a GitHub.

- **URL base:** `http://localhost:8081`
- **Swagger:** `http://localhost:8081/swagger-ui.html`
- **Formato:** JSON. Todas las solicitudes con cuerpo llevan la cabecera `Content-Type: application/json`.
- **Autenticación:** los endpoints protegidos llevan la cabecera `Authorization: Bearer <token>`.

Para no armar todo a mano, importa `Mi-Espacio.postman_collection.json` en Postman (**Import**). Ya trae estas solicitudes en orden, guarda el token automáticamente y la primera es el login del administrador.

---

## 1. Obtener el token principal (ADMIN)

**Esta es la primera solicitud que debes ejecutar.**

| | |
| --- | --- |
| Método | `POST` |
| URL | `http://localhost:8081/api/auth/login` |
| Acceso | Público |

**Body** (en Postman: pestaña *Body* → *raw* → tipo *JSON*):

```json
{
  "email": "admin@miespacio.cl",
  "password": "8UWnspPtUU8MyH"
}
```

**Respuesta 200:**

```json
{
  "token": "eyJhbGciOi...(texto largo)...",
  "expiraEn": 1764000000000,
  "usuario": {
    "id": 1,
    "nombre": "Administrador",
    "email": "admin@miespacio.cl",
    "rol": "ADMIN"
  }
}
```

### Mostrar y guardar el token en Postman

1. Presiona **Send**: el token aparece en el campo `token` del cuerpo de la respuesta.
2. Para guardarlo automáticamente, abre la pestaña **Scripts** (o **Tests**) de la solicitud y pega:

```javascript
pm.test("Login correcto (200)", function () {
  pm.response.to.have.status(200);
});
const json = pm.response.json();
pm.collectionVariables.set("token", json.token);
console.log("TOKEN:", json.token);
```

3. Para verlo, abre la **Consola de Postman** (`Ctrl+Alt+C`) y busca `TOKEN:`.
4. Para usarlo en las demás solicitudes, ve a la pestaña **Authorization**, elige **Bearer Token** y escribe `{{token}}`. Si lo prefieres manual, copia el valor de `token` y úsalo como `Authorization: Bearer <token>`.

### El mismo login con PowerShell

```powershell
$r = Invoke-RestMethod -Method Post -Uri 'http://localhost:8081/api/auth/login' -ContentType 'application/json' -Body '{"email":"admin@miespacio.cl","password":"8UWnspPtUU8MyH"}'
$r.token
```

### El mismo login con curl (Linux, macOS o Git Bash)

```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"admin@miespacio.cl","password":"8UWnspPtUU8MyH"}'
```

Para los ejemplos siguientes: `TOKEN` es el valor del campo `token`.

---

## 2. Registro de un cliente

| | |
| --- | --- |
| Método y URL | `POST http://localhost:8081/api/auth/registro` |
| Acceso | Público |
| Respuesta | 201 con el mismo formato del login (ya trae el token del cliente) |

```json
{
  "nombre": "Cliente Demo",
  "email": "cliente.demo@correo.cl",
  "password": "Cliente123"
}
```

Errores: 400 (datos inválidos), 409 (el correo ya existe).

Para iniciar sesión como ese cliente después: `POST /api/auth/login` con su correo y contraseña.

---

## 3. Servicios

| Operación | Método y URL | Acceso |
| --- | --- | --- |
| Listar | `GET /api/servicios` | Público |
| Obtener uno | `GET /api/servicios/{id}` | Público |
| Crear | `POST /api/servicios` | ADMIN |
| Actualizar | `PUT /api/servicios/{id}` | ADMIN |
| Eliminar | `DELETE /api/servicios/{id}` | ADMIN |

**GET (listar):**

```bash
curl http://localhost:8081/api/servicios
```

**GET (uno):** `http://localhost:8081/api/servicios/1`

**POST (crear)**, con `Authorization: Bearer TOKEN`:

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

**PUT (actualizar)** `PUT /api/servicios/5`: mismo cuerpo del POST con los datos nuevos. Respuesta 200.

**DELETE** `DELETE /api/servicios/5`: respuesta 204 sin cuerpo. Si el servicio tiene cotizaciones asociadas responde 409.

---

## 4. Sucursales

| Operación | Método y URL | Acceso |
| --- | --- | --- |
| Listar | `GET /api/sucursales` | Público |
| Obtener una | `GET /api/sucursales/{id}` | Público |
| Crear | `POST /api/sucursales` | ADMIN |
| Actualizar | `PUT /api/sucursales/{id}` | ADMIN |
| Eliminar | `DELETE /api/sucursales/{id}` | ADMIN |

**POST (crear)**, con `Authorization: Bearer TOKEN`:

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

`PUT /api/sucursales/{id}` recibe el mismo cuerpo. `DELETE` responde 204.

---

## 5. Reseñas

| Operación | Método y URL | Acceso |
| --- | --- | --- |
| Listar | `GET /api/resenas` | Público |
| Obtener una | `GET /api/resenas/{id}` | Público |
| Crear | `POST /api/resenas` | Sesión (una por usuario) |
| Actualizar | `PUT /api/resenas/{id}` | Autor o ADMIN |
| Eliminar | `DELETE /api/resenas/{id}` | Autor o ADMIN |

**POST (crear)**, con el token del cliente:

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

Reglas: `texto` entre 10 y 500 caracteres, `evaluacion` entre 1 y 5. Si el usuario ya publicó una reseña responde 409.

`PUT /api/resenas/{id}` recibe el mismo cuerpo. `DELETE` responde 204.

---

## 6. Cotizaciones (requieren sesión)

| Operación | Método y URL | Acceso |
| --- | --- | --- |
| Crear | `POST /api/cotizaciones` | Sesión |
| Listar las mías | `GET /api/cotizaciones/mis` | Sesión |
| Listar todas | `GET /api/cotizaciones` | ADMIN |
| Obtener una | `GET /api/cotizaciones/{id}` | Dueño o ADMIN |
| Actualizar | `PUT /api/cotizaciones/{id}` | Dueño (mensaje) o ADMIN (estado) |
| Eliminar | `DELETE /api/cotizaciones/{id}` | Dueño (solo PENDIENTE) o ADMIN |

**POST (crear)**, con el token del cliente:

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

Reglas: `mensaje` entre 10 y 200 caracteres; si ya existe una cotización PENDIENTE del mismo usuario para ese servicio responde 409.

**GET (mías):** `GET http://localhost:8081/api/cotizaciones/mis` con el token del cliente.

**GET (todas):** `GET http://localhost:8081/api/cotizaciones` con el token del ADMIN. Con un token de cliente responde 403.

**PUT como cliente (editar mensaje)**, solo si está PENDIENTE:

```json
{ "mensaje": "Ahora necesito el espacio por 6 meses." }
```

**PUT como ADMIN (cambiar estado)**, solo si está PENDIENTE. Valores permitidos: `ATENDIDA` o `RECHAZADA`:

```json
{ "estado": "ATENDIDA" }
```

**DELETE:** responde 204.

---

## 7. Usuarios

| Operación | Método y URL | Acceso |
| --- | --- | --- |
| Mi perfil | `GET /api/usuarios/me` | Sesión |
| Listar | `GET /api/usuarios` | ADMIN |
| Eliminar | `DELETE /api/usuarios/{id}` | ADMIN |

`GET /api/usuarios/me` sirve para comprobar que el token es válido. `DELETE` borra también las cotizaciones y reseñas del usuario, y no permite eliminar la propia cuenta (409).

---

## 8. Orden sugerido para probar el CRUD completo

1. **Login ADMIN** (sección 1) → guarda `token`.
2. `GET /api/servicios` → ve el catálogo cargado.
3. **Registro de cliente** (sección 2) → guarda el token del cliente.
4. `POST /api/servicios` con el token ADMIN → crea un servicio; luego `PUT` y `DELETE` sobre ese id.
5. `POST /api/cotizaciones` con el token del cliente → guarda el `id`.
6. `GET /api/cotizaciones/mis` con el token del cliente.
7. `GET /api/cotizaciones` con el token ADMIN → ve la cotización del cliente.
8. `PUT /api/cotizaciones/{id}` con el token ADMIN y `{"estado":"ATENDIDA"}`.
9. `POST /api/resenas` con el token del cliente.
10. Prueba un 403: `GET /api/cotizaciones` con el token del cliente.

## 9. Códigos de respuesta

| Código | Significado |
| --- | --- |
| 200 | Operación correcta |
| 201 | Recurso creado |
| 204 | Eliminado (sin cuerpo) |
| 400 | Datos inválidos (el cuerpo detalla los campos) |
| 401 | Falta el token, expiró o las credenciales son incorrectas |
| 403 | El rol no tiene permiso para esa acción |
| 404 | El recurso no existe |
| 409 | Conflicto de negocio (correo repetido, cotización pendiente duplicada, etc.) |

Formato de error:

```json
{ "status": 409, "mensaje": "Ya tienes una cotización pendiente para este servicio." }
```
