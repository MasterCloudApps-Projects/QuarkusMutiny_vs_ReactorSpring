This section has been created to focus on the performance of both frameworks and observe how they manage the JVM. We have chosen to start from the rest-db project, because we believe it's the most representative example of the day to day.

The rest-db project isn't 100% the same, we have added the micrometer maven dependencies in order to obtain the JVM metrics, and we have also removed the dependencies such as swagger because we want it to be as clean as possible.

## What does the test do? 
Basically we have chosen to perform load tests with the [Artillery.io](https://www.artillery.io/) tool, the http configuration consists of a pool of 10 threads and a timeout of 2 seconds, we believe that this timeout is enough for an application in a productive environment and that provides an API rest. 

It consists of 11 phases, where the first phase starts at 5 constant req/sec and the last phase ends at 400 constant req/sec, it also consists of 4 scenarios with different weights. For more details the definition is [here](https://github.com/MasterCloudApps-Projects/QuarkusMutiny_vs_ReactorSpring/blob/main/lab/configuration/load-testing.yml).

## How did we do it?
Before starting, we had to configure Prometheus, where it collects all the data provided by micrometer from each application. On the other hand, we have also set up a Grafana, where all the data that is dumped in Prometheus is represented. [Here](https://github.com/MasterCloudApps-Projects/QuarkusMutiny_vs_ReactorSpring/tree/main/lab/configuration) you can see how to configure both tools.

Each application has been launched individually to avoid performance problems between them. 

The procedure has been to start the application, leave it quiet for 1 minute, launch the load tests, and at the end of the tests, leave it for another minute to "cold" and stop the application. This process has been done twice, leaving another minute between tests. 

## Where have they been tested?
To provide a better comparison, these tests were run on two different machines:

```
MacBook Pro 13" (2017) running macOS Monterey OS.
Processor: 3.1 GHz dual-core Intel Core i5
Memory 16GB 2133 MHz LPDDR3
```

```
MacBook Pro 13" (2017) running macOS Monterey OS.
Processor: 2.3 GHz dual-core Intel Core i5
Memory 8GB 2133 MHz LPDDR3
```

## What data are we going to look at?
Although we have more metrics collected by micrometer we are going to focus on: request response times, nnumber of concurrent requests, CPU usage, JVM heap/non-heap, JVM metaspace, number of threads and classes loaded.