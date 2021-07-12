## Configuration
The behavior of the application can be configured in different ways. One way is to provide a *application.properties* configuration file.
On application.properties you can configure the DB and API endpoint.
For example:

```
#ArangoDB
spring.data.arangodb.hosts=localhost:8529
spring.data.arangodb.database=events
spring.data.arangodb.user=root
spring.data.arangodb.password=openSesame

#configure the application not to use ArangoDB uncomment the following line
#spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.ArangoAutoConfiguration 

#API events
API=https://virtserver.swaggerhub.com/EW-Shopp/EW-Shopp_Event_API/2.2.0/event/

#number of days to retrieve
num_days=5
starting_date=2018-01-01
ending_date=2018-02-01
working_path=work
results_dir=events
outputOnFile=enabled   #to enable the application to generate an output file
fileName = output.json #output file name
```

## Build using Apache Maven
```
$ mvn package
```

## Build with Docker 
Once the application is packaged a docker container can be created automatically using maven with following command

```
$ mvn docker:build
```
and pushed

```
$ mvn docker:push
```
The user and container name can be configured in the *pom.xml* file.


## Run
```
$ java -jar target/EventsEWSArango-0.1.jar --spring.config.location=file:///${properties_file}
``` 


## Run with Docker Compose
A courtesy docker-compose.yml file is provided in src/main/docker. The application can be 
executed with the following command: 

```
$ docker-compose up -d 
```
Docker compose will take care of downloading the container from the Docker Hub and executing it against a mock custom event server.  


## API response reference table

The following table reports example of Queries and API response for EW-Shopp Business Cases. 

| Business Case 	| Intuitive Query                                                                                                                                                                                                                                                	| Query Data                                                                    	| API Response                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          	|
| Business Case | Input Data | Intuitive Query | API Response |
|-|-|-|-|
| Brand  Performance Insights (BC1) | { "IdUser": "user1",  "TimeStamp": "2021-01-01T07:50:41.252Z",  "Query": "canon makro" } | Given a table containing data about users’ queries for products, retrieve all events (from products price history) that describe the change in price in a selected temporal span (days) - API request -> /events/2017-01-01?query=event.measure.priceChange>10 | {  "@context":{ --- hide ---},  "eventArray":[  {  "@type":"beeo:Event",  "identifier":"event1",  "name":"happy new year",  "startDate":"2021-01-01T00:00:00.000Z",  "category":"Foto",  "product":{  "@type":"beeo:Product",  "identifier":"prod1",  "gtin13":"123456789012",  "description":"Canon objektiv EF 100 F/2,8 Makro USM”,  "seller": {  “@type”: “beeo:Seller”,  “identifier”: “seller1”,  “name”: “seller # 1”  },  "sku":"018719ACME/WMD001080165",  "catalogId":"xx192s",  "category": {  “@type”: “beeo:Category”,  “identifier”: "Objektivi"  }  },  "measure":{  "@type":"beeo:Measure",  "priceChanged":true,  “discount”: true,  “priceChange”: 15  "price": 123.45  }  },  … other events from other sellers ...  ] }	|
| Weather and Event-aware Business Intelligence for the Optimization of Campaigns and Human Resources (BC2) | { IdUser: userX,  TimeStamp: 2021-01-01T16:33:12.756Z,  StoreType: online, channel: xcodeA32_3 } |     Given a table containing data about user visits in online and physical stores in a selected temporal span (for instance 10 days), retrieve all recorded marketing events for a certain channel that took place in that period - API request: /events/2021-01-01+10?query=event.channelCode=xcodeA32_3 | {  "@context":{ --- hide ---},  "eventArray":[  {  "@type":"beeo:Event",  "identifier":"event57",  "name":"winter promo",  "startDate":"2021-01-06T00:00:00.000Z",  "category": {"@type":"beeo:Category", "identifier": "fashion_winter", "description":"Winter collection"},  "channelCode":"xcodeA32_3" },  … other events from the same channel ...  ] } |
|Workforce  &  Campaign  Management  Optimization (BC3)| N.A. (dataset contruction) | To build a dataset with user interaction data, retrieve all information about new product launch events that occurred in the 30-day time interval - API request: /events/2021-01-01+29?query=event.category.description=product\%20launch| {  "@context":{ --- hide ---},  "eventArray":[  {  "@type":"beeo:Event",  "identifier":"event42",  "name":"iPhone XX (the best iPhone ever)",  "startDate":"2021-01-21T21:21:21.211Z",  "category": {"@type":"beeo:Category", "identifier": "plaunch", "description":"product launch"} },  … other events of the same category ...  ] } |
|Event and Weather-aware Foot Traffic Predictions and Analytics (BC4) | { "SellerId":"seller5", "location": "Milan", "zipCode":"20131", "storeType":"showroom", "# visitors": "7777", "Timestamp":"2021-05-05T20:00:00.000Z"}| Given a table with information about user visiting a showroom in Milan (Italy) with postal code 20131, retrieve all information regarding the interested audience for events occurred over a 6-day time span - API request: /events/2021-05-05+5?query=event.location.addressCountry=IT\&event.location.postalCode=20131 | {  "@context":{ --- hide ---},  "eventArray":[  {  "@type":"beeo:Event",  "identifier":"event990",  "name":"Car Expo Milan",  "startDate":"2021-05-07T08:00:00.000Z",  "category":"car expo", "location": {"@type":"schema:PostalAddress", "addressCountry": "IT", "postalCode": "20131"} },  … other events in the same location ...  ] } |



