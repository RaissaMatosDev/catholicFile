# 📄 CatholicFile — API de Automação de Folhetos Litúrgicos

O CatholicFile é uma API backend desenvolvida em Java 17 e Spring Boot para automatizar a criação de folhetos de cânticos utilizados em celebrações religiosas.

Antes, todo o processo era manual, feito em editores de texto como Word, exigindo tempo, organização e atenção constante a detalhes. Como responsável por essa tarefa, desenvolvi esta solução de forma voluntária para reduzir o tempo de criação, padronizar os folhetos e eliminar erros manuais.

Atualmente, o sistema está em uso real, com potencial de expansão para outras comunidades.

---

## 🚀 Testar a API (Online)

A aplicação está disponível em produção.

👉 Acesse diretamente:
https://catholicfile.onrender.com/swagger-ui/index.html

⚠️ Observação:
- Pode levar alguns segundos para responder na primeira requisição (cold start do Render)

---

## 💡 Problema Resolvido

A criação de folhetos litúrgicos envolve:

- Montagem manual de conteúdo
- Repetição de estruturas
- Alto risco de erros de formatação
- Tempo elevado de preparação

👉 O CatholicFile automatiza esse processo, permitindo gerar folhetos completos em poucos minutos.

---

## ⚙️ Solução Implementada

- Criação independente de seções reutilizáveis
- Associação de seções a múltiplos folhetos
- Ordenação automática baseada em regras litúrgicas (Enum)
- Geração dinâmica de PDF com layout pronto para impressão (duas colunas)
- Filtros avançados e paginação para escalabilidade

---

## 🚀 Tecnologias Utilizadas

- Java 17
- Spring Boot 3
- Spring Security + JWT
- Spring Data JPA / Hibernate
- PostgreSQL
- Thymeleaf
- OpenHTMLtoPDF
- Swagger (OpenAPI 3)

---

## 🔐 Segurança

- Autenticação baseada em JWT
- Controle de acesso por roles (RBAC)
- Apenas administradores podem:
  - Criar outros administradores
  - Acessar endpoints sensíveis

---

## 🧠 Arquitetura

A aplicação segue arquitetura em camadas e princípios SOLID:

- **Controller** → endpoints REST  
- **Service** → regras de negócio  
- **Repository** → acesso a dados  
- **DTO** → transporte seguro  
- **Infra** → segurança e tratamento de exceções  
- ++
---

## 📂 Estrutura do Projeto


src/main/java/com/catholicfile/catholicfile
├── configurations
├── controller
├── dtos
├── entities
├── enums
├── infra
├── repositories
└── services


---

## 📸 Documentação da API

![WhatsApp Image 2026-03-24 at 15 05 05](https://github.com/user-attachments/assets/8abb6ae0-ce6b-4b00-a025-97a4a7dee72d)
![WhatsApp Image 2026-03-24 at 15 06 25](https://github.com/user-attachments/assets/2717a673-4d03-406c-bd54-71f65761a1c3)
![WhatsApp Image 2026-03-24 at 21 12 35](https://github.com/user-attachments/assets/205481da-a204-4871-9fa9-ce7661156773)

---

## 🎯 Impacto

- Redução significativa no tempo de criação de folhetos  
- Eliminação de erros manuais  
- Padronização de documentos  
- Uso real em ambiente produtivo  

---

## 🔮 Roadmap

- [ ] Desenvolvimento de frontend (Angular)
- [ ] Testes automatizados
- [ ] Migração para infraestrutura paga
- [ ] Melhorias na performance do PDF

---

## 👩‍💻 Autora

Raíssa Matos  
Desenvolvedora Backend Java  

Projeto desenvolvido de forma voluntária com foco em resolver problemas reais.
