# Gestion Eventos API

API REST para gestionar autenticacion, eventos, RSVP, comentarios, calificaciones, historial y estadisticas.

## Ejecutar

```bash
npm install
npm run dev
```

## Firestore

La API usa Firebase Admin SDK para guardar eventos, RSVP, comentarios, calificaciones, historial y estadisticas en Firestore.

Para ejecutarla localmente:

1. En Firebase Console entra a Configuracion del proyecto > Cuentas de servicio.
2. Genera una nueva clave privada.
3. Guarda el archivo como `backend/serviceAccountKey.json`.
4. Ejecuta la API con `npm run dev`.

El archivo `serviceAccountKey.json` no debe subirse a GitHub.

Por defecto la API queda disponible en:

```txt
http://localhost:3000/api
```

## Endpoints

```txt
POST   /api/auth/register
POST   /api/auth/login
GET    /api/events
POST   /api/events
GET    /api/events/:id
PUT    /api/events/:id
DELETE /api/events/:id
POST   /api/events/:id/rsvp
POST   /api/events/:id/comments
POST   /api/events/:id/ratings
GET    /api/users/:id/history
GET    /api/users/:id/stats
GET    /api/users/:id/rsvps
```
