xwc-explorer
===================

# dependencies

* chain node
* redis
* mysql
* docker
* jdk 8
* nodejs 12

# deploy

* `deploy.md`

# description
The jdk version uses 1.8 version, not too high.

If you set the corresponding time zone, please set the serverTimezone of the database connection information in application-*.properties to the corresponding time zone, and specify -Duser.timezone as the corresponding time zone when the jvm is started。
