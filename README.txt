docker pull dhanushkadocker/short-url-builder
docker images
docker run -p 8080:8080 dhanushkadocker/short-url-builder

http://localhost:8080/api/v1/generate

Swagger endpoint: http://localhost:8081/swagger-ui/index.html

=========================================================================
use below command to check the status of mongod
systemctl status mongod

ps aux | grep mongod
This will show any running mongod processes.


=========================================================================


ToDo 1 : change the mongodb to mydql - done

If we hardcode the ip and port in the database it will cause an issue if we deploy the service in another instance.
So better way is to keep only the unique id in the table
