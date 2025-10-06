## 📘 Sumário

1. [📁 Estrutura do Projeto](#-estrutura-do-projeto)
2. [⚙️ Configuração do `pom.xml`](#️-configuração-do-pomxml)
3. [💻 Código Essencial](#-código-essencial)
4. [🔧 Configuração do `application.properties`](#-configuração-do-applicationproperties)
5. [🧩 Script DDL (SQL Server)](#-script-ddl-sql-server)
6. [☁️ Configurar Banco no Azure SQL (via CLI)](#️-configurar-banco-no-azure-sql-via-cli)
7. [📥 Aplicar DDL no Azure SQL](#-aplicar-ddl-no-azure-sql)
8. [🔗 Configurar Spring Boot para usar Azure SQL](#-configurar-spring-boot-para-usar-azure-sql)
9. [🚀 Build e Deploy da Aplicação no Azure](#-build-e-deploy-da-aplicação-no-azure)
10. [🩺 Health Check e Configuração Final](#-health-check-e-configuração-final)
11. [🧪 Testar Persistência — Endpoints REST](#-testar-persistência--endpoints-rest)
12. [🔍 Verificar Dados no Banco](#-verificar-dados-no-banco)
13. [📊 Monitoramento e Logs (Azure)](#-monitoramento-e-logs-azure)
14. [🧠 Boas Práticas de Segurança e Observabilidade](#-boas-práticas-de-segurança-e-observabilidade)

---

## 📁 Estrutura do Projeto

```bash
dimdim-webapp-java/
├─ pom.xml
├─ src/
│  ├─ main/
│  │  ├─ java/com/dimdim/
│  │  │  ├─ DimdimApplication.java
│  │  │  ├─ controller/
│  │  │  │  ├─ ClienteController.java
│  │  │  │  └─ PedidoController.java
│  │  │  ├─ service/
│  │  │  │  ├─ ClienteService.java
│  │  │  │  └─ PedidoService.java
│  │  │  ├─ repository/
│  │  │  │  ├─ ClienteRepository.java
│  │  │  │  └─ PedidoRepository.java
│  │  │  └─ model/
│  │  │     ├─ Cliente.java
│  │  │     └─ Pedido.java
│  │  └─ resources/
│  │     ├─ application.properties
│  │     └─ data.sql (opcional para seed)
└─ ddl/
   └─ dimdim_schema.sql
```

---

## ⚙️ Configuração do `pom.xml` (Spring Boot + JPA + SQL Server + Actuator)

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" ...>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.dimdim</groupId>
  <artifactId>dimdim-webapp</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <packaging>jar</packaging>

  <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.0</version>
    <relativePath/>
  </parent>

  <properties>
    <java.version>17</java.version>
  </properties>

  <dependencies>
    <!-- Web -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- JPA -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- SQL Server driver -->
    <dependency>
      <groupId>com.microsoft.sqlserver</groupId>
      <artifactId>mssql-jdbc</artifactId>
      <version>12.3.0.jre17</version>
    </dependency>

    <!-- Actuator (health/metrics) -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>

    <!-- Lombok (opcional) -->
    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <optional>true</optional>
    </dependency>

    <!-- Testes -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
      </plugin>
    </plugins>
  </build>
</project>
```

---

## 💻 Código Essencial

### `DimdimApplication.java`

```java
package com.dimdim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DimdimApplication {
  public static void main(String[] args) {
    SpringApplication.run(DimdimApplication.class, args);
  }
}
```

---

### `model/Cliente.java`

```java
package com.dimdim.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "cliente")
public class Cliente {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable=false)
  private String nome;

  @Column
  private String email;

  @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
  private List<Pedido> pedidos;

  // getters e setters
}
```

---

### `model/Pedido.java`

```java
package com.dimdim.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedido")
public class Pedido {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable=false)
  private String descricao;

  @Column(nullable=false)
  private Double custo;

  @Column
  private LocalDateTime dataCriacao;

  @ManyToOne
  @JoinColumn(name = "cliente_id", nullable = false)
  private Cliente cliente;

  // getters e setters
}
```

---

### `repository/ClienteRepository.java`

```java
package com.dimdim.repository;

import com.dimdim.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> { }
```

---

### `repository/PedidoRepository.java`

```java
package com.dimdim.repository;

import com.dimdim.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
  List<Pedido> findByClienteId(Long clienteId);
}
```

---

### `service/ClienteService.java`

```java
package com.dimdim.service;

import com.dimdim.model.Cliente;
import com.dimdim.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
  private final ClienteRepository repo;
  public ClienteService(ClienteRepository repo){ this.repo = repo; }

  public List<Cliente> findAll(){ return repo.findAll(); }
  public Optional<Cliente> findById(Long id){ return repo.findById(id); }
  public Cliente save(Cliente c){ return repo.save(c); }
  public void delete(Long id){ repo.deleteById(id); }
}
```

---

### `controller/ClienteController.java`

```java
package com.dimdim.controller;

import com.dimdim.model.Cliente;
import com.dimdim.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
  private final ClienteService service;
  public ClienteController(ClienteService service){ this.service = service; }

  @GetMapping
  public List<Cliente> all(){ return service.findAll(); }

  @GetMapping("/{id}")
  public ResponseEntity<Cliente> get(@PathVariable Long id){
    return service.findById(id)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public Cliente create(@RequestBody Cliente cliente){ return service.save(cliente); }

  @PutMapping("/{id}")
  public ResponseEntity<Cliente> update(@PathVariable Long id, @RequestBody Cliente cliente){
    return service.findById(id).map(existing -> {
      cliente.setId(existing.getId());
      return ResponseEntity.ok(service.save(cliente));
    }).orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id){
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
```

---

## 🔧 Configuração do `application.properties`

```properties
# Datasource Configuration
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.SQLServerDialect
spring.jpa.properties.hibernate.format_sql=true

# Server Configuration
server.port=8080

# Logging
logging.level.com.dimdim=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate.SQL=DEBUG

# Actuator
management.endpoints.web.exposure.include=health,info
management.endpoint.health.show-details=Always
```

---

## 🧩 Script DDL (SQL Server)

```sql
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

---

## ☁️ Configurar Banco no Azure SQL (via CLI)

```powershell
$RG="rg-dimdim"
$LOCATION="brazilsouth"
$SQL_SERVER="dimdim-sqlsrv"
$SQL_ADMIN="sqladmin"
$SQL_PASS="StrongPassword1!"
$DB_NAME="dimdimdb"
$APP_PLAN="plan-dimdim"
$WEBAPP="dimdim-webapp"

az login

# 1. Resource group
az group create -n $RG -l $LOCATION

# 2. SQL Server
az sql server create -g $RG -n $SQL_SERVER -l $LOCATION -u $SQL_ADMIN -p $SQL_PASS

# 3. Database
az sql db create -g $RG -s $SQL_SERVER -n $DB_NAME --service-objective S0

# 4. Allow your IP
$MY_IP = (Invoke-RestMethod -Uri "https://ipinfo.io/ip").Trim()
az sql server firewall-rule create -g $RG -s $SQL_SERVER -n AllowMyIP --start-ip-address $MY_IP --end-ip-address $MY_IP

# 5. Allow Azure Services
az sql server firewall-rule create -g $RG -s $SQL_SERVER -n AllowAzureServices --start-ip-address 0.0.0.0 --end-ip-address 0.0.0.0

# 6. App Service Plan + Web App
az appservice plan create -g $RG -n $APP_PLAN --is-linux --sku B2
az webapp create -g $RG -p $APP_PLAN -n $WEBAPP --runtime "JAVA:17-java17"
```

---

## 📥 Aplicar DDL no Azure SQL

```powershell
$SQL_SERVER="dimdim-sqlsrv.database.windows.net"
$DB_NAME="dimdimdb"
$USER="sqladmin"
$PASS="StrongPassword1!"

sqlcmd -S $SQL_SERVER -U $USER -P $PASS -d $DB_NAME -i ddl_dimdim.sql
sqlcmd -S $SQL_SERVER -U $USER -P $PASS -d $DB_NAME -Q "SELECT name FROM sys.tables"
```

---

## 🔗 Configurar Spring Boot para usar Azure SQL

```powershell
az webapp config appsettings set --name dimdim-webapp --resource-group rg-dimdim --settings `
  WEBSITES_PORT=8080 `
  JAVA_OPTS="-Dserver.port=8080" `
  SPRING_DATASOURCE_URL="jdbc:sqlserver://dimdim-sqlsrv.database.windows.net;database=dimdimdb;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;" `
  DB_USER="sqladmin@dimdim-sqlsrv" `
  DB_PASSWORD="StrongPassword1!" `
  SPRING_JPA_HIBERNATE_DDL_AUTO="update" `
  SPRING_JPA_SHOW_SQL="true"
```

---

## 🚀 Build e Deploy da Aplicação no Azure

```powershell
# 1. Build
mvn clean package -DskipTests

# 2. Deploy
az webapp deploy --resource-group rg-dimdim --name dimdim-webapp --src-path target/dimdim_webapp-0.0.1-SNAPSHOT.jar --type jar --verbose
```

Monitorar:

```powershell
az webapp log tail --name dimdim-webapp --resource-group rg-dimdim
```

---

## 🩺 Health Check e Configuração Final

```powershell
curl -v https://dimdim-webapp.azurewebsites.net/actuator/health

az webapp config appsettings list --name dimdim-webapp --resource-group rg-dimdim --output table

# Após o primeiro deploy bem-sucedido:
az webapp config appsettings set --name dimdim-webapp --resource-group rg-dimdim --settings SPRING_JPA_HIBERNATE_DDL_AUTO=validate
```

---

## 🧪 Testar Persistência — Endpoints REST

**Criar Cliente**

```bash
POST https://dimdim-webapp.azurewebsites.net/api/clientes
Content-Type: application/json

{
  "nome": "João Silva",
  "email": "joao.silva@email.com"
}
```

**Listar Clientes**

```bash
GET https://dimdim-webapp.azurewebsites.net/api/clientes
```

**Criar Pedido**

```bash
POST https://dimdim-webapp.azurewebsites.net/api/pedidos
Content-Type: application/json

{
  "descricao": "Notebook Dell",
  "valor": 2850.90,
  "clienteId": 1
}
```

**Listar Pedidos**

```bash
GET https://dimdim-webapp.azurewebsites.net/api/pedidos
```

---

## 🔍 Verificar Dados no Banco

```powershell
sqlcmd -S dimdim-sqlsrv.database.windows.net -U sqladmin -P "StrongPassword1!" -d dimdimdb -Q "SELECT * FROM cliente"
sqlcmd -S dimdim-sqlsrv.database.windows.net -U sqladmin -P "StrongPassword1!" -d dimdimdb -Q "SELECT * FROM pedido"
```

---

## 📊 Monitoramento e Logs (Azure)

```powershell
# Logs em tempo real
az webapp log tail --name dimdim-webapp --resource-group rg-dimdim

# Diagnóstico avançado
# https://dimdim-webapp.scm.azurewebsites.net

# Verificar métricas básicas
az webapp show --name dimdim-webapp --resource-group rg-dimdim --query "{name: name, state: state, defaultHostName: defaultHostName, enabled: enabled}"
```

---

## 🧠 Boas Práticas de Segurança e Observabilidade

* **Não coloque** usuário/senha no `application.properties`.
* Configure `DB_USER` e `DB_PASSWORD` em **Application Settings** do App Service.
* Em produção, use **Managed Identity + Key Vault**.
* Ative **HTTPS obrigatório** (App Service já fornece).
* Configure **Application Insights**:

  * Crie um recurso no Azure → copie a `APPLICATIONINSIGHTS_CONNECTION_STRING`
  * Adicione como Application Setting:

    ```
    APPLICATIONINSIGHTS_CONNECTION_STRING=<sua_connection_string>
    ```
  * Adicione o agent:

    ```
    -javaagent:/home/site/wwwroot/applicationinsights-agent-3.x.x.jar
    ```
* Monitore **health checks** (`/actuator/health`), **logs** e **métricas de performance**.


