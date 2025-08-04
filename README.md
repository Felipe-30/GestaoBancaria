Projeto: Gestão Bancária API
============================

1 Descrição
------------
API REST em Java com Spring Boot para simular uma aplicação bancária simples. Permite criar contas, consultar saldo, realizar transações com cálculo de taxas e manter histórico de transações.

2 Tecnologias Utilizadas
--------------------------
- Java 17
- Spring Boot 3.2.5
- Maven
- Spring Web
- Spring Data JPA
- Banco H2 (para testes)
- JUnit 5 + AssertJ + Mockito (para testes)

3 Funcionalidades
------------------
- Criar conta com saldo inicial (não pode ser negativo)
- Consultar uma conta pelo número
- Realizar transações com cálculo de taxas:
  - Pix: 0%
  - Débito: 3%
  - Crédito: 5%
- Armazenar transações e consultar histórico por:
  - Número da conta
  - Forma de pagamento
  - Valor mínimo
  - Intervalo de datas
  - Ordenadas por data

4 Como Executar o Projeto
--------------------------
Pré-requisitos:
- Java 17
- Maven
- Git

Passos:
1. Clone o repositório:
   git clone https://github.com/Felipe-30/GestaoBancaria.git

2. Acesse a pasta do projeto:
   cd GestaoBancaria

3. Execute com Maven:
   mvn spring-boot:run

4. Acesse via navegador ou ferramentas como Postman:
   http://localhost:8080

5 Como Rodar os Testes:
------------------------
1. Execute o comando: mvn test

2. Todos os testes unitários e de integração serão executados automaticamente.

6 Endpoints Disponíveis
------------------------

6.1. Conta

- Criar Conta
  - POST /conta
  - Body: { "numero_conta": 123, "saldo": 100.00 }

- Consultar Conta
  - GET /conta?numero_conta=123

6.2. Transação

- Realizar Transação
  - POST /transacao
  - Body: { "numero_conta": 123, "forma_pagamento": "C", "valor": 10.00 }

- Listar Todas Transações por Conta
  - GET /transacao/todas?numero_conta=123

- Listar por Forma de Pagamento
  - GET /transacao/forma-pagamento?numero_conta=123&forma=C

- Listar por Valor Mínimo
  - GET /transacao/valor-minimo?numero_conta=123&valor=50

- Listar Ordenadas por Data (desc)
  - GET /transacao/ordenadas?numero_conta=123

- Listar por Intervalo de Datas
  - GET /transacao/intervalo?numero_conta=123&inicio=2025-08-01T00:00:00&fim=2025-08-04T23:59:59

7 Organização e Diferenciais
-----------------------------

- Projeto completo, funcional e preparado para expansão futura
- Código limpo, legível e modularizado em camadas (Controller, Service, Repository, DTOs)
- Utilização dos padrões de projeto Repository, Service Layer e DTO para melhor organização e manutenção
- Cobertura abrangente de testes unitários e de integração utilizando JUnit 5, AssertJ e Mockito
- Uso do banco H2 em memória para testes de integração, garantindo isolamento e confiabilidade
- Persistência de dados com Spring Data JPA, facilitando operações e consultas avançadas
- Registro detalhado e armazenamento do histórico de transações, com consultas flexíveis por filtros diversos
- Validações robustas de regras de negócio e tratamento consistente de exceções
- Commits descritivos e organizados, refletindo boas práticas de versionamento  
