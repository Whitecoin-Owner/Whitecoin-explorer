deployment
==============

# dependencies

* JDK8 or JDK8+
* maven3
* mysql5.7
* redis
* nginx
* nodejs 12
* xwc_node and xwc_cli rpc service

# backend

* create database in mysql with utf8-mb4 encoding
* import `sql/whitecoin_explorer.sql` to the mysql database
* make sure xwc_node and xwc_cli is started with rpc services
* `mvn clean package -Dmaven.test.skip=true` to package the backend code to `target/whitecoin-browser-0.0.1.jar` and copy the jar to deploy directory
* copy `src/main/resources/application-prod.properties` to deploy directory and rename file to `application.properties`
* modify deploy directory `application.properties` configuration
* `java -jar whitecoin-browser-0.0.1.jar` to start the backend

# frontend

* frontend code is in directory `front`
* `npm install` to install dependencies
* modify `front/src/axios/request.js` and change `axios.defaults.baseURL` to production backend url
* `npm run build` to compile frontend codes to `dist` directory
* deploy the `dist` directory to server's nginx html root

# nginx proxy

* use nginx to proxy frontend and backend urls
