**部署项目前命令:**

1、初始化节点
```bash
docker swarm init
```
2、创建网络
```bash
docker network create -d overlay --attachable my-overlay
```
3、初始化任务
```bash
docker stack deploy -c docker-compose.yml web
```

