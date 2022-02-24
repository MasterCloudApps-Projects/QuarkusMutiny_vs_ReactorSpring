
```sh
docker run --name prometheus-lab -d  -p 9090:9090 -v <host-path>/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus

http://localhost:9090
```

```sh
docker run -d --name grafana-lab -p 3000:3000 grafana/grafana-enterprise

http://localhost:3000
```

Dashboard: https://grafana.com/grafana/dashboards/4701