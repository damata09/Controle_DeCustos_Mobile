-- Script de inserção de dados de exemplo (semeadura do banco de dados)

-- Inserir Categorias
INSERT INTO "Categoria" ("nome", "descricao") VALUES
('Alimentação', 'Despesas com restaurantes, supermercados, lanches e feiras'),
('Transporte', 'Gastos com combustível, Uber, passagens de ônibus/metrô e manutenção do carro'),
('Lazer', 'Entretenimento, cinema, viagens, shows e atividades recreativas'),
('Serviços e Utilidades', 'Contas recorrentes como energia, água, internet e assinaturas de streaming');

-- Inserir Custos (Mínimo de 10 registros vinculados às categorias inseridas acima)
-- Como o ID é SERIAL, as categorias inseridas terão IDs de 1 a 4.
INSERT INTO "Custo" ("descricao", "valor", "data", "categoria_id") VALUES
('Supermercado Mensal', 489.50, '2026-05-10 14:30:00+00', 1),
('Combustível Posto Ipiranga', 150.00, '2026-05-11 09:15:00+00', 2),
('Ingresso Cinema e Lanche', 45.00, '2026-05-12 19:30:00+00', 3),
('Conta de Luz (Copel/Enel)', 230.40, '2026-05-15 08:00:00+00', 4),
('Almoço Restaurante Silva', 35.00, '2026-05-16 12:30:00+00', 1),
('Corrida de Uber ao Trabalho', 27.50, '2026-05-17 07:45:00+00', 2),
('Mensalidade de Internet Fibra', 99.90, '2026-05-20 10:00:00+00', 4),
('Pizza de Sexta-feira à Noite', 75.00, '2026-05-22 21:00:00+00', 1),
('Assinatura Netflix', 55.90, '2026-05-25 04:00:00+00', 4),
('Lavagem Completa do Carro', 80.00, '2026-05-26 15:00:00+00', 2),
('Show de Rock Local', 120.00, '2026-05-28 22:30:00+00', 3);
