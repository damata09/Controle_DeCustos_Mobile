-- Script de criação das tabelas para o banco de dados PostgreSQL (Neon / Supabase)

-- Criar tabela Categoria se não existir
CREATE TABLE IF NOT EXISTS "Categoria" (
    "id" SERIAL PRIMARY KEY,
    "nome" VARCHAR(255) NOT NULL,
    "descricao" VARCHAR(500)
);

-- Criar tabela Custo se não existir
CREATE TABLE IF NOT EXISTS "Custo" (
    "id" SERIAL PRIMARY KEY,
    "descricao" VARCHAR(255) NOT NULL,
    "valor" DECIMAL(10, 2) NOT NULL,
    "data" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "categoria_id" INTEGER NOT NULL,
    CONSTRAINT "fk_categoria" FOREIGN KEY ("categoria_id") REFERENCES "Categoria" ("id") ON DELETE CASCADE
);
