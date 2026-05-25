const User = require("../models/User");
const firestoreService = require("../services/firestoreService");
const { createId } = require("../services/idService");

const register = async (req, res) => {
  // Registra usuarios cuando se usan las rutas propias de la API.
  const { name, email, password } = req.body;

  if (!name || !email || !password) {
    return res.status(400).json({ message: "Nombre, correo y contrasena son requeridos." });
  }

  const existingUsers = await firestoreService.whereEquals("users", "email", email);
  if (existingUsers.length > 0) {
    return res.status(409).json({ message: "El correo ya esta registrado." });
  }

  const user = new User({
    id: createId("user"),
    name,
    email,
    password
  });

  await firestoreService.save("users", user.id, user);

  return res.status(201).json({
    message: "Usuario registrado correctamente.",
    user: user.toPublicJson()
  });
};

const login = async (req, res) => {
  // Valida credenciales contra los usuarios guardados en Firestore.
  const { email, password } = req.body;

  const users = await firestoreService.whereEquals("users", "email", email);
  const userData = users.find((item) => item.password === password);

  if (!userData) {
    return res.status(401).json({ message: "Credenciales invalidas." });
  }

  const user = new User(userData);

  return res.json({
    message: "Inicio de sesion correcto.",
    user: user.toPublicJson()
  });
};

const socialLogin = async (req, res) => {
  // Mantiene una ruta social simple para pruebas de backend.
  const { providerName } = req.body;

  if (!providerName) {
    return res.status(400).json({ message: "El proveedor social es requerido." });
  }

  const email = `${providerName.toLowerCase()}@social.local`;
  const users = await firestoreService.whereEquals("users", "email", email);
  let user = users.length > 0 ? new User(users[0]) : null;

  if (!user) {
    user = new User({
      id: createId("user"),
      name: `Usuario ${providerName}`,
      email,
      password: "social-login"
    });

    await firestoreService.save("users", user.id, user);
  }

  return res.json({
    message: "Inicio de sesion social correcto.",
    user: user.toPublicJson()
  });
};

module.exports = {
  register,
  login,
  socialLogin
};
