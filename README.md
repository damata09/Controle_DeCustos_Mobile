# Sistema de Controle de Custos Pessoais/Empresariais

Este é um sistema completo para gerenciamento de custos pessoais ou corporativos. O projeto é composto por:
1. **Banco de Dados Relacional**: Scripts SQL para PostgreSQL (compatíveis com Neon, Supabase ou local).
2. **Backend**: Uma API REST desenvolvida em Node.js com Express.js e Prisma ORM.
3. **Frontend**: Um aplicativo Android nativo desenvolvido em Kotlin, estruturado com ViewBinding, RecyclerView e consumo de APIs via Retrofit.

---

## 📁 Estrutura de Pastas do Projeto

```text
Nova pasta/
├── database/                   # Scripts SQL para banco de dados
│   ├── create_tables.sql       # Script de criação das tabelas
│   └── insert_data.sql         # Script de semente (11 registros iniciais)
│
├── backend/                    # API REST em Node.js
│   ├── prisma/
│   │   ├── schema.prisma       # Schema do Prisma para PostgreSQL
│   │   └── seed.js             # Seed script programático em JS
│   ├── src/
│   │   ├── database/           # Wrapper de conexão com banco (prisma.js)
│   │   ├── services/           # Regras de negócio e consultas Prisma
│   │   ├── controllers/        # Controladores HTTP (validações e status)
│   │   ├── routes/             # Rotas do Express (/categorias e /custos)
│   │   └── app.js              # Ponto de entrada e configuração do Express
│   ├── .env.example            # Exemplo de variáveis de ambiente
│   ├── package.json            # Dependências e scripts npm
│   └── costs_system_api_collection.json # Coleção do Postman para testes
│
└── android/                    # Código do Aplicativo Android Nativo
    ├── app/
    │   ├── src/main/
    │   │   ├── java/com/example/costcontrol/
    │   │   │   ├── adapter/    # Adaptadores para RecyclerView
    │   │   │   ├── api/        # Configuração do Retrofit e Endpoints
    │   │   │   ├── model/      # Modelos de dados serializáveis (Category e Cost)
    │   │   │   └── ui/         # Activities divididas por fluxos
    │   │   ├── res/
    │   │   │   ├── layout/     # XMLs das 8 telas de CRUD + itens de lista
    │   │   │   └── values/     # Cores (Slate & Sky Blue), strings e temas
    │   │   └── AndroidManifest.xml # Configurações globais e permissões
    │   └── build.gradle.kts    # Configuração de build e dependências (Retrofit, ViewBinding)
    ├── build.gradle.kts        # Plugins do Gradle
    └── settings.gradle.kts     # Definições do projeto Android
```

---

## 1. Banco de Dados

O banco de dados do sistema possui 2 tabelas principais relacionadas de **1-para-Muitos** (uma categoria pode ter múltiplos custos, enquanto um custo pertence a exatamente uma categoria).

### Estrutura
- **Categoria**: `id` (chave primária autoincremento), `nome` (VARCHAR obrigatório), `descricao` (VARCHAR opcional).
- **Custo**: `id` (chave primária autoincremento), `descricao` (VARCHAR obrigatório), `valor` (DECIMAL obrigatório), `data` (TIMESTAMP obrigatório, padrão atual), `categoria_id` (chave estrangeira referenciando `Categoria(id)` com ação de exclusão em cascata `ON DELETE CASCADE`).

Os scripts SQL de criação e inserção de dados estão disponíveis em:
- [create_tables.sql](file:///c:/Users/henri/Downloads/Nova%20pasta/database/create_tables.sql)
- [insert_data.sql](file:///c:/Users/henri/Downloads/Nova%20pasta/database/insert_data.sql)

---

## 2. Instruções de Execução: Backend (Node.js)

### Pré-requisitos
- Node.js (v16 ou superior) instalado.
- Acesso a um banco de dados PostgreSQL (como Neon.tech, Supabase ou local).

### Passo a Passo
1. Acesse a pasta do backend via terminal:
   ```bash
   cd backend
   ```
2. Instale as dependências necessárias do projeto:
   ```bash
   npm install
   ```
3. Crie e configure o arquivo `.env` com a URL do seu banco de dados PostgreSQL:
   - Você pode copiar o `.env.example` para `.env` e alterar a variável `DATABASE_URL`:
   ```env
   DATABASE_URL="postgresql://USUARIO:SENHA@HOST:PORTA/NOME_BANCO?schema=public"
   PORT=3000
   ```
4. Aplique as migrações e sincronize o banco de dados via Prisma:
   ```bash
   npx prisma db push
   ```
5. Popule o banco de dados com os registros de teste programaticamente:
   ```bash
   npx prisma db seed
   ```
6. Inicie a API em modo de desenvolvimento (reinicializa automaticamente com alterações de arquivos):
   ```bash
   npm run dev
   ```
   *A API estará ativa em `http://localhost:3000`.*

### Arquitetura Utilizada no Backend
A API segue a arquitetura em camadas para maior legibilidade e separação de responsabilidades:
- **Routes**: Mapeia as URLs de requisição e aponta para as funções correspondentes dos controladores.
- **Controllers**: Recebe os parâmetros de entrada, executa validações rigorosas (impede valores nulos, negativos ou tipos incorretos) e retorna as respostas com os códigos de status HTTP apropriados (`200 OK`, `201 Created`, `400 Bad Request`, `404 Not Found`, `500 Server Error`).
- **Services**: Contém a lógica de negócios e chama os métodos do Prisma Client para realizar consultas no banco de dados.
- **Database/Prisma**: Gerencia a conexão ativa com o banco PostgreSQL.

---

## 3. Testes da API (Postman)

Fornecemos um arquivo contendo a coleção completa de testes pronta para importação no Postman ou Insomnia.
- **Arquivo**: [costs_system_api_collection.json](file:///c:/Users/henri/Downloads/Nova%20pasta/backend/costs_system_api_collection.json)

### Como Importar e Usar:
1. Abra o Postman.
2. Clique no botão **Import** no painel esquerdo superior.
3. Arraste ou selecione o arquivo `costs_system_api_collection.json` localizado na pasta do backend.
4. A coleção `Controle de Custos API` será importada com a variável de ambiente `baseUrl` configurada por padrão para `http://localhost:3000`.
5. Execute os testes seguindo a ordem de fluxos (Categorias -> Custos).

---

## 4. Instruções de Execução: Aplicativo Android (Kotlin)

O aplicativo consome a API REST local ou remota através da biblioteca Retrofit, exibindo os dados de forma moderna através de `RecyclerView` e permitindo a edição e exclusão de cada registro individualmente em telas dedicadas.

### Pré-requisitos
- Android Studio instalado.
- SDK do Android (Mínimo API 24 - Android 7.0 Nougat, Compilação API 34).

### Como Rodar no Emulador / Dispositivo Físico:
1. Abra o **Android Studio**.
2. Vá em **File > Open** e selecione o diretório `android` deste projeto.
3. Aguarde a sincronização e indexação dos arquivos do Gradle (isso pode demorar alguns minutos na primeira vez).
4. Verifique a configuração da URL Base em [RetrofitClient.kt](file:///c:/Users/henri/Downloads/Nova%20pasta/android/app/src/main/java/com/example/costcontrol/api/RetrofitClient.kt):
   - Se rodar o backend localmente no mesmo computador e usar o **Emulador do Android**, a URL padrão `http://10.0.2.2:3000/` está correta e funcional.
   - Se utilizar um **Dispositivo Físico (via USB ou Wi-Fi)**, altere para o endereço de IP da sua rede local (ex: `http://192.168.1.100:3000/`).
5. Execute o aplicativo clicando no botão **Run** (ícone de play verde) com o emulador ativo.

### Divisão das 8 Telas Implementadas
O fluxo de telas do aplicativo é separado da seguinte forma:

1. **Dashboard Inicial (MainActivity)**: Menu com opções para abrir o gerenciador de Categorias ou o de Custos.
2. **Fluxo de Categorias**:
   - *Tela 1: Listagem (`CategoryListActivity`)*: Lista todas as categorias vindas da API em um RecyclerView. Contém botões individuais em cada item para "Editar" ou "Excluir".
   - *Tela 2: Cadastro (`CategoryCreateActivity`)*: Formulário com campos de nome e descrição para inserir uma nova categoria.
   - *Tela 3: Edição (`CategoryEditActivity`)*: Carrega os dados da categoria selecionada e executa a atualização na API.
   - *Tela 4: Exclusão (`CategoryDeleteActivity`)*: Apresenta os dados da categoria e solicita confirmação do usuário com aviso sobre a remoção em cascata dos custos vinculados.
3. **Fluxo de Custos**:
   - *Tela 5: Listagem (`CostListActivity`)*: Lista todos os custos com formatação brasileira de valores (R$) e data amigável, exibindo a categoria associada como uma tag colorida.
   - *Tela 6: Cadastro (`CostCreateActivity`)*: Formulário para registrar custos. O campo de categoria é um Spinner carregado dinamicamente via API com as categorias disponíveis.
   - *Tela 7: Edição (`CostEditActivity`)*: Permite alterar dados do custo selecionado, incluindo reassociar o custo a uma categoria diferente utilizando o Spinner.
   - *Tela 8: Exclusão (`CostDeleteActivity`)*: Tela de confirmação antes de remover definitivamente um registro do banco de dados.

---

## 5. Capturas de Tela Recomendadas para Apresentação Acadêmica

Ao compilar e rodar o projeto, capture capturas de tela (prints) nos seguintes momentos para enriquecer seu relatório:

1. **API**:
   - Resposta do endpoint raiz `/` no navegador ou Postman (Status Online).
   - Teste de listagem no Postman (`GET /categorias` ou `GET /custos`) mostrando o JSON retornado com o status `200 OK`.
2. **Aplicativo Android**:
   - **Dashboard**: A tela inicial mostrando as opções de navegação.
   - **Lista de Categorias**: Exibindo os 4 registros inseridos pelo script de seed.
   - **Cadastro de Categorias**: Formulário com preenchimento ou validação de erro (caso tente salvar em branco).
   - **Lista de Custos**: Demonstrando a tags de categoria coloridas (como "Alimentação") e a formatação de dinheiro e datas.
   - **Confirmação de Exclusão**: Tela vermelha de aviso de deleção de categorias destacando a exclusão em cascata.
