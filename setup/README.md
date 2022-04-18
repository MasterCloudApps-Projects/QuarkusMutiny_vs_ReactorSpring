# How to set up the environment for testing? 🛠

## Configuring the MYSQL database 🗄
First, you have to start a container with a MySQL image:
```sh
docker run --name mysql-tfm -d -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=imdb -p 3306:3306 mysql:8.0.26
```
Then, you should wait a few seconds (5s - 10s) for MySQL will finish booting. Now, you need to [download] (https://github.com/MasterCloudApps-Projects/QuarkusMutiny_vs_ReactorSpring/blob/main/setup/imdb_movies.sql) the script for the database, and execute the following command to populate the database:

```sh
docker exec -i mysql-tfm mysql -uroot -ppassword imdb < imdb_movies.sql
```

## Configuring Prometheus 📝
Download the [Prometheus configuration file](https://github.com/MasterCloudApps-Projects/QuarkusMutiny_vs_ReactorSpring/blob/main/lab/configuration/prometheus.yml), then you will have to indicate in the docker volume the path to where this configuration is located:

```sh
docker run --name prometheus-lab -d -p 9090:9090 -v <host-path>/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus
```

If you want to access Prometheus, click [here](http://localhost:9090)


## Viewing the data in Grafana 📊

To see the data collected by prometheus in grafana it's also necessary to start it with:

```sh
docker run -d --name grafana-lab -p 3000:3000 grafana/grafana-enterprise
```

Once you have the Grafana container running, you can access it [here](http://localhost:3000). Now, login using admin/admin (username/password) you need to configure Prometheus as data source, for that, I recommend you to follow these [instructions](https://prometheus.io/docs/visualization/grafana/). When you have it configured, import this [dashboard](https://grafana.com/grafana/dashboards/4701)

## Launch load testing 🔥

To do this you will need to install [Artillery](https://www.artillery.io/), download the [load file](https://github.com/MasterCloudApps-Projects/QuarkusMutiny_vs_ReactorSpring/blob/main/lab/configuration/load-testing.yml), and make sure you have one of the applications in the "lab" folder up. 

Now, you can run the load tests, note that by default it launches on port 8080, but depending on the application it will launch on a different port, so we recommend that you always launch the command with the desired address and port:

```sh
artillery run -t http://localhost:<app_port> load-testing.yml -o myFirstReport.json
```

When the test has finished you will be able to generate a report with:

```
artillery report myFirstReport.json
```