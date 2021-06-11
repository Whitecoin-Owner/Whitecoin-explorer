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
* copy `src/main/resources/application-prod.demo.properties` to deploy directory and rename file to `application.properties`,For content, refer to the application-prod.demo.properties file under the resource directory
* modify deploy directory `application.properties` configuration
* ` nohup java -Duser.timezone=Europe/Athens -jar whitecoin-browser-0.0.1.jar --spring.config.location=/root/projects/whitecoin-browser/application.properties >/dev/null 2>&1 & ` to start the backend,About the /root/projects/whitecoin-browser/ directory is the storage directory of our online jar package.

# frontend

* frontend code is in directory `front`
* `npm install` to install dependencies
* modify `front/src/axios/request.js` and change `axios.defaults.baseURL` to production backend url
* `npm run build` to compile frontend codes to `dist` directory
* deploy the `dist` directory to server's nginx html root

# nginx proxy

* use nginx to proxy frontend and backend urls

# xwc_node & xwc_cli

* get the latest xwc_node & xwc_cli from here: https://github.com/Whitecoin-XWC/Whitecoin-core/releases/download/1.3.0/xwc_linux_1.3.0.tar.gz

* start `xwc_node` use following command line:

  ```shell
  $> SCREEN -dmS xwc_explorer_node ./xwc_node --rpc-endpoint=127.0.0.1:50806 --data-dir=/data/xwc_data --p2p-endpoint=0.0.0.0:23456
  ```

* start `xwc_cli` use following command line:

  ```shell
  $> SCREEN -dmS xwc_explorer_cli ./xwc_cli --server-rpc-endpoint=ws://127.0.0.1:50806 --rpc-endpoint=0.0.0.0:10044
  ```

* create a new wallet and create a new account `caller0` & `wallfacer0`

  ```shell
  new >>> set_password yourpassword
  set_password yourpassword
  368360ms th_a       wallet.cpp:1341               save_wallet_file     ] saving wallet to file wallet.json
  368371ms th_a       wallet.cpp:1341               save_wallet_file     ] saving wallet to file wallet.json
  null
  locked >>> unlock yourpassword
  unlock yourpassword
  null
  unlocked >>> wallet_create_account caller0
  wallet_create_account caller0
  425506ms th_a       wallet.cpp:1341               save_wallet_file     ] saving wallet to file wallet.json
  "XWCNT2MeQxbnthxz53CmiEtwJuBXJkfS7exeC"
  unlocked >>> wallet_create_account wallfacer0
  wallet_create_account wallfacer0
  439042ms th_a       wallet.cpp:1341               save_wallet_file     ] saving wallet to file wallet.json
  "XWCNdpLtWuw4n5BDF1Ren2quTMv3JqkgtiowE"
  unlocked >>>
  ```

  