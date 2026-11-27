# Configuración privada · Mi Espacio

## 1. Base de datos Oracle

| Dato | Valor |
| --- | --- |
| Usuario | `MIESPACIO` |
| Contraseña | `NuevaClaveSegura2026!` |
| Nombre de la base | `mi_espacio` |
| Alias del proyecto | `a6etg25wwemt8hfb_low` |
| URL JDBC | `jdbc:oracle:thin:@a6etg25wwemt8hfb_low` |
| Wallet | `backend/Wallet_A6ETG25WWEMT8HFB` |
| Esquema | `MIESPACIO` |

Database Actions (SQL Developer web):
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

Los clientes se crean desde **Registrarse** o con `POST /api/auth/registro`.

Para recrear el administrador con otra clave:

```sql
DELETE FROM MIESPACIO.COTIZACIONES WHERE USUARIO_ID = (SELECT ID FROM MIESPACIO.USUARIOS WHERE EMAIL = 'admin@miespacio.cl');
DELETE FROM MIESPACIO.RESENAS WHERE USUARIO_ID = (SELECT ID FROM MIESPACIO.USUARIOS WHERE EMAIL = 'admin@miespacio.cl');
DELETE FROM MIESPACIO.USUARIOS WHERE EMAIL = 'admin@miespacio.cl';
COMMIT;
```

## 3. Arrancar el backend

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

## 4. URLs

| Qué | URL |
| --- | --- |
| API (backend) | `http://localhost:8081` |
| Swagger UI | `http://localhost:8081/swagger-ui.html` |
| Documento OpenAPI | `http://localhost:8081/v3/api-docs` |
| Frontend | `http://localhost:8080` (`npm run dev -- --port 8080`) |

Otro puerto de frontend: `$env:CORS_ORIGENES = 'http://localhost:5173'`
