# Spring and gRPC

This project is built using the [Spring Boot Framework](https://spring.io/).

In this case, **Spring** is combined with **gRPC**, which is a modern open source high performance Remote Procedure
Call (RPC) framework that can run in any environment.

Learn more about gRPC in [gRPC.io](https://grpc.io/)

## Set Up ⚙

You must have installed on your machine:

* JDK 11 version
* Apache Maven 3.8.1
* [GRPCurl](https://github.com/fullstorydev/grpcurl/blob/master/README.md)

## Start Up 🛠

You can run the application as follows:

```bash
./mvnw spring-boot:run
```

## Testing 🔍

The application is tested by making a call directly to the GRPC server that we have set up, GRPCurl allows us to do this
in a simple way by launching the following command:

    grpcurl -import-path src/main/proto/ -proto generator.proto -plaintext -d '{"number": 2}' localhost:6565 generator.RandomNumberGenerator/GenerateNumber

This command tells **GRPCurl** to make a call to our gRPC service called **RandomNumberGenerator** indicating where is
the **path** and **file** **name** of our proto file.