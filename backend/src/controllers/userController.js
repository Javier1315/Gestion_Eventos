const firestoreService = require("../services/firestoreService");

const getHistory = async (req, res) => {
  // Construye el historial con eventos donde el usuario confirmo asistencia.
  const userId = req.params.id;
  const rsvps = await firestoreService.whereEquals("rsvps", "userId", userId);
  const events = await firestoreService.getAll("events");
  const attendedEventIds = rsvps
    .filter((rsvp) => rsvp.userId === userId && rsvp.status === "CONFIRMED")
    .map((rsvp) => rsvp.eventId);

  // Cruza RSVP con eventos para conservar datos de eventos cancelados o editados.
  const attendedEvents = events.filter((event) => attendedEventIds.includes(event.id));

  return res.json(attendedEvents);
};

const getStats = async (req, res) => {
  // Calcula estadisticas de participacion y actividad social del usuario.
  const userId = req.params.id;
  const rsvps = await firestoreService.whereEquals("rsvps", "userId", userId);
  const comments = await firestoreService.whereEquals("comments", "userId", userId);
  const ratings = await firestoreService.whereEquals("ratings", "userId", userId);
  const events = await firestoreService.getAll("events");
  const confirmedRsvps = rsvps.filter(
    (rsvp) => rsvp.userId === userId && rsvp.status === "CONFIRMED"
  );
  const attendedEventIds = confirmedRsvps.map((rsvp) => rsvp.eventId);
  // Las estadisticas se calculan a partir de RSVP, eventos e interacciones sociales.
  const attendedEvents = events.filter((event) => attendedEventIds.includes(event.id));
  const now = Date.now();

  return res.json({
    userId,
    attendedEvents: confirmedRsvps.length,
    upcomingEvents: attendedEvents.filter(
      (event) => !event.isCanceled && Number(event.dateTimeMillis) >= now
    ).length,
    pastEvents: attendedEvents.filter(
      (event) => !event.isCanceled && Number(event.dateTimeMillis) < now
    ).length,
    canceledEvents: attendedEvents.filter((event) => event.isCanceled).length,
    comments: comments.length,
    ratings: ratings.length
  });
};

const getRsvps = async (req, res) => {
  // Devuelve todas las respuestas RSVP del usuario.
  const userId = req.params.id;
  const rsvps = await firestoreService.whereEquals("rsvps", "userId", userId);

  return res.json(rsvps);
};

module.exports = {
  getHistory,
  getStats,
  getRsvps
};
