# Gestion de Eventos

Aplicacion movil Android para gestionar eventos universitarios y comunitarios. El proyecto usa Kotlin, Jetpack Compose, MVVM, una estructura inspirada en Clean Architecture y una API REST en Node.js con Express conectada a Firestore.

## Integrante

- Javier Canizalez
- Modalidad: Proyecto individual
- Grupo teorico: Pendiente de completar

## Objetivo

Permitir que usuarios y organizadores puedan registrarse, iniciar sesion, crear eventos, explorar eventos proximos, confirmar asistencia, comentar, calificar, compartir eventos y consultar historial con estadisticas de participacion.

## Tecnologias

- Android
- Kotlin
- Jetpack Compose
- MVVM
- Clean Architecture
- Firebase Authentication
- Google Sign In
- Node.js
- Express
- Firebase Admin SDK
- Firestore
- Retrofit

## Funcionalidades

- Registro e inicio de sesion con correo y contrasena.
- Inicio de sesion con Google mediante Firebase.
- Creacion de eventos por pasos.
- Edicion y cancelacion de eventos por parte del organizador.
- Lista de eventos con busqueda.
- Detalle de evento con fecha, hora, categoria, ubicacion, descripcion y portada.
- Confirmacion de asistencia RSVP.
- Notificaciones en historial cuando un evento confirmado se actualiza o cancela.
- Comentarios por evento.
- Calificacion de 1 a 5 estrellas, actualizable por usuario.
- Compartir eventos por redes sociales o correo.
- Historial con estadisticas de confirmados, proximos, pasados, cancelados, comentarios y calificaciones.

## Arquitectura

```txt
Gestion_Eventos/
|-- app/
|   |-- src/main/java/gestion_eventos/com/
|       |-- core/
|       |   |-- navigation/
|       |   |-- ui/
|       |   |-- utils/
|       |-- data/
|       |   |-- local/
|       |   |-- remote/
|       |   |-- repository/
|       |   |-- mapper/
|       |-- domain/
|       |   |-- model/
|       |   |-- repository/
|       |   |-- usecase/
|       |-- presentation/
|       |   |-- auth/
|       |   |-- events/
|       |   |-- history/
|       |   |-- profile/
|       |   |-- rsvp/
|       |   |-- social/
|       |-- MainActivity.kt
|-- backend/
|   |-- src/
|       |-- config/
|       |-- controllers/
|       |-- models/
|       |-- routes/
|       |-- services/
|-- LICENSE-CONTENT.md
|-- README.md
```

## Backend API

La API REST se encuentra en `backend/`. Android consume esta API mediante Retrofit. La API conserva la logica de negocio y guarda datos persistentes en Firestore.

### Endpoints

```txt
GET    /api/health
POST   /api/auth/register
POST   /api/auth/login
POST   /api/auth/social-login
GET    /api/events
POST   /api/events
GET    /api/events/:id
PUT    /api/events/:id
DELETE /api/events/:id
POST   /api/events/:id/rsvp
GET    /api/events/:id/comments
POST   /api/events/:id/comments
GET    /api/events/:id/ratings
POST   /api/events/:id/ratings
GET    /api/users/:id/history
GET    /api/users/:id/stats
GET    /api/users/:id/rsvps
```

## Configuracion De Firebase

La app Android usa `app/google-services.json` para Firebase Authentication y Google Sign In.

Para Google Sign In se deben registrar en Firebase las huellas:

```txt
SHA1: 81:9B:FD:D8:90:27:7F:56:89:AC:5B:EC:AE:6A:33:94:BD:F9:DB:56
SHA-256: 7E:43:5B:AF:86:31:EE:14:C3:00:D2:C4:91:DE:F9:35:B7:F2:06:15:6A:37:77:D5:6A:57:68:25:0C:4F:C7:93
```

## Configuracion De Firestore En La API

La API usa Firebase Admin SDK. Para ejecutarla localmente:

1. Entrar a Firebase Console.
2. Ir a Configuracion del proyecto.
3. Abrir Cuentas de servicio.
4. Generar una nueva clave privada.
5. Guardar el archivo como:

```txt
backend/serviceAccountKey.json
```

Este archivo no debe subirse a GitHub y ya esta protegido por `.gitignore`.

## Ejecucion Del Backend

```powershell
cd backend
npm install
npm run dev
```

La API queda disponible en:

```txt
http://localhost:3000/api
```

Desde el emulador Android se consume como:

```txt
http://10.0.2.2:3000/api/
```

## Ejecucion De Android

1. Abrir el proyecto en Android Studio.
2. Sincronizar Gradle.
3. Verificar que el backend este corriendo.
4. Ejecutar la configuracion `app` en un emulador o dispositivo.

## Portadas De Eventos

Las portadas de eventos se almacenan localmente en el dispositivo donde se seleccionan. El backend guarda la ruta local para la demostracion visual, pero las imagenes no se sincronizan entre usuarios porque el proyecto no utiliza un servicio externo de archivos como Firebase Storage o Cloudinary.

## Disenos Mockups

Pendiente de agregar link.

## Licencia Creative Commons

La documentacion, disenos, mockups, imagenes y contenido visual de este proyecto estan licenciados bajo Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International.

Licencia: https://creativecommons.org/licenses/by-nc-sa/4.0/

Para mas detalles, revisar [LICENSE-CONTENT.md](LICENSE-CONTENT.md).

## Notas De Seguridad

- No subir `backend/serviceAccountKey.json`.
- No subir contrasenas ni credenciales privadas.
- El archivo `google-services.json` es requerido por Android, pero no reemplaza la llave privada del backend.

## Estado Del Proyecto

El proyecto ya cubre autenticacion, gestion de eventos, RSVP, interaccion social, historial, estadisticas, UI principal, backend API y persistencia en Firestore.
