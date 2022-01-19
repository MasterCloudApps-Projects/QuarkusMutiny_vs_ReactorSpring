# Imperative-grpc-quarkus

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
mvn compile quarkus:dev
```

This will generate all the necessary files and classes that GRPC needs.

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## GRPCurl

GRPCurl is a command-line tool that lets you interact with gRPC servers. It's basically curl for gRPC servers.

To test and query the grpc examples within this project it is necessary to install GRPCurl

If you want to know more about GRPCurl and learn how to install it, please visit the official
documentation: https://github.com/fullstorydev/grpcurl/blob/master/README.md
