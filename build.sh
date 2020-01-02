read -t 30 -p "请输入生成的镜像版本号:" version
echo -e "\033[34m 您输入的版本号为:${version},请稍后... \033[0m"

echo -e "\033[33m 1.开始执行build构建: \033[0m"
gradle clean build -x test

echo -e "\033[33m 2.打包docker镜像: \033[0m"
cd api
gradle dockerBuilder

echo -e "\033[33m 3.镜像重命名,修改tag: \033[0m"
docker tag com.byy.service/api:1.0 47.96.110.23:8081/flash-car/app:${version}
docker rmi com.byy.service/api:1.0

docker login 47.96.110.23:8081 -u yyci -p '/o5)]:rh|O/T'
docker push 47.96.110.23:8081/flash-car/app:${version}
docker logout
