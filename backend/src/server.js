const express = require("express");
const cors = require("cors");
const environment = require("./config/environment");
const authRoutes = require("./routes/authRoutes");
const eventRoutes = require("./routes/eventRoutes");
const userRoutes = require("./routes/userRoutes");

const app = express();

// Configura middlewares y rutas principales de la API.
app.use(cors());
app.use(express.json());

app.get("/api/health", (req, res) => {
  res.json({ status: "ok", service: "gestion-eventos-api" });
});

app.use("/api/auth", authRoutes);
app.use("/api/events", eventRoutes);
app.use("/api/users", userRoutes);

app.use((req, res) => {
  res.status(404).json({ message: "Ruta no encontrada." });
});

app.listen(environment.port, () => {
  console.log(`API ejecutandose en http://localhost:${environment.port}/api`);
});
