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
* Customer has a location
* Customer collection
* CustomerRepository - customer store
* Customer Service/Calculator - consider different formula?
* Java8 streams to manipulate customer collection

