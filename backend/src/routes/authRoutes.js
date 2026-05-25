const express = require("express");
const authController = require("../controllers/authController");

const router = express.Router();

// Agrupa las rutas de autenticacion de la API.
router.post("/register", authController.register);
router.post("/login", authController.login);
router.post("/social-login", authController.socialLogin);

module.exports = router;
