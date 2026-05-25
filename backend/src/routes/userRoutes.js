const express = require("express");
const userController = require("../controllers/userController");

const router = express.Router();

// Agrupa rutas de historial, estadisticas y respuestas del usuario.
router.get("/:id/history", userController.getHistory);
router.get("/:id/stats", userController.getStats);
router.get("/:id/rsvps", userController.getRsvps);

module.exports = router;
