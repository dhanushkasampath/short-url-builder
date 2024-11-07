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


ToDo 1 : change the mongodb to mysql - done

ToDo 2 : done
If we hardcode the ip and port in the database it will cause an issue if we deploy the service in another instance.
So better way is to keep only the unique id in the table

ToDo 3 : done
When searching only records with is_deleted = false, should be queried.
To achieve that I used below method in repository

ShortUrlData findByShortUrlAndIsDeletedFalse(String shortUrl);

ToDO 4 : swagger Integrated - done

ToDo 5 :
Implement user management. Only Authorized users should able to update and delete

ToDo 6 : done
set up link expiration time
- add to property file(1 month).  ## done
- if expired not allowed to re-direct(add validation before redirect-added to hql)   ##done
- If an update to the link happens, then expiry time will move 1 month forward  ##done

ToDo 7 :
Implement rate limiter
- for create
- for update
- for delete

ToDO 8 :
implement cronjob to remove expired and deleted records

ToDo 9 :
Implement caching for get request
-when delete happens, it should delete from cache as well

ToDo 10 : Add logs - done