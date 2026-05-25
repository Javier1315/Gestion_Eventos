const admin = require("firebase-admin");
const path = require("path");

const initializeFirebase = () => {
  // Inicializa Firebase Admin para que la API pueda escribir en Firestore.
  if (admin.apps.length > 0) {
    return admin.firestore();
  }

  const serviceAccountPath =
    process.env.FIREBASE_SERVICE_ACCOUNT_PATH ||
    path.join(__dirname, "../../serviceAccountKey.json");

  try {
    const serviceAccount = require(serviceAccountPath);

    admin.initializeApp({
      credential: admin.credential.cert(serviceAccount)
    });

    return admin.firestore();
  } catch (error) {
    throw new Error(
      `No se pudo inicializar Firebase Admin. Coloca la llave en backend/serviceAccountKey.json o configura FIREBASE_SERVICE_ACCOUNT_PATH. Detalle: ${error.message}`
    );
  }
};

module.exports = initializeFirebase();
