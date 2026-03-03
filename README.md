# 📄 CatholicFile — Gerador de Folhetos de Cânticos

API backend desenvolvida em **Java + Spring Boot** para geração e gerenciamento de folhetos de cânticos utilizados em celebrações.

O sistema permite cadastrar usuários, montar folhetos com seções opcionais e futuramente gerar PDFs formatados em duas colunas.

---

## 🚀 Tecnologias utilizadas

* Java 17
* Spring Boot
* Spring Data JPA
* Spring Security
* Maven
* Lombok
* Bean Validation
* (em breve) geração de PDF
* (em breve) Angular no frontend

---

## 📌 Funcionalidades atuais

* ✅ Cadastro de usuários
* ✅ Atualização de usuários
* ✅ Listagem de usuários
* ✅ Exclusão de usuários
* ✅ Estrutura inicial de folhetos
* ✅ Relacionamento entre entidades (JPA)

---

## 🧠 Arquitetura

O projeto segue boas práticas de backend:

* **Controller** → recebe requisições HTTP
* **Service** → regras de negócio
* **Repository** → acesso ao banco
* **DTO** → comunicação da API
* **Entity** → mapeamento JPA

---

## 📂 Estrutura do projeto

```text
src/main/java/com/catholicFile/catholicFile
├── Configurations
├── controller
├── DTOs
├── entities
├── enums
├── infra
├── repositories
├── services
├── usuarios



```

---

## ⚙️ Como executar o projeto

### 1️⃣ Clonar o repositório

```bash
git clone https://github.com/RaissaMatosDev/catholicFile.git
```

### 2️⃣ Entrar na pasta

```bash
cd catholicFile
```

### 3️⃣ Rodar a aplicação

```bash
./mvnw spring-boot:run
```

Ou execute pela sua IDE.

---

## 🔮 Próximos passos (roadmap)

* 🔲 Geração de PDF dos folhetos
* 🔲 Layout em duas colunas
* 🔲 Seções opcionais configuráveis
* 🔲 Autenticação JWT
* 🔲 Frontend em Angular
* 🔲 Deploy em nuvem

---

## 👩‍💻 Autora

**Raíssa Matos**
Desenvolvedora Java Júnior
Foco em desenvolvimento backend com Java e Spring.

---

## ⭐ Objetivo do projeto

Este projeto faz parte do meu portfólio profissional, com foco em:

* boas práticas com Spring
* modelagem JPA
* APIs REST
* arquitetura em camadas
* evolução incremental do sistema

---

💡 Projeto em desenvolvimento contínuo.
