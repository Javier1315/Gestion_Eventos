class User {
  constructor({ id, name, email, password }) {
    // Modelo de usuario para las rutas de autenticacion de la API.
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
  }

  toPublicJson() {
    // Evita exponer la contrasena en las respuestas HTTP.
    return {
      id: this.id,
      name: this.name,
      email: this.email
    };
  }
}

module.exports = User;
