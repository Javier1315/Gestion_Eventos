const db = require("../config/firebase");

// Agrupa las colecciones usadas por la API.
const collections = {
  events: db.collection("events"),
  users: db.collection("users"),
  rsvps: db.collection("rsvps"),
  comments: db.collection("comments"),
  ratings: db.collection("ratings")
};

const toPlainObject = (document) => {
  // Convierte un documento de Firestore en un objeto JSON normal.
  if (!document.exists) {
    return null;
  }

  return {
    id: document.id,
    ...document.data()
  };
};

const normalizeData = (data) => JSON.parse(JSON.stringify(data));

const getAll = async (collectionName) => {
  // Obtiene todos los documentos de una coleccion.
  const snapshot = await collections[collectionName].get();
  return snapshot.docs.map(toPlainObject);
};

const getById = async (collectionName, id) => {
  // Busca un documento por su identificador.
  const document = await collections[collectionName].doc(id).get();
  return toPlainObject(document);
};

const save = async (collectionName, id, data) => {
  // Guarda un documento nuevo o reemplaza uno existente.
  // normalizeData elimina prototipos de clases antes de enviar a Firestore.
  const plainData = normalizeData(data);
  await collections[collectionName].doc(id).set(plainData);
  return plainData;
};

const update = async (collectionName, id, data) => {
  // Actualiza parcialmente un documento existente.
  // Si el documento no existe devuelve null para que el controlador responda 404.
  const reference = collections[collectionName].doc(id);
  const document = await reference.get();

  if (!document.exists) {
    return null;
  }

  const updated = {
    ...document.data(),
    ...normalizeData(data),
    id
  };

  await reference.set(updated);
  return updated;
};

const whereEquals = async (collectionName, field, value) => {
  // Filtra documentos por igualdad en un campo.
  const snapshot = await collections[collectionName].where(field, "==", value).get();
  return snapshot.docs.map(toPlainObject);
};

module.exports = {
  getAll,
  getById,
  save,
  update,
  whereEquals
};
