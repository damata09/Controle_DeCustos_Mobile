const express = require('express');
const router = express.Router();
const costController = require('../controllers/cost.controller');

router.post('/', costController.create);
router.get('/', costController.getAll);
router.get('/:id', costController.getById);
router.put('/:id', costController.update);
router.delete('/:id', costController.remove);

module.exports = router;
