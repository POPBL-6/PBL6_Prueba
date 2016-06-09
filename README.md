# Middleware simple demo application

[![Build Status](https://travis-ci.org/POPBL-6/PBL6_Prueba.svg?branch=master)](https://travis-ci.org/POPBL-6/PBL6_Prueba)

Application for basic testing the middleware API and the broker

##How it works
Run this application uses <a href=https://github.com/POPBL-6/middleware>Middleware</a> together with the 
<a href=https://github.com/POPBL-6/broker>Broker</a> in order to test their functionality.

It consists in a single class that spawns a "console" from whitch you can test the functionality of the
Middleware.

First of all, a reachable <a href=https://github.com/POPBL-6/broker>Broker</a> needs to be run, in order to
be able to connect with other instances. Incidentaly, the configuration and keystore that come with that 
project is compatible with the default configuration of this application:

    PSPortSSL -p 5434 -k client1.jks -t client1.jks -kp snowflake
    
For other configurations, just let the initial connection attempt fail, type the new configuration in the textBox
below, click Send, and it should connect. Or edit the source.

Then, you can type commands in the textBox and click Send or press return to execute them:

`subscribe <topic>` or `s <topic>`

`unsubscribe <topic>` or `u <topic>`

`publish <topic> <message>` or `p <topic> <message>`

`getLastSample <topic>` or `g <topic>`

`addListener` or `a`

`removeListener` or `r`

Clear screen: `clear` or `c`

Terminate application: `exit` or `e`

## Example execution
* Run the application (instance 1), should display `Connected with PSPortSSL` if connection is succesful.
* Run the application (instance 2), should display `Connected with PSPortSSL` if connection is succesful.
* (Optional)`PSPortSSL -p 5434 -k client2.jks -t client2.jks -kp snowflake` on instance 2 to use a different certificate (this disconnects the previous connection)
* `subscribe message` on instance 1
* `publish message hello` on instance 2
* Instance 1 should display: `Received> Timestamp: Thu Jun 09 10:03:22 CEST 2016 || Sender: CN=Client2 || Topic: message || Data: hello`

## How to run it?
### If you have Gradle installed
Just run the following command in the repository folder
```sh
$ gradle run
```
The broker will start listening in port 5434 by default

### If you don't have gradle installed
We have included a gradle wrapper also, just execute the next command to run it
```sh
$ ./gradlew run
```
 
