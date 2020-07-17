## Build Jar
mvn clean install

## Build Image
docker build -t elaa_eto_qrcode .

## Run Container
docker run -p 8080:8080 elaa_eto_qrcode

## healthcheck
curl http://localhost:8080/healthcheck