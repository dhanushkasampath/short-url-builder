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

ToDo 7 : done
Implement rate limiter
- for query
- for create
- for update
- for delete

ToDO 8 :
implement cronjob to remove expired and deleted records

ToDo 9 : In-Progress
Implement caching for get request - done
-when update happens, it should update the cache as well
-when delete happens, it should delete from cache as well

http://localhost:8081/actuator/caches  <- use this endpoint to check that caching has set up properly

===========ISSUE I FACED=========================
i implemented spring-boot-starter-cache in my project to a particular endpoint.
Also I have implemented rate limiter by giving below properties to that endpoint.


resilience4j:
  ratelimiter:
    instances:
      myServiceRateLimiter:
        limit-for-period: 5
        limit-refresh-period: 20s
        timeout-duration: 500ms

Then When I call the below actuator endpoint to see whether the caching is working,
I can see in the logs, Automatically 5 requests are getting hitted to the above endpoint.
Why is that?


http://localhost:8081/actuator/caches

=========================================

ToDo 10 : Add logs - done