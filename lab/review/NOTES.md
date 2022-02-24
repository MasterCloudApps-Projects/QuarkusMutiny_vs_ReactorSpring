docker run --name mongodb-lab -p 27017:27017 -v /Users/diego/Projects/TFM/quarkus/lab/mongodb-volume:/data/db -d mongo-d mongo:5.0.5-focal
docker exec mongodb-lab sh -c 'mongoimport --db test --collection movies --type json --file /data/db/movies.json'


------ prometheus.yml ------
global:
  scrape_interval: 15s # By default, scrape targets every 15 seconds.

# A scrape configuration containing exactly one endpoint to scrape:
# Here it's Prometheus itself.
scrape_configs:

  - job_name: 'prometheus'
    scrape_interval: 5s
    static_configs:
      - targets: ['localhost:9090']

  - job_name: 'quarkus'
    scrape_interval: 5s
    metrics_path: /q/metrics
    static_configs:
      - targets: ['docker.for.mac.localhost:8080']
------------------
docker run --name prometheus-lab -d  -p 9090:9090 -v ~/Projects/QuarkusMutiny_vs_ReactorSpring/lab/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus

http://localhost:9090
-----------------------------------