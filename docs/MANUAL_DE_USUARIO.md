# Manual de usuario · Mi Espacio

## 1. Puesta en marcha

1. Inicia el backend (`mvn spring-boot:run` dentro de `backend/`). Al arrancar, la consola muestra el correo y la contraseña del administrador.
2. Inicia el frontend (`npm run dev` dentro de `frontend/`) y abre http://localhost:5173.

![alt text](image.png)
![alt text](img/image-1.png)
![alt text](img/image-2.png)

## 2. Navegación pública

Sin iniciar sesión puedes ver **Inicio**, **Acerca de**, **Contacto**, **Servicios**, **Preguntas Frecuentes**, **Reseñas** y **Sucursales**.

- En **Servicios** se muestran las tarjetas del catálogo. Presiona **Ver detalle** para ver el tamaño, el tipo y la descripción.

![alt text](img/image-3.png)
![alt text](img/image-4.png)
![alt text](img/image-5.png)

## 3. Registro de cliente

1. Presiona **Registrarse** (arriba a la derecha).
2. Completa nombre, correo, contraseña (mínimo 6 caracteres) y su confirmación.
3. Al enviar, la sesión se inicia automáticamente.

Si el correo ya existe o algún dato es inválido, se muestra un mensaje bajo el campo o sobre el formulario.
![alt text](img/image-6.png)
![alt text](img/image-7.png)
![alt text](img/image-8.png)
![alt text](img/image-9.png)

## 4. Inicio de sesión

1. Presiona **Iniciar Sesión** e ingresa tu correo y contraseña.
2. Si las credenciales son incorrectas, se muestra "Correo o contraseña incorrectos."
3. La sesión se mantiene aunque recargues la página o abras otra pestaña, hasta que expire (60 minutos) o cierres sesión.

![alt text](img/image-10.png)

## 5. Solicitar una cotización (requiere sesión)

1. Entra a **Cotiza Aquí** (o presiona **Cotizar este servicio** en el detalle de un servicio).
2. Si no has iniciado sesión, el sistema te lleva a Iniciar Sesión y luego vuelve a esta página.
3. Elige el servicio, escribe tu mensaje (entre 10 y 200 caracteres) y presiona **Solicitar cotización**.
4. No puedes tener dos cotizaciones pendientes del mismo servicio.

![alt text](img/image-11.png)
![alt text](img/image-12.png)
![alt text](img/image-13.png)


## 6. Mis cotizaciones

En **Mis cotizaciones** ves el servicio, el mensaje, el estado (PENDIENTE, ATENDIDA o RECHAZADA) y la fecha. Puedes eliminar solo las cotizaciones pendientes.

![alt text](img/image-14.png)

## 7. Reseñas

- Todos pueden leer las reseñas.
- Con sesión iniciada, aparece el formulario **Deja tu reseña** (evaluación de 1 a 5 y comentario). Puedes publicar una reseña por usuario y eliminar la tuya.

![alt text](img/image-15.png)

## 8. Panel de administración (solo ADMIN)

Inicia sesión con la cuenta de administrador. Aparece el enlace **Administración**.

**Servicios:** crea un servicio con el formulario, edítalo con **Editar** o elimínalo con **Eliminar**. Un servicio con cotizaciones asociadas no se puede eliminar.

**Cotizaciones:** revisa todas las solicitudes y márcalas como **Atender** o **Rechazar**.

Un cliente que intente entrar a `/admin` ve "Acceso denegado".

![alt text](img/image-16.png)
![alt text](img/image-17.png)
![alt text](img/image-18.png)
![alt text](img/image-19.png)

## 9. Cerrar sesión

Presiona **Cerrar sesión** en el encabezado. Las rutas protegidas vuelven a pedir inicio de sesión.

## 10. Documentación de la API (Swagger)

1. Abre http://localhost:8080/swagger-ui.html.
![alt text](img/image-20.png)![alt text](img/image-21.png)![alt text](img/image-22.png)
2. Ejecuta `POST /api/auth/login` con tu correo y contraseña y copia el campo `token`.
![alt text](img/image-23.png)
3. Presiona **Authorize**, pega el token y confirma.
![alt text](img/image-24.png)
4. Prueba los endpoints protegidos (por ejemplo `GET /api/cotizaciones` como ADMIN).



![alt text](img/image-25.png)
![alt text](img/image-26.png)
![alt text](img/image-27.png)
![alt text](img/image-28.png)

## 11. Documentacion Swagger como Cliente

![alt text](img/image-29.png)![alt text](img/image-30.png)![alt text](img/image-31.png)![alt text](img/image-32.png)![alt text](img/image-33.png)