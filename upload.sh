read -t 30 -p "请输入本地路径:" local
read -t 30 -p "请输入远程路径:" remote
scp -r ${local} flash@94.191.18.131:${remote}
