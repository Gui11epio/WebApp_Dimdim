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
