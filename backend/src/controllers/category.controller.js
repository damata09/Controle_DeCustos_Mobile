const categoryService = require('../services/category.service');

async function create(req, res, next) {
  try {
    const { nome, descricao } = req.body;
    if (!nome || typeof nome !== 'string' || nome.trim() === '') {
      return res.status(400).json({ error: 'O campo "nome" é obrigatório e deve ser uma string.' });
    }
    const category = await categoryService.createCategory({ nome: nome.trim(), descricao });
    return res.status(201).json(category);
  } catch (error) {
    next(error);
  }
}

async function getAll(req, res, next) {
  try {
    const categories = await categoryService.getCategories();
    return res.status(200).json(categories);
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
    const category = await categoryService.getCategoryById(id);
    if (!category) {
      return res.status(404).json({ error: 'Categoria não encontrada.' });
    }
    return res.status(200).json(category);
  } catch (error) {
    next(error);
  }
}

async function update(req, res, next) {
  try {
    const { id } = req.params;
    const { nome, descricao } = req.body;

    if (isNaN(id)) {
      return res.status(400).json({ error: 'O ID informado deve ser um número inteiro válido.' });
    }
    if (!nome || typeof nome !== 'string' || nome.trim() === '') {
      return res.status(400).json({ error: 'O campo "nome" é obrigatório para atualização e deve ser uma string.' });
    }

    // Check if exists
    const categoryExists = await categoryService.getCategoryById(id);
    if (!categoryExists) {
      return res.status(404).json({ error: 'Categoria não encontrada.' });
    }

    const updatedCategory = await categoryService.updateCategory(id, { nome: nome.trim(), descricao });
    return res.status(200).json(updatedCategory);
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

    const categoryExists = await categoryService.getCategoryById(id);
    if (!categoryExists) {
      return res.status(404).json({ error: 'Categoria não encontrada.' });
    }

    await categoryService.deleteCategory(id);
    return res.status(200).json({ message: 'Categoria excluída com sucesso.' });
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
