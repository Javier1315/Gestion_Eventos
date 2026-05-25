const Event = require("../models/Event");
const firestoreService = require("../services/firestoreService");
const { createId } = require("../services/idService");

const getEvents = async (req, res) => {
  // Devuelve todos los eventos almacenados en Firestore.
  const events = await firestoreService.getAll("events");
  return res.json(events);
};

const getEventById = async (req, res) => {
  // Busca un evento especifico por su id.
  const event = await firestoreService.getById("events", req.params.id);

  if (!event) {
    return res.status(404).json({ message: "Evento no encontrado." });
  }

  return res.json(event);
};

const createEvent = async (req, res) => {
  // Crea un evento nuevo con los datos enviados por Android.
  const { title, description, dateTimeMillis, location, organizerId, category, imageUrl, isPastEvent } = req.body;

  // Valida los campos minimos que la rubrica exige para gestionar eventos.
  if (!title || !description || !dateTimeMillis || !location || !organizerId) {
    return res.status(400).json({ message: "Todos los campos del evento son requeridos." });
  }

  const event = new Event({
    // La API genera el id para mantener control del documento en Firestore.
    id: createId("event"),
    title,
    description,
    dateTimeMillis,
    location,
    organizerId,
    category,
    imageUrl,
    isPastEvent
  });

  await firestoreService.save("events", event.id, event);

  return res.status(201).json(event);
};

const updateEvent = async (req, res) => {
  // Actualiza un evento existente y marca la fecha de modificacion.
  const event = await firestoreService.getById("events", req.params.id);

  if (!event) {
    return res.status(404).json({ message: "Evento no encontrado." });
  }

  const { title, description, dateTimeMillis, location, category, imageUrl, isPastEvent } = req.body;

  // Mantiene los valores anteriores cuando algun campo no viene en la peticion.
  const updatedEvent = await firestoreService.update("events", req.params.id, {
    title: title ?? event.title,
    description: description ?? event.description,
    dateTimeMillis: dateTimeMillis ?? event.dateTimeMillis,
    location: location ?? event.location,
    category: category ?? event.category,
    imageUrl: imageUrl ?? event.imageUrl,
    isPastEvent: isPastEvent ?? event.isPastEvent,
    updatedAt: new Date().toISOString()
  });

  return res.json(updatedEvent);
};

const deleteEvent = async (req, res) => {
  // Cancela un evento sin borrarlo para conservar historial y notificaciones.
  const event = await firestoreService.getById("events", req.params.id);

  if (!event) {
    return res.status(404).json({ message: "Evento no encontrado." });
  }

  await firestoreService.update("events", req.params.id, {
    // La cancelacion logica permite avisar a usuarios que ya hicieron RSVP.
    isCanceled: true,
    canceledAt: new Date().toISOString(),
    updatedAt: new Date().toISOString()
  });

  return res.status(204).send();
};

module.exports = {
  getEvents,
  getEventById,
  createEvent,
  updateEvent,
  deleteEvent
};
