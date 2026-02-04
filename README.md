# events-java

![Java](https://img.shields.io/badge/Java-21-blue?logo=java)
![Maven](https://img.shields.io/badge/Maven-3.9.0-red?logo=apachemaven)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.2-green?logo=springboot)
![Build](https://img.shields.io/badge/build-passing-brightgreen)

Projeto de teste para treinar e criar conhecimento com Java e Spring Boot.  
Esta API simula um sistema de gerenciamento de **eventos**, com a possibilidade de adicionar **cupons** aos eventos.

---

## Funcionalidades
- Criar, listar, atualizar e deletar eventos.
- Adicionar e gerenciar cupons de desconto associados aos eventos.
- API documentada via Swagger.

---

## Tecnologias e Dependências
- **Java 21**
- **Spring Boot 4**
- **Spring Data JPA**
- **PostgreSQL** (banco de dados)
- **Lombok** (redução de boilerplate)
- **MapStruct** (mapeamento de DTOs)
- **SpringDoc OpenAPI** (documentação da API)
- **Spring Boot DevTools** (para desenvolvimento)
- **Spring Boot Starter Test** (testes unitários e de integração)

---

## Documentação da API
A documentação está disponível via Swagger em:  
[http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)

Ela mostra todos os endpoints disponíveis, parâmetros, request/response e exemplos de uso.

---

## Exemplos de Requisição

**Criar Evento**
```bash
POST
Content-Type: application/json

{
  "name": "Workshop Spring Boot",
  "description": "Aprenda Spring Boot do zero",
  "date": "2026-03-10T10:00:00"
}
```
**Adicionar cupom**
```bash
POST /events/{id}/coupons
Content-Type: application/json

{
  "code": "SPRING10",
  "discount": 10
}
```

---

### Como começar

Clone o repositório e rode a aplicação com Maven:

git clone https://github.com/julyana-gusmao/events-java.git
cd events-java
mvn spring-boot:run

---

### Rodando o teste
```bash
mvn test
```

