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

ToDo 9 : done
- 9.1 Implement caching for get request - done

We can verify this as follows.
When caching enabled the second request does not come to the service layer. no any log at service layer level get appeared in console

- 9.2 when update happens, it should update the cache as well - done

1. first I implemented caching for query endpoint
2. Requested that resource for the first time. Then it got the data from db and served me
3. Then I requested for 2nd time. Then it did not get data from db. but from cache and served me
4. Then I changed the long url using modify endpoint and again requested. Unfortunately I got the previous data.
That means, since I have updated the db, the cache has not been updated.

note : cache is working with the help of return type and return value
in both cache generating and updating. So make sure the value we return when generating and updating the
cache is same and in updating cache, it's the new value we updated

- 9.3 when delete happens, it should delete from cache as well - In-Progress

@CacheEvict
it's not mandatory to that the return type of this method should not match the @Cacheable or @CachePut

- 9.4 Implement cache timeout - done

With spring-boot-starter-cache, configuring a cache timeout (TTL) depends on the specific cache provider that you
configure alongside the starter. spring-boot-starter-cache provides the abstraction, but you’ll need to pick a
concrete caching provider to control features like timeouts. Here’s how to set it up with commonly used providers:

9.4.1) Add Caffeine to your project:

<dependency>
    <groupId>com.github.ben-manes.caffeine</groupId>
    <artifactId>caffeine</artifactId>
</dependency>

9.4.2) Configure Caffeine Cache Timeout in application.yml:

spring:
  cache:
    type: caffeine
    caffeine:
      spec: maximumSize=1000,expireAfterWrite=5m

This worked fine. refer the image in READMORE dir

http://localhost:8081/actuator/caches  <- use this endpoint to check that caching has set up properly

===========ISSUE I FACED===========This still remains==============
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