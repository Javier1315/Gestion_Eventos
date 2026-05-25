class Comment {
  constructor({ id, eventId, userId, userName, message }) {
    // Modelo de comentario asociado a un evento.
    this.id = id;
    this.eventId = eventId;
    this.userId = userId;
    this.userName = userName;
    this.message = message;
    this.createdAt = new Date().toISOString();
  }
}

module.exports = Comment;
