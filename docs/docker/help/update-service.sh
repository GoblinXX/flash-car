echo -e "\033[33m 更新服务... \033[0m"
read -t 30 -p "请输入容器更新的基础镜像名:" image
read -t 30 -p "请输入容器更新的镜像版本号:" version
read -t 30 -p "请输入需要更新的service:" service
docker service update --image 47.96.110.23:8081/${image}/app:${version} ${service}