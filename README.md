### 📄 CatholicFile — Gerador de Folhetos de Cânticos
API backend desenvolvida em Java 17 + Spring Boot para o gerenciamento e geração automatizada de folhetos litúrgicos. O sistema permite a montagem de folhetos com seções dinâmicas, ordenação inteligente e exportação direta para PDF, otimizada para celebrações religiosas.

## 🚀 Tecnologias e Bibliotecas
- Linguagem: Java 17.

- Framework: Spring Boot 3.

- Persistência: Spring Data JPA & Hibernate.

- Banco de Dados: PostgreSQL (Produção/Dev).

- Segurança: Spring Security & JWT (JSON Web Token).


- Geração de PDF: OpenHTMLtoPDF (baseado em Flying Saucer) com suporte a CSS3 e Fast-mode.

- Template Engine: Thymeleaf (para renderização dinâmica de HTML para PDF).

- Documentação: Swagger (OpenAPI 3).

## 📌 Funcionalidades Implementadas
# 📄 Gestão de Folhetos & PDF

- Geração de PDF Dinâmico: Exportação de folhetos em formato PDF 1.7.

- Ordenação Inteligente: As seções do folheto são ordenadas automaticamente com base no ordinal() do Enum (ex: Entrada, Salmo, Ofertório).

- Vínculo Relacional: Gerenciamento de seções vinculadas a folhetos específicos com suporte a operações em cascata corrigidas via Hibernate.

# 🧩 Seções & Filtros Avançados
- Busca Global: Filtro por palavra-chave em títulos e conteúdos.

- Filtros Litúrgicos: Filtragem refinada por Tipo de Seção e Tempo Litúrgico (Advento, Quaresma, Páscoa, etc.).

- Paginação: Listagem paginada e eficiente de todas as seções.

# 🔐 Segurança & Infraestrutura
- Autenticação JWT: Sistema de login com Roles e Tokens de acesso.

- Tratamento de Erros: Exceções customizadas (RecursoNaoEncontradoException) e respostas HTTP padronizadas.

- Prevenção de Recursão: Uso de @JsonIgnore para evitar loops infinitos em relacionamentos Bidirecionais.

# 🧠 Arquitetura do Projeto
O projeto segue os princípios SOLID e uma arquitetura em camadas para garantir manutenibilidade:

- Controller: Exposição dos endpoints REST e documentação Swagger.

- Service: Camada de regras de negócio, ordenação de listas e lógica de geração de PDF.

- Repository: Consultas otimizadas com JPQL para filtros opcionais.

- DTO (Data Transfer Object): Tráfego de dados seguro, evitando a exposição direta das entidades.

- Infra: Configurações de segurança, CORS e manipuladores de erros globais.

## 📂 Estrutura de Pastas
Plaintext
src/main/java/com/catholicfile/catholicfile
├── configurations  # Configurações de Beans e Swagger
├── controller      # Endpoints da API
├── dtos            # Objetos de transferência de dados
├── entities        # Modelos de dados (JPA)
├── enums           # Tipagem (TempoLit, TipoSecao, UserRole)
├── infra           # Segurança e Tratamento de Exceções
├── repositories    # Interfaces de comunicação com o DB
└── services        # Lógica de negócio e PDF
## ⚙️ Como Executar
Clonar:

Bash
git clone https://github.com/RaissaMatosDev/catholicFile.git
Configurar Banco: Certifique-se de que o PostgreSQL está rodando e ajuste o application.properties com suas credenciais.

## Rodar:

Bash
./mvnw spring-boot:run
Documentação: Acesse http://localhost:8080/swagger-ui.html para testar os endpoints.

## 🔮 Roadmap Atualizado
[x] Geração de PDF com OpenHTMLtoPDF.

[x] Filtros dinâmicos por Enum.

[x] Layout do PDF em duas colunas (estilo folheto de missa).

[ ] Implementação do Frontend em Angular.

[ ] Deploy automatizado na Render.

Autora: Raíssa Matos — Junior Java Backend Developer