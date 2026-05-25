const Comment = require("../models/Comment");
const Rating = require("../models/Rating");
const Rsvp = require("../models/Rsvp");
const firestoreService = require("../services/firestoreService");
const { createId } = require("../services/idService");

const createRsvp = async (req, res) => {
  // Registra o actualiza la confirmacion de asistencia del usuario.
  const { userId, status = "CONFIRMED" } = req.body;
  const eventId = req.params.id;

  if (!userId) {
    return res.status(400).json({ message: "El usuario es requerido." });
  }

  const rsvp = new Rsvp({
    // El id compuesto evita duplicar RSVP del mismo usuario en el mismo evento.
    id: `${eventId}-${userId}`,
    eventId,
    userId,
    status
  });

  await firestoreService.save("rsvps", rsvp.id, rsvp);

  return res.status(201).json(rsvp);
};

const getCommentsByEvent = async (req, res) => {
  // Devuelve los comentarios asociados a un evento.
  const eventId = req.params.id;
  const comments = await firestoreService.whereEquals("comments", "eventId", eventId);

  return res.json(comments);
};

const addComment = async (req, res) => {
  // Agrega un comentario nuevo al evento.
  const { userId, userName, message } = req.body;
  const eventId = req.params.id;

  if (!userId || !userName || !message) {
    return res.status(400).json({ message: "Usuario y comentario son requeridos." });
  }

  const comment = new Comment({
    // Los comentarios si permiten multiples entradas del mismo usuario.
    id: createId("comment"),
    eventId,
    userId,
    userName,
    message
  });

  await firestoreService.save("comments", comment.id, comment);

  return res.status(201).json(comment);
};

const getRatingsByEvent = async (req, res) => {
  // Devuelve las calificaciones registradas para un evento.
  const eventId = req.params.id;
  const ratings = await firestoreService.whereEquals("ratings", "eventId", eventId);

  return res.json(ratings);
};

const addRating = async (req, res) => {
  // Crea o actualiza la calificacion del usuario para evitar duplicados.
  const { userId, value } = req.body;
  const eventId = req.params.id;

  if (!userId || value === undefined) {
    return res.status(400).json({ message: "Usuario y calificacion son requeridos." });
  }

  const ratingValue = Number(value);

  if (Number.isNaN(ratingValue) || ratingValue < 1 || ratingValue > 5) {
    return res.status(400).json({ message: "La calificacion debe estar entre 1 y 5." });
  }

  const ratingId = `${eventId}-${userId}`;
  // Se usa un id compuesto para que una persona solo tenga una calificacion activa.
  const existingRating = await firestoreService.getById("ratings", ratingId);

  if (existingRating) {
    const updatedRating = await firestoreService.update("ratings", ratingId, {
      value: ratingValue,
      updatedAt: new Date().toISOString()
    });
    return res.json(updatedRating);
  }

  const rating = new Rating({
    id: ratingId,
    eventId,
    userId,
    value: ratingValue
  });
  await firestoreService.save("ratings", rating.id, rating);

  return res.status(201).json(rating);
};

module.exports = {
  createRsvp,
  getCommentsByEvent,
  addComment,
  getRatingsByEvent,
  addRating
};
