const costService = require('../services/cost.service');
const categoryService = require('../services/category.service');

async function create(req, res, next) {
  try {
    const { descricao, valor, data, categoria_id } = req.body;

    if (!descricao || typeof descricao !== 'string' || descricao.trim() === '') {
      return res.status(400).json({ error: 'O campo "descricao" é obrigatório.' });
    }
    if (valor === undefined || isNaN(valor) || parseFloat(valor) <= 0) {
      return res.status(400).json({ error: 'O campo "valor" é obrigatório e deve ser um número positivo.' });
    }
    if (!categoria_id || isNaN(categoria_id)) {
      return res.status(400).json({ error: 'O campo "categoria_id" é obrigatório e deve ser um ID numérico válido.' });
    }

    // Verify if category exists
    const categoryExists = await categoryService.getCategoryById(categoria_id);
    if (!categoryExists) {
      return res.status(400).json({ error: 'A categoria informada não existe.' });
    }

    const cost = await costService.createCost({
      descricao: descricao.trim(),
      valor,
      data,
      categoria_id
    });
    return res.status(201).json(cost);
  } catch (error) {
    next(error);
  }
}

async function getAll(req, res, next) {
  try {
    const costs = await costService.getCosts();
    return res.status(200).json(costs);
  } catch (error) {
    next(error);
  }
}

async function getById(req, res, next) {
  try {
    const { id } = req.params;
    if (isNaN(id)) {
      return res.status(400).json({ error: 'O ID informado deve ser um número inteiro válido.' });
    }
    const cost = await costService.getCostById(id);
    if (!cost) {
      return res.status(404).json({ error: 'Custo não encontrado.' });
    }
    return res.status(200).json(cost);
  } catch (error) {
    next(error);
  }
}

async function update(req, res, next) {
  try {
    const { id } = req.params;
    const { descricao, valor, data, categoria_id } = req.body;

    if (isNaN(id)) {
      return res.status(400).json({ error: 'O ID informado deve ser um número inteiro válido.' });
    }

    const costExists = await costService.getCostById(id);
    if (!costExists) {
      return res.status(404).json({ error: 'Custo não encontrado.' });
    }

    // Optional validations if fields are passed
    if (descricao !== undefined && (typeof descricao !== 'string' || descricao.trim() === '')) {
      return res.status(400).json({ error: 'O campo "descricao" não pode ser vazio.' });
    }
    if (valor !== undefined && (isNaN(valor) || parseFloat(valor) <= 0)) {
      return res.status(400).json({ error: 'O campo "valor" deve ser um número positivo.' });
    }
    if (categoria_id !== undefined) {
      if (isNaN(categoria_id)) {
        return res.status(400).json({ error: 'O campo "categoria_id" deve ser um número.' });
      }
      const categoryExists = await categoryService.getCategoryById(categoria_id);
      if (!categoryExists) {
        return res.status(400).json({ error: 'A categoria informada não existe.' });
      }
    }

    const updatedCost = await costService.updateCost(id, {
      descricao: descricao ? descricao.trim() : undefined,
      valor,
      data,
      categoria_id
    });

    return res.status(200).json(updatedCost);
  } catch (error) {
    next(error);
  }
}

async function remove(req, res, next) {
  try {
    const { id } = req.params;
    if (isNaN(id)) {
      return res.status(400).json({ error: 'O ID informado deve ser um número inteiro válido.' });
    }

    const costExists = await costService.getCostById(id);
    if (!costExists) {
      return res.status(404).json({ error: 'Custo não encontrado.' });
    }

    await costService.deleteCost(id);
    return res.status(200).json({ message: 'Custo excluído com sucesso.' });
  } catch (error) {
    next(error);
  }
}

module.exports = {
  create,
  getAll,
  getById,
  update,
  remove
};
