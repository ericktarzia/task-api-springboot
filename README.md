# Task API - Spring Boot

API REST para gerenciamento de tarefas, desenvolvida com foco em boas práticas, arquitetura em camadas e padrões de mercado.

## 🚀 Tecnologias

* Java 17
* Spring Boot
* Spring Data JPA
* H2 Database
* Lombok
* Swagger (OpenAPI)

## 📁 Arquitetura

O projeto segue separação em camadas:

* Controller → entrada HTTP
* Service → regras de negócio
* Repository → acesso a dados
* DTO → comunicação externa
* Exception → tratamento global de erros

## 📌 Funcionalidades

* Criar tarefa
* Listar tarefas (com paginação)
* Atualizar tarefa (PUT)
* Atualização parcial (PATCH)
* Deletar tarefa

## 🔄 Paginação

Exemplo:

GET /tasks?page=0&size=10

## 📖 Documentação

Swagger disponível em:

http://localhost:8080/swagger-ui/index.html

## 🧪 Executando o projeto

```bash
mvn spring-boot:run
```

## 💡 Observações

* Banco H2 em memória (dados não persistem após reiniciar)
* Projeto focado em estrutura e boas práticas

## 📬 Autor

Erick Tarzia
