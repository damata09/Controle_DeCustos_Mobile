const { PrismaClient } = require('@prisma/client');
const prisma = new PrismaClient();

async function main() {
  console.log('Iniciando semeadura do banco de dados...');

  // Limpar tabelas existentes
  await prisma.custo.deleteMany();
  await prisma.categoria.deleteMany();

  // Reiniciar sequências se aplicável (pode falhar dependendo da permissão, mas o deleteMany garante tabelas limpas)
  try {
    await prisma.$executeRawUnsafe(`ALTER SEQUENCE "Categoria_id_seq" RESTART WITH 1;`);
    await prisma.$executeRawUnsafe(`ALTER SEQUENCE "Custo_id_seq" RESTART WITH 1;`);
  } catch (e) {
    console.log('Aviso: Não foi possível reiniciar os contadores de ID (normal em alguns servidores em nuvem).');
  }

  // 1. Criar Categorias
  const c1 = await prisma.categoria.create({
    data: { nome: 'Alimentação', descricao: 'Despesas com restaurantes, supermercados, lanches e feiras' }
  });
  const c2 = await prisma.categoria.create({
    data: { nome: 'Transporte', descricao: 'Gastos com combustível, Uber, passagens de ônibus/metrô e manutenção do carro' }
  });
  const c3 = await prisma.categoria.create({
    data: { nome: 'Lazer', descricao: 'Entretenimento, cinema, viagens, shows e atividades recreativas' }
  });
  const c4 = await prisma.categoria.create({
    data: { nome: 'Serviços e Utilidades', descricao: 'Contas recorrentes como energia, água, internet e assinaturas de streaming' }
  });

  console.log('Categorias criadas com sucesso!');

  // 2. Criar Custos
  const custos = [
    { descricao: 'Supermercado Mensal', valor: 489.50, data: new Date('2026-05-10T14:30:00Z'), categoria_id: c1.id },
    { descricao: 'Combustível Posto Ipiranga', valor: 150.00, data: new Date('2026-05-11T09:15:00Z'), categoria_id: c2.id },
    { descricao: 'Ingresso Cinema e Lanche', valor: 45.00, data: new Date('2026-05-12T19:30:00Z'), categoria_id: c3.id },
    { descricao: 'Conta de Luz (Copel/Enel)', valor: 230.40, data: new Date('2026-05-15T08:00:00Z'), categoria_id: c4.id },
    { descricao: 'Almoço Restaurante Silva', valor: 35.00, data: new Date('2026-05-16T12:30:00Z'), categoria_id: c1.id },
    { descricao: 'Corrida de Uber ao Trabalho', valor: 27.50, data: new Date('2026-05-17T07:45:00Z'), categoria_id: c2.id },
    { descricao: 'Mensalidade de Internet Fibra', valor: 99.90, data: new Date('2026-05-20T10:00:00Z'), categoria_id: c4.id },
    { descricao: 'Pizza de Sexta-feira à Noite', valor: 75.00, data: new Date('2026-05-22T21:00:00Z'), categoria_id: c1.id },
    { descricao: 'Assinatura Netflix', valor: 55.90, data: new Date('2026-05-25T04:00:00Z'), categoria_id: c4.id },
    { descricao: 'Lavagem Completa do Carro', valor: 80.00, data: new Date('2026-05-26T15:00:00Z'), categoria_id: c2.id },
    { descricao: 'Show de Rock Local', valor: 120.00, data: new Date('2026-05-28T22:30:00Z'), categoria_id: c3.id }
  ];

  for (const custo of custos) {
    await prisma.custo.create({ data: custo });
  }

  console.log('Custos de exemplo inseridos com sucesso!');
  console.log('Semeadura concluída!');
}

main()
  .catch((e) => {
    console.error('Erro durante o seed:', e);
    process.exit(1);
  })
  .finally(async () => {
    await prisma.$disconnect();
  });
