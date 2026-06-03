require('dotenv').config();
const path = require('path');
const express = require('express');
const cors = require('cors');
const categoryRoutes = require('./routes/category.routes');
const costRoutes = require('./routes/cost.routes');

const app = express();
const PORT = process.env.PORT || 3000;

// Configuração de CORS para permitir acesso de qualquer origem (essencial para Android e testes)
app.use(cors());

// Middleware para processar JSON nas requisições
app.use(express.json());

// Rotas principais
app.use('/categorias', categoryRoutes);
app.use('/custos', costRoutes);
app.use('/preview', express.static(path.join(__dirname, '../preview')));

// Rota raiz para verificação de status
app.get('/', (req, res) => {
  res.status(200).json({
    status: 'online',
    message: 'API de Controle de Custos Pessoais/Empresariais ativa.',
    endpoints: {
      categorias: '/categorias',
      custos: '/custos'
    }
  });
});

// Middleware Global de Tratamento de Erros
app.use((err, req, res, next) => {
  console.error('Erro detectado na aplicação:', err);
  
  // Tratamento de erros comuns do Prisma
  if (err.code === 'P2025') {
    return res.status(404).json({ error: 'Registro não encontrado no banco de dados.' });
  }
  if (err.code === 'P2003') {
    return res.status(400).json({ error: 'Falha de restrição de chave estrangeira (relação inválida).' });
  }
  
  res.status(500).json({
    error: 'Ocorreu um erro interno no servidor.',
    message: err.message || 'Erro inesperado.'
  });
});

// Escuta em todas as interfaces (emulador 10.0.2.2 e celular na mesma rede)
app.listen(PORT, '0.0.0.0', () => {
  console.log(`Servidor rodando com sucesso na porta ${PORT}`);
  console.log(`Local:  http://localhost:${PORT}`);
  console.log(`Rede:   use o IP do PC na mesma Wi-Fi (ex: http://192.168.x.x:${PORT})`);
});

module.exports = app;
