class Rating {
  constructor({ id, eventId, userId, value }) {
    // Modelo de calificacion unica por usuario y evento.
    this.id = id;
    this.eventId = eventId;
    this.userId = userId;
    this.value = value;
    this.createdAt = new Date().toISOString();
    this.updatedAt = new Date().toISOString();
  }
}

module.exports = Rating;
