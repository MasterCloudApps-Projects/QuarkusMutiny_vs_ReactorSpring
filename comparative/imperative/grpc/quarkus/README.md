# Quarkus and gRPC

This project is built using the [Quarkus Framework](https://quarkus.io/).

In this case, **Quarkus** is combined with **gRPC**, which is a modern open source high performance Remote Procedure
Call (RPC) framework that can run in any environment.

Learn more about gRPC in [gRPC.io](https://grpc.io/)

## Set Up ⚙

You must have installed on your machine:

* JDK 11 version
* Apache Maven 3.8.1
* [GRPCurl](https://github.com/fullstorydev/grpcurl/blob/master/README.md)

## Start Up 🛠

There are two option:

1. Development

   You can run your application in dev mode:

    ```shell script
    ./mvnw clean compile quarkus:dev
    ```

2. Production

   First, the application must be packaged using:

    ```bash
    ./mvnw package
    ```

   It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory. Now, the only thing left to do is to
   run the application:

    ```bash
    java -jar target/quarkus-app/quarkus-run.jar
    ```

## Testing 🔍

The application is tested by making a call directly to the GRPC server that we have set up, GRPCurl allows us to do this
in a simple way by launching the following command:

    grpcurl -import-path src/main/proto/ -proto generator.proto -plaintext -d '{"number": 2}' localhost:6565 generator.RandomNumberGenerator/GenerateNumber

This command tells **GRPCurl** to make a call to our gRPC service called **RandomNumberGenerator** indicating where is
the **path** and **file** **name** of our proto file.