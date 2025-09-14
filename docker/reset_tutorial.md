# reset tutorial

```
docker compose down
docker volume rm agenda-backend_postgres_data
docker compose up -d --build
```

ou

```
docker compose down -v database
```

para caso queira ver mais  informações:
```
docker ps  #contâineres ativos
docker volume ls  #volumes em uso
docker logs agenda-backend-database-1  #logs do contâiner do banco
```