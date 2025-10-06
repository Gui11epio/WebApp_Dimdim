# 🌐 DimDim WebApp — Spring Boot + Azure SQL + Application Insights

> 💡 Sistema web Java para gestão de **clientes e pedidos**, com persistência no **Azure SQL Database**, monitoramento via **Application Insights** e deploy automatizado no **Azure App Service**.

---

## 🧱 Estrutura do Projeto

dimdim-webapp-java/
├─ pom.xml

├─ src/

│ ├─ main/

│ │ ├─ java/com/dimdim/

│ │ │ ├─ DimdimApplication.java

│ │ │ ├─ controller/

│ │ │ │ ├─ ClienteController.java

│ │ │ │ └─ PedidoController.java

│ │ │ ├─ service/

│ │ │ │ ├─ ClienteService.java

│ │ │ │ └─ PedidoService.java

│ │ │ ├─ repository/

│ │ │ │ ├─ ClienteRepository.java

│ │ │ │ └─ PedidoRepository.java

│ │ │ └─ model/

│ │ │ ├─ Cliente.java

│ │ │ └─ Pedido.java

│ │ └─ resources/

│ │ ├─ application.properties

│ │ └─ data.sql (opcional)

└─ ddl/

└─ dimdim_schema.sql


---

## ⚙️ Tecnologias Utilizadas

| Categoria | Tecnologia |
|------------|-------------|
| ☕ Backend | Spring Boot 3.2 |
| 🧩 ORM | Spring Data JPA |
| 🗄️ Banco de Dados | Azure SQL Server |
| 🧰 Build | Maven |
| 📊 Monitoramento | Azure Application Insights |
| 🔍 Health Check | Spring Boot Actuator |
| 🚀 Deploy | Azure App Service |
| 💬 Utilitário | Lombok (opcional) |

---

## 🧩 Dependências principais (`pom.xml`)

```xml
<dependencies>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
  </dependency>

  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
  </dependency>

  <dependency>
    <groupId>com.microsoft.sqlserver</groupId>
    <artifactId>mssql-jdbc</artifactId>
    <version>12.3.0.jre17</version>
  </dependency>

  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
  </dependency>

  <dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
  </dependency>
</dependencies>
```

💾 Configuração do Banco de Dados

Arquivo: src/main/resources/application.properties
```
spring.datasource.url=jdbc:sqlserver://<AZURE_SQL_SERVER>.database.windows.net:1433;database=<DB_NAME>;encrypt=true;trustServerCertificate=false;loginTimeout=30;
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.SQLServerDialect
```
management.endpoints.web.exposure.include=health,info,metrics,prometheus


🧠 Script DDL — ddl/dimdim_schema.sql
```
CREATE TABLE cliente (
  id BIGINT IDENTITY(1,1) PRIMARY KEY,
  nome NVARCHAR(200) NOT NULL,
  email NVARCHAR(200) NULL
);

CREATE TABLE pedido (
  id BIGINT IDENTITY(1,1) PRIMARY KEY,
  descricao NVARCHAR(500) NOT NULL,
  custo DECIMAL(12,2) NOT NULL,
  data_criacao DATETIME2 DEFAULT SYSUTCDATETIME(),
  cliente_id BIGINT NOT NULL,
  CONSTRAINT FK_pedido_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);
```

🔧 Endpoints Principais (REST API)

| Método   | Endpoint                           | Descrição                   |
| -------- | ---------------------------------- | --------------------------- |
| `GET`    | `/api/clientes`                    | Lista todos os clientes     |
| `GET`    | `/api/clientes/{id}`               | Busca cliente por ID        |
| `POST`   | `/api/clientes`                    | Cria novo cliente           |
| `PUT`    | `/api/clientes/{id}`               | Atualiza cliente            |
| `DELETE` | `/api/clientes/{id}`               | Remove cliente              |
| `GET`    | `/api/pedidos`                     | Lista todos os pedidos      |
| `POST`   | `/api/pedidos`                     | Cria novo pedido            |
| `GET`    | `/api/pedidos/cliente/{clienteId}` | Lista pedidos de um cliente |

🧪 Exemplos de Requisições
➕ Criar Cliente
```
curl -X POST https://<app>.azurewebsites.net/api/clientes \
 -H "Content-Type: application/json" \
 -d '{"nome":"João Silva","email":"joao@ex.com"}'
```
📋 Listar Clientes
```
curl https://<app>.azurewebsites.net/api/clientes
```
🛠️ Criar Pedido
```
curl -X POST https://<app>.azurewebsites.net/api/pedidos \
 -H "Content-Type: application/json" \
 -d '{"descricao":"Troca de óleo","custo":250.00,"cliente":{"id":1}}'
```
☁️ Deploy no Azure (via IntelliJ)

Instale o plugin Azure Toolkit for IntelliJ

Gere o .jar:

mvn clean package


Crie um App Service com runtime Java 17 (Java SE)

Configure variáveis de ambiente:

DB_USER

DB_PASSWORD

APPLICATIONINSIGHTS_CONNECTION_STRING

Faça deploy pelo IntelliJ → Azure → Deploy Artifact

Teste:
```
https://<seu-app>.azurewebsites.net/actuator/health
```
📊 Monitoramento com Application Insights

Crie um recurso Application Insights no Azure

Copie a Connection String

Adicione ao App Service:

APPLICATIONINSIGHTS_CONNECTION_STRING = InstrumentationKey=...


Faça upload do applicationinsights-agent.jar

Adicione JVM Option:

-javaagent:/home/site/wwwroot/applicationinsights-agent.jar


Verifique métricas, requisições e exceções diretamente no portal do Azure.
