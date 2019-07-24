
# Spring cloud sleuth example

This example demonstrates the following features
- Programatically create spans
- set sampling rate
- Zipkin via HTTP
- RestTemplate integration
- Creating a custom filter that adds tags to the current span 

Start up all modules using 

```shell
   mvn spring-boot:run
```   
then hit

[http://localhost:8080/1007?port=8090](http://localhost:8080/1007?port=8090)

to call service a so it will call service b

then open 

[http://localhost:9090/](http://localhost:9090/)

and look for the trace of the call
