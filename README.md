# Task API

API REST para gerenciamento de tarefas, desenvolvida com Spring Boot com foco em boas práticas, arquitetura em camadas e cobertura de testes.

---

## 🚀 Tecnologias

| Tecnologia | Versão |
|---|---|
| Java | 17 |
| Spring Boot | 3.2.5 |
| Spring Data JPA | — |
| H2 Database | — |
| Lombok | — |
| SpringDoc OpenAPI (Swagger) | 2.5.0 |
| JUnit 5 + Mockito | via `spring-boot-starter-test` |
| JaCoCo | 0.8.12 |

---

## 📁 Arquitetura

O projeto segue separação em camadas:

```
controller   → entrada HTTP, validação de request
service      → regras de negócio
repository   → acesso a dados (Spring Data JPA)
dto          → contratos de entrada e saída
entity       → modelo de dados
exception    → tratamento global de erros (GlobalExceptionHandler)
```

---

## 📌 Endpoints

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/health` | Verifica se a API está online |
| `GET` | `/tasks` | Lista tarefas com paginação |
| `GET` | `/tasks/{id}` | Busca tarefa por ID |
| `POST` | `/tasks` | Cria nova tarefa |
| `PATCH` | `/tasks/{id}` | Atualização parcial (título, descrição, status) |
| `PATCH` | `/tasks/{id}/complete` | Marca tarefa como concluída |
| `DELETE` | `/tasks/{id}` | Remove tarefa |

### Paginação

```
GET /tasks?page=0&size=10
```

### Exemplo — Health check

```json
GET /health
{
  "online": true
}
```

### Exemplo — Criar tarefa

```json
POST /tasks
{
  "title": "Estudar Spring Boot",
  "description": "Revisar documentação oficial"
}
```

### Exemplo — Atualização parcial

```json
PATCH /tasks/1
{
  "title": "Novo título",
  "completed": true
}
```

### Respostas de erro

| Status | Situação |
|---|---|
| `400` | Payload inválido (ex: `title` em branco) |
| `404` | Tarefa não encontrada |

---

## 🧪 Testes

O projeto adota testes em duas camadas:

- **Unitários** (`TaskServiceTest`) — lógica de negócio isolada com Mockito
- **Web** (`TaskControllerWebMvcTest`) — contratos HTTP com MockMvc (`@WebMvcTest`)
- **Exceções** (`GlobalExceptionHandlerTest`) — respostas de erro 400 e 404

### Cobertura (JaCoCo)

| Pacote | Cobertura |
|---|---|
| `controller` | 100% |
| `exception` | 92% |
| `dto` | 91% |
| `service` | 77% |
| **Total** | **~87%** |

### Rodar os testes

```bash
mvn test
```

### Gerar relatório de cobertura

```bash
mvn verify
```

Relatório disponível em: `target/site/jacoco/index.html`

---
![img.png](img.png)
## ▶️ Executando o projeto

```bash
mvn spring-boot:run
```

Depois, acesse:

```bash
curl http://localhost:8080/health
```

---

## 📖 Documentação (Swagger)

```
http://localhost:8080/swagger-ui/index.html
```

---
![img_1.png](img_1.png)
## 💡 Observações

- Banco H2 em memória — dados não persistem após reiniciar
- Validação de entrada com `@Valid` e `@NotBlank`
- Erros retornam corpo padronizado com `message`, `status` e `timestamp`

---

## 📬 Autor

**Erick Tarzia**
