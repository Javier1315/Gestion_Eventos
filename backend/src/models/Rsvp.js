class Rsvp {
  constructor({ id, eventId, userId, status }) {
    // Modelo de confirmacion de asistencia.
    this.id = id;
    this.eventId = eventId;
    this.userId = userId;
    this.status = status;
    this.updatedAt = new Date().toISOString();
  }
}

module.exports = Rsvp;
