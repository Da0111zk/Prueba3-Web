# Configuración privada · Mi Espacio

> **ARCHIVO PRIVADO. NO lo subas a GitHub ni lo compartas.**
> Guárdalo fuera de la carpeta del proyecto, o dentro de una carpeta `PRIVADO/` y agrega estas líneas al `.gitignore` del repositorio:
>
> ```
> PRIVADO/
> Wallet_*/
> .env
> ```

## 1. Base de datos Oracle

| Dato | Valor |
| --- | --- |
| Usuario | `MIESPACIO` |
| Contraseña | `NuevaClaveSegura2026!` |
| Nombre de la base (según tus datos) | `mi_espacio` |
| Alias que usa el proyecto | `a6etg25wwemt8hfb_low` |
| URL JDBC (ya está en `application-oracle.properties`) | `jdbc:oracle:thin:@a6etg25wwemt8hfb_low` |
| Wallet | `backend/Wallet_A6ETG25WWEMT8HFB` (el backend la detecta solo al arrancar) |
| Esquema | `MIESPACIO` |

**Ojo con el nombre `mi_espacio`:** no aparece como alias en el `tnsnames.ora` de tu wallet. Los alias disponibles son `a6etg25wwemt8hfb_high`, `_medium`, `_low`, `_tp` y `_tpurgent`, y el proyecto usa `_low`. Si `mi_espacio` es el nombre de tu base en el panel de Oracle, no hace falta usarlo en la conexión.

Database Actions (SQL Developer web), tomado del README de tu wallet:
`https://G0FBF24BA59B790-A6ETG25WWEMT8HFB.adb.sa-santiago-1.oraclecloudapps.com/ords/sql-developer`

### Consultas para verificar

```sql
SELECT COUNT(*) FROM MIESPACIO.SERVICIOS;
SELECT COUNT(*) FROM MIESPACIO.SUCURSALES;
SELECT COUNT(*) FROM MIESPACIO.RESENAS;
SELECT ID, NOMBRE, EMAIL, ROL FROM MIESPACIO.USUARIOS;
SELECT ID, USUARIO_ID, SERVICIO_ID, ESTADO FROM MIESPACIO.COTIZACIONES;
```

## 2. Usuarios de la página web

| Rol | Correo | Contraseña |
| --- | --- | --- |
| ADMIN | `admin@miespacio.cl` | `8UWnspPtUU8MyH` |

Los clientes se crean desde la página en **Registrarse** (o con `POST /api/auth/registro`).

**Importante sobre `ADMIN_PASSWORD`:** el administrador se crea solo la primera vez que arranca el backend. Si ya existe en la tabla `USUARIOS`, cambiar `ADMIN_PASSWORD` no modifica su contraseña. Para volver a crearlo con otra clave, borra su fila y reinicia con la variable definida (si tiene cotizaciones o reseñas, bórralas antes por las claves foráneas):

```sql
DELETE FROM MIESPACIO.COTIZACIONES WHERE USUARIO_ID = (SELECT ID FROM MIESPACIO.USUARIOS WHERE EMAIL = 'admin@miespacio.cl');
DELETE FROM MIESPACIO.RESENAS WHERE USUARIO_ID = (SELECT ID FROM MIESPACIO.USUARIOS WHERE EMAIL = 'admin@miespacio.cl');
DELETE FROM MIESPACIO.USUARIOS WHERE EMAIL = 'admin@miespacio.cl';
COMMIT;
```

## 3. Arrancar el backend con estos datos

### PowerShell

```powershell
cd backend
$env:SPRING_PROFILES_ACTIVE = 'oracle'
$env:DB_USER = 'MIESPACIO'
$env:DB_PASSWORD = 'NuevaClaveSegura2026!'
$env:ADMIN_EMAIL = 'admin@miespacio.cl'
$env:ADMIN_PASSWORD = '8UWnspPtUU8MyH'
$env:JWT_SECRET = [Convert]::ToBase64String((1..48 | ForEach-Object { Get-Random -Maximum 256 }))
mvn spring-boot:run
```

### Linux o macOS

```bash
cd backend
export SPRING_PROFILES_ACTIVE=oracle
export DB_USER='MIESPACIO'
export DB_PASSWORD='NuevaClaveSegura2026!'
export ADMIN_EMAIL='admin@miespacio.cl'
export ADMIN_PASSWORD='8UWnspPtUU8MyH'
export JWT_SECRET="$(openssl rand -base64 48)"
mvn spring-boot:run
```

Con la comilla simple, el `!` de la contraseña no causa problemas. `JWT_SECRET` se genera al azar en cada arranque, así que los tokens anteriores dejan de valer al reiniciar. Si quieres que sobrevivan, genera la clave una vez y guárdala aquí:

`JWT_SECRET = (pega aquí tu clave de al menos 32 caracteres)`

## 4. URLs de la aplicación

| Qué | URL |
| --- | --- |
| API (backend) | `http://localhost:8081` |
| Swagger UI | `http://localhost:8081/swagger-ui.html` |
| Documento OpenAPI | `http://localhost:8081/v3/api-docs` |
| Frontend | `http://localhost:8080` (con `npm run dev -- --port 8080`) |

El backend permite por defecto el origen `http://localhost:8080`. Si abres el frontend en otro puerto, por ejemplo 5173, define `CORS_ORIGENES` antes de arrancar el backend (`$env:CORS_ORIGENES = 'http://localhost:5173'`).

## 5. Antes de subir a GitHub

1. Agrega `Wallet_*/`, `PRIVADO/` y `.env` al `.gitignore`.
2. Ejecuta `git status` y confirma que la carpeta de la wallet y este archivo no aparecen.
3. Si la wallet ya se subió en algún commit anterior, bórrala del historial y descarga una wallet nueva desde Oracle Cloud, además de cambiar la contraseña del usuario `MIESPACIO`.
4. El archivo `ojdbc.properties` dentro de la wallet contiene contraseñas de los keystore, otra razón para no publicarla.
