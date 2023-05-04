# To-Do App - GO

As tabelas de banco de dados não são criadas ou alteradas pela aplicação, mas foi disponibilizado script criado para definição das tabelas.  

![SQL script for the project](/assets/database_sql.jpg "SQL script")

Posteriormente são configuradas as rotas para endpoints da API, para isso foi configurado o mux.Router no pacote de rotas.
As rotas foram configuradas a partir de um array de struct, dessa forma verificando atributos para validar o uso ou não de middleware de autenticação.

![Route and controller configuration for the project](/assets/routes.jpg "routes-config")
![Route and controller configuration for the project](/assets/routes_.jpg "routes-config")
![Route and controller configuration for the project](/assets/tasks-routes.jpg "routes-config")
![Route and controller configuration for the project](/assets/tasks-controller.jpg "routes-config")

Os controllers são cadastrados na configuração das rotas, configurando os endpoints, controllers e método HTTP, os controllers acionam uma camada de negócio.  

![Business Layer for the project](/assets/business-layer.jpg "business layer")

Nessa camada de negócio são realizadas validações, transformação de dados e acionamento da camada DAO.  

![DAO Layer for the project](/assets/dao_layer.jpg "dao layer")

A camada DAO gerencia as conexões com a base de dados e aciona a camada de repositório, responsável por realizar as operações de dados na base de dados.

![Repository Layer for the project](/assets/repository-layer.jpg "repository layer")
