# BFF Gateway — Audiência Virtual

## Execução

O BFF roda em `8084` e encaminha para a API de domínio em `8082`.

```powershell
cd C:\Users\kesia\Desktop\projetos-tjba\audiencia-virtual-bff
.\mvnw.cmd spring-boot:run
```

Swagger UI: <http://localhost:8084/swagger-ui.html>

OpenAPI JSON: <http://localhost:8084/v3/api-docs>

Documentação integrada: [DOCUMENTACAO-INTEGRADA-APIS.md](../api-audiencia-virtual/DOCUMENTACAO-INTEGRADA-APIS.md).

## Rotas públicas

| Método | Rota                                  |
| ------ | ------------------------------------- |
| `GET`  | `/api/v1/bff/audiencias`              |
| `GET`  | `/api/v1/bff/audiencias/{id}`         |
| `GET`  | `/api/v1/bff/audiencias/buscar?nome=` |
| `POST` | `/api/v1/bff/audiencias`              |

O cadastro recebe `agendaNome`, `parteNome`, `parteCpf`, `parteNumeroProcesso`,
`email`, `dataAudiencia` e `siteAgendamento`. O BFF propaga `Authorization` e
`X-Correlation-Id` para a API.
