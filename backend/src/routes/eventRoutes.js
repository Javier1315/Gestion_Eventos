const express = require("express");
const eventController = require("../controllers/eventController");
const socialController = require("../controllers/socialController");

const router = express.Router();

// Agrupa rutas de eventos, RSVP, comentarios y calificaciones.
router.get("/", eventController.getEvents);
router.post("/", eventController.createEvent);
router.get("/:id", eventController.getEventById);
router.put("/:id", eventController.updateEvent);
router.delete("/:id", eventController.deleteEvent);
router.post("/:id/rsvp", socialController.createRsvp);
router.get("/:id/comments", socialController.getCommentsByEvent);
router.post("/:id/comments", socialController.addComment);
router.get("/:id/ratings", socialController.getRatingsByEvent);
router.post("/:id/ratings", socialController.addRating);

module.exports = router;
