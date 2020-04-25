# Technical problem
* customers.txt - JSON file one line per customer
* Each customer has a lat, long location 
* Find customers within 100km of Dublin office (lat=53.339428, long=-6.257664)
* Output names and user ids of these customers (in ascending order by userId)
* Goal - find shortest distance between customer and Dublin and filter distances less than 100km
* Method - find the shortest distance between to (lat,long) points => minimum path on sphere(earth) - the 'great circle distance'
* Formula:-
```
gcd = 6,378.8 * arccos[sin(lat1) *  sin(lat2) + cos(lat1) * cos(lat2) * cos(lon2 - lon1)] kilometers

6,378.8 kilometers = Earth Radious
```

#### Thoughts
* Customer entity - maps to json
* Customer has a  (lat,long)
* Customer collection
* CustomerRepository - customer store
* Customer Service/Calculator - consider different formula?
* Avoid Service Anti-Pattern?
* Java8 streams to manipulate customer collection
* Venue class?

#### Actuals
###### TDD
* Spring Boot
* Json maps one-one to Customer mapping class
* Converter maps from JSON to Customer object class with encapsulated Location
* Customer collection
* CustomerRepository - customer store
* Customer Invitation/Calculator - consider different formula/no anti-pattern
* Java8 streams as needed

###### GreatCircleDistanceCalculatorTest calculation Testing
* Based on Earth geometry
* and from online site http://www.geosats.com/0x2circ.html

#### Installation
* JDK11
* Maven 3

#### Run
Check out Git project:
```
git clone git@github.com:mdecourci/customer-project.git
```
goto project directory
```
cd customer-project
```
Build Spring application
```
mvn clean install
```
Application requires setting external properties
* customer.datafile : file with json list of customers
* location.latitude : latitude of any venue, for example Dublin
* location.longitude : longitude of any venue, for example Dublin
* location.range : range of venue, for example Dublin
for Dublin
```
java -jar target/customer-project-1.0-SNAPSHOT.jar --customer.datafile=file:/tmp/customers.txt --location.range=100000 --location.latitude=53.339428 --location.longitude=-6.257664
```

