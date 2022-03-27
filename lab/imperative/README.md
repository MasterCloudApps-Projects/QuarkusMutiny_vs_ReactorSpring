# Analysis Spring vs Quarkus Imperative
If you want to see all detatils about load test data and JVM metrics can be found [here]().

## HTTP Performance
These are the results obtained using Artillery tool to load the application.

<table>
<thead>
  <tr>
    <th></th>
    <th colspan="2">SPRING</th>
    <th colspan="2">QUARKUS</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td></td>
    <td>T1</td>
    <td>T2</td>
    <td>T1</td>
    <td>T2</td>
  </tr>
  <tr>
    <td>Request made</td>
    <td>60678 (100%)</td>
    <td>60608 (100%)</td>
    <td>60668 (100%)</td>
    <td>60706 (100%)</td>
  </tr>
  <tr>
    <td>Request completed</td>
    <td>5802 (9,56%)</td>
    <td>5803 (9,57%)</td>
    <td>52037 (85,77%)</td>
    <td>52409 (86,33%)</td>
  </tr>
  <tr>
    <td>Request failed</td>
    <td>54876 (90,44%)</td>
    <td>54805 (90,43%)</td>
    <td>8631 (14,23%)</td>
    <td>8297 (13,67%)</td>
  </tr>
  <tr>
    <td>Max req/seg</td>
    <td>77</td>
    <td>80</td>
    <td>328</td>
    <td>331</td>
  </tr>
</tbody>
</table>

If we look at the table and see the number of successful requests between the two frameworks there is a huge difference. Incredibly Spring doesn't reach 10% of successful requests, while Quarkus reaches 85%.

Regarding the amount of maximum supported requests between the two also huge, Spring<sup>(1)</sup> isn't able to reach 100 concurrent req/sec, however Quarkus reaches 330 concurrent req/sec. This is 4 times higher than Spring.

<table>
<thead>
  <tr>
    <th></th>
    <th colspan="2">SPRING</th>
    <th colspan="2">QUARKUS</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td></td>
    <td>T1</td>
    <td>T2</td>
    <td>T1</td>
    <td>T2</td>
  </tr>
  <tr>
    <td>Median Response Time (ms)</td>
    <td>24,8</td>
    <td>23,8</td>
    <td>7</td>
    <td>7,9</td>
  </tr>
  <tr>
    <td>p95 Response Time (ms)</td>
    <td>327,1</td>
    <td>308</td>
    <td>104,7</td>
    <td>125,2</td>
  </tr>
  <tr>
    <td>p99 Response Time (ms)</td>
    <td>497,8</td>
    <td>459,5</td>
    <td>232,8</td>
    <td>273,2</td>
  </tr>
</tbody>
</table>

|        	| Number of times better response time of Quarkus over Spring 	|
|--------	|:------------------------------------------------------------	|
| Median 	|                                                         3,28 	|
| p95    	|                                                         2,79 	|
| p99    	|                                                         1,91 	|

On the time of requests completed by both, Quarkus gives better response times, being 2.5 times faster if we set p95 as a more realistic measure.

(1) To be more specific, the problem lies in Hikari's connection management.

## JVM metrics
At this point, we can see that the total percentage of heap/non-heap usage is quite similar, being slightly better on the Quarkus side. If we compare the CPU usage they are almost identical, but considering that the Spring application broke at the beginning of the test and wasn't able to report its metrics, we can't take that measurement for good, we believe it would be higher as that 15.5% only reflects when the application was stable without much load.

About the amount of MB used by the JVM heap/non-heap and JVM total, we can deduce that Quarkus consumes 78% compared than Spring. And if we look at the metaspace memory it goes in the same direction, with Spring consuming more resources.

We can also observe that there is also a difference in the number of classes between both, where Quarkus loads 73.5% of the classes needed by Spring.

Finally, the number of live threads and although both reach 213 threads, the same behaviour is observed in Spring, increasing rapidly the number of threads shortly after starting the test, in comparison, Quarkus remains more stable until it reaches a point where the application can no longer run.

# Conclusion
For some reason, with the default configuration of the Spring application it has problems with database connection management (at least on this machine), causing it to break much earlier than expected, and therefore the concurrent request data isn't very reliable.

Still, if we only look at response times and JVM resources, Quarkus performs better than Spring, which speaks very well for Quarkus, as it isn't such a mature framework. 

In addition, these tests have been tested with the Quarkus's Fat Jar, if the tests had been done with a Quarkus native image, the data would possibly be better.
