# embassy1

A simple Camel Demo

## Prerequisites:

<pre><code>
JDK 8

Maven 3.2+

Apache ActiveMQ 5.12.1 

Derby DB
</code></pre>

## How to kick off the demo

### Start your activemq broker:

<pre><code>
 
 # Linux
 apache-activemq-5.12.1/bin> ./activemq start 

 # Windows
 apache-activemq-5.12.1\bin$ activemq

</code></pre>

### Start Camel Routes

 Open a new terminal / command prompt.

 Source your environment (Java Home, Maven Home)

 To build the project routes type:

<pre><code>

  embassy> mvn clean install

</code></pre>

 You'll need to start each route in its own terminal for this demo.

<pre><code>

  embassy> mvn camel:run

</code></pre>

### Send in request to request queue. 

Open a web browser the ActiveMQ console
http://localhost:8161/admin/queues.jsp

Create a message for the request queue.

In the body section, copy and paste the below json body:

<pre><code>

{
  "businessEvent": "t00.sbx.BnrPerson",
  "messageId": "61132227",
  "timeStamp": "2015-11-02T18:29:28.747Z",
  "sourceOfChange": "foobar",
  "key": "30180",
  "entity": "SPRIDEN",
  "changeType": "INSERT",
  "changeData": [
    {
      "name": "SPRIDEN_ACTIVITY_DATE",
      "new": "2015/11/02 18:29:18"
    },
    {
      "name": "SPRIDEN_CHANGE_IND",
      "new": null
    },
    {
      "name": "SPRIDEN_CREATE_DATE",
      "new": "2015/11/02 18:29:18"
    },
    {
      "name": "SPRIDEN_CREATE_FDMN_CODE",
      "new": null
    },
  ]
}

</code></pre>

## Setup Debug Mode for Camel Unit Tests

 To configure debug mode of your Camel Unit Tests, setup your IDE's Debug runner to include the below VM options:

<code><pre>

 -ea -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005

</code></pre>

## Setup Tomcat 7 in Debug Mode

 To configure Tomcat 7 to run in debug mode, set the following:

<code><pre>

 For windows first set variables:

 set JPDA_ADDRESS=8000
 set JPDA_TRANSPORT=dt_socket

 to start server in debug mode:

 %TOMCAT_HOME%/bin/catalina.bat jpda start

 For unix first export variables:

 export JPDA_ADDRESS=8000
 export JPDA_TRANSPORT=dt_socket

 and to start server in debug mode:

 %TOMCAT_HOME%/bin/catalina.sh jpda start

</code></pre>

Note: You'll need to configure your editor to connect to port 8000

