# Spring Boot Camel Integration example
Example using spring boot with camel and dynamically adding routes to different REST APIs.

### Adding sample vin lookup API proxy
```
curl -v -XPOST -H 'content-type: application/json' 'http://localhost:8080/route/' -d '
{
  "routeId": "vin-lookup",
  "url": "http://www.mocky.io/v2/5b0790c12f0000b118c620ff",
  "outMap": {
    "make": "car_make",
    "model": "car_model",
    "year": "car_year"
  }
}
'
```

## Invoking sample vin lookup proxy
```
curl -v -H 'content-type: application/json' -XPOST 'http://localhost:8080/vin-lookup' -d '                                                                           [13:49:01]
{
  "vin": "1234"
}
'
```


### Adding sample fein lookup API proxy
```
curl -v -XPOST -H 'content-type: application/json' 'http://localhost:8080/route/' -d '
{
  "routeId": "fein-lookup",
  "url": "http://www.mocky.io/v2/5b085a743200008a0070027a",
  "outMap": {
    "company_name": "business_name",
    "address1": "business_address1",
    "address2": "business_address2",
    "city": "business_city",
    "state": "business_state",
    "zip": "business_zip"
  }
}
'
```

### Invoking the sample fein lookup proxy
```
curl -v -H 'content-type: application/json' -XPOST 'http://localhost:8080/fein-lookup' -d '                                                                           [13:49:01]
{
  "fein": "1234"
}
'
```