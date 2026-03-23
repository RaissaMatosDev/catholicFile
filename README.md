# 📄 CatholicFile — Gerador de Folhetos de Cânticos

API backend desenvolvida em **Java + Spring Boot** para geração e gerenciamento de folhetos de cânticos utilizados em celebrações.

O sistema permite cadastrar usuários, montar folhetos com seções configuráveis e gerar PDFs formatados para uso real em missas e eventos religiosos.

---

## 🚀 Tecnologias utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- Spring Security
- Maven
- Lombok
- Bean Validation
- PostgreSQL
- Swagger (OpenAPI)
- Geração de PDF

🔧 **Em desenvolvimento:**

- Frontend em Angular

---

## 📌 Funcionalidades

### 👤 Usuários
- ✅ Cadastro de usuários
- ✅ Atualização de usuários
- ✅ Listagem paginada
- ✅ Exclusão de usuários

### 📄 Folhetos
- ✅ Criação de folhetos
- ✅ Associação com seções
- ✅ Gerar PDF
- ✅ Estrutura modular e escalável


### 🧩 Seções
- ✅ Cadastro de seções de cânticos
- ✅ Filtros por:
    - palavra-chave
    - tipo
    - tempo litúrgico
- ✅ Paginação de resultados

### 🔐 Autenticação
- ✅ Login com geração de token
- ✅Integração completa com JWT 

---

## 🧠 Arquitetura

O projeto segue uma arquitetura em camadas bem definida:

- **Controller** → entrada da API (requisições HTTP)
- **Service** → regras de negócio
- **Repository** → acesso ao banco de dados
- **DTO** → comunicação entre camadas
- **Entity** → mapeamento JPA
- **Enums**  → Permissões e classificação dos itens
- **Infra** → Tratamento de Erros
- **Configurations** → Info e definições JWT e configurações de documentação

📌 Foco em:
- Separação de responsabilidades
- Código limpo
- Escalabilidade

---

## 📂 Estrutura do projeto
src/main/java/com/catholicFile/catholicFile
├── configurations
├── controller
├── dtos
├── entities
├── enums
├── infra
├── repositories
├── services


## ⚙️ Como executar o projeto

### 1️⃣ Clonar o repositório

git clone https://github.com/RaissaMatosDev/catholicFile.git

### 2️⃣ Acessar a pasta do projeto
cd catholicFile
### 3️⃣ Executar a aplicação
./mvnw spring-boot:run

Ou execute diretamente pela sua IDE.

## 📄 Documentação da API

Após rodar o projeto, acesse a documentação interativa via Swagger:

http://localhost:8080/swagger-ui.html
## ☁️ Deploy

O deploy da aplicação será realizado na plataforma Render, permitindo acesso público à API para testes e demonstração do projeto em produção.

## 🔮 Roadmap
 Geração de PDF dos folhetos
 Layout em duas colunas
 Seções opcionais configuráveis
 Autenticação com JWT
 Frontend em Angular
 Deploy em nuvem (Render)
## 👩‍💻 Autora

Raíssa Matos
Desenvolvedora Java Júnior
Foco em desenvolvimento backend com Java e Spring Boot

#