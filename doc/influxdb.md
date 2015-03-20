InfluxDB
========

To list all time series

```sql
list series
```

Select the max value for all LDP Sport requests
```sql
select value from gatling.ldpsport.allRequests.all.max;
```

Select max values after a specific date
```sql
select value from gatling.ldpsport.allRequests.all.max where time > '2015-03-18';
```

Max values between two dates
```sql
select value from gatling.ldpsport.allRequests.all.max where time > '2015-03-18' and time < '2015-03-20';
```

Retrieve all data where values are greater than 1000ms
```sql
select * from gatling.ldpsport.allRequests.all.max where value > 1000;
```

... etc
