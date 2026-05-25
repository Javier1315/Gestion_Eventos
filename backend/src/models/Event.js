class Event {
  constructor({
    id,
    title,
    description,
    dateTimeMillis,
    location,
    organizerId,
    category,
    imageUrl,
    isPastEvent,
    isCanceled,
    canceledAt,
    createdAt,
    updatedAt
  }) {
    // Modelo de evento usado antes de guardar en Firestore.
    const now = new Date().toISOString();

    this.id = id;
    this.title = title;
    this.description = description;
    this.dateTimeMillis = dateTimeMillis;
    this.location = location;
    this.organizerId = organizerId;
    this.category = category || "ACADEMIC";
    this.imageUrl = imageUrl || null;
    this.isPastEvent = Boolean(isPastEvent);
    this.isCanceled = Boolean(isCanceled);
    this.canceledAt = canceledAt || null;
    this.createdAt = createdAt || now;
    this.updatedAt = updatedAt || now;
  }
}

module.exports = Event;
