# Android application for rooms management

### Description
This is a mobile interface for the management of a set of rooms and buildings.

### Features
* This application interacts with a [REST API](https://github.com/MajeureInfo/webProject) provided by a server hosted on [Heroku](https://www.heroku.com/).
* User requests made from this application update the database on the Heroku server and also send MQTT messages to a [CloudMQTT](https://www.cloudmqtt.com/) broker.
This enables real-time updates of other user interfaces thanks to the asynchrony of MQTT. These messages could also be received by the objects themselves, however this is not implemented yet.
* This application is itself updated in real-time whis MQTT messages sent by other interfaces such as the [website](https://github.com/MajeureInfo/webProjectFront) or the [Arduino object](https://github.com/MajeureInfo/Arduino).
