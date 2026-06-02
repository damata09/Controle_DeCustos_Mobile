const prisma = require('../database/prisma');

async function createCategory(data) {
  return await prisma.categoria.create({
    data: {
      nome: data.nome,
      descricao: data.descricao
    }
  });
}

async function getCategories() {
  return await prisma.categoria.findMany({
    orderBy: {
      id: 'asc'
    }
  });
}

async function getCategoryById(id) {
  return await prisma.categoria.findUnique({
    where: { id: Number(id) }
  });
}

async function updateCategory(id, data) {
  return await prisma.categoria.update({
    where: { id: Number(id) },
    data: {
      nome: data.nome,
      descricao: data.descricao
    }
  });
}

async function deleteCategory(id) {
  return await prisma.categoria.delete({
    where: { id: Number(id) }
  });
}

module.exports = {
  createCategory,
  getCategories,
  getCategoryById,
  updateCategory,
  deleteCategory
};
