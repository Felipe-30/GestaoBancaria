Gestão Bancária API
Este projeto é uma API REST desenvolvida em Java com Spring Boot, voltada para a simulação de uma aplicação bancária simples. É possível criar contas, consultar saldo e realizar transações com cálculo de taxa, dependendo da forma de pagamento (Pix, Débito ou Crédito).

Tecnologias utilizadas 

A aplicação foi construída com as seguintes tecnologias:

- Java 17

- Spring Boot 3.2.5

- Maven para gerenciamento de dependências

- API REST utilizando Spring Web

Funcionalidades

- A API permite:

- Criar conta com saldo inicial (não pode ser negativo)

- Consultar uma conta pelo número

- Realizar transações de pagamento com cálculo de taxas

- Validar saldo e existência de conta

- Evitar transações com valor inválido ou saldo insuficiente

Como executar o projeto localmente

Pré-requisitos: é necessário ter instalado Java 17, Maven e Git.

1 - Clone este repositório:
git clone https://github.com/Felipe-30/GestaoBancaria.git

2 - Acesse a pasta do projeto:
cd GestaoBancaria

3 - Execute o projeto com Maven:
mvn spring-boot:run

4 - A aplicação estará rodando em:
http://localhost:8080

Exemplos de uso

- Criar conta (POST /conta):

  - Envie um JSON como:
  { "numero_conta": 234, "saldo": 180.37 }


- Consultar conta (GET /conta?numero_conta=234):

    - Retorna os dados da conta especificada.


- Realizar transação (POST /transacao):

    - Envie um JSON como:{ "forma_pagamento": "D", "numero_conta": 234, "valor": 10 } 
    - Onde D é Débito, C é Crédito e P é Pix.

Sobre o desenvolvimento

Durante o desenvolvimento, foi priorizado:

- Separação clara entre camadas (model, repository, service, controller)

- Código limpo e bem organizado

- Commits descritivos para cada etapa do desenvolvimento

- Preparação do projeto para expansão futura

- Validações robustas de regras de negócio

Diferenciais implementados

- Projeto completo e funcional

- Boas práticas de arquitetura em camadas

- README explicativo

- Organização e clareza no código

- Preparado para testes e integração futura com persistência real

Contato

Dúvidas ou sugestões podem ser enviadas via GitHub ou por e-mail.