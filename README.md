# agenda-backend
agenda com lista de contatos - engenharia de software UNEB 

O objetivo deste projeto é a construção de um conjunto de classes que 
permitam a criação de uma agenda. A agenda por definição neste exemplo 
possui uma lista de contatos. Através da agenda será possível efetuar 
operações para adicionar, localizar, remover e listar os contatos. A 
unicidade dos contatos é controlada pelo seu telefone, ou seja, não 
deverão existir dois contatos com o mesmo telefone.

[Repositório do front](https://github.com/VerttB/agenda-frontend)



## Tabela
![Imagem das tabelas](docs/database-plan.png)

# Instruções

###  Criar Arquivo de secrets

Na pasta `src/main/resources`, crie o arquivo **`application-secrets.properties`** com as credenciais do banco:  
### Subir o container
 ```
sudo docker compose up
```
# Acessar o banco via adminer
1. Abra http://localhost:8090
2. Configure o login:
   * Sistema: PostgreSQL
   * Servidor: database
   * Dados definidos no `.env`
      * Usuário
      * Senha
      * Banco de dados