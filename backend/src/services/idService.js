const createId = (prefix) => {
  // Genera identificadores simples para documentos creados por la API.
  return `${prefix}-${Date.now()}-${Math.random().toString(16).slice(2)}`;
};

module.exports = {
  createId
};
