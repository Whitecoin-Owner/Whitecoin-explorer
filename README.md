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

Note that according to different time zones, modify the warehousing time variable UTC_WAREHOUSING_TIME_INTERVAL in com.browser.tools.Constant, change UTC to 0, and change the value of Dongba District to 8.
