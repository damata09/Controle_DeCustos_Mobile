const prisma = require('../database/prisma');

async function createCost(data) {
  return await prisma.custo.create({
    data: {
      descricao: data.descricao,
      valor: parseFloat(data.valor),
      data: data.data ? new Date(data.data) : new Date(),
      categoria_id: Number(data.categoria_id)
    },
    include: {
      categoria: true
    }
  });
}

async function getCosts() {
  return await prisma.custo.findMany({
    include: {
      categoria: true
    },
    orderBy: {
      data: 'desc'
    }
  });
}

async function getCostById(id) {
  return await prisma.custo.findUnique({
    where: { id: Number(id) },
    include: {
      categoria: true
    }
  });
}

async function updateCost(id, data) {
  return await prisma.custo.update({
    where: { id: Number(id) },
    data: {
      descricao: data.descricao,
      valor: data.valor ? parseFloat(data.valor) : undefined,
      data: data.data ? new Date(data.data) : undefined,
      categoria_id: data.categoria_id ? Number(data.categoria_id) : undefined
    },
    include: {
      categoria: true
    }
  });
}

async function deleteCost(id) {
  return await prisma.custo.delete({
    where: { id: Number(id) }
  });
}

module.exports = {
  createCost,
  getCosts,
  getCostById,
  updateCost,
  deleteCost
};
