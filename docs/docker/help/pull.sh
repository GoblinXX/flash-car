echo -e "\033[33m 拉取镜像... \033[0m"
read -t 30 -p "请输入镜像名称:" app
read -t 30 -p "请输入镜像版本号:" version
docker pull 47.96.110.23:8081/${app}/app:${version}
