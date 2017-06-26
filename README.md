# Simple rpc client-server.

### Introduction

This is a simple rpc client-server solution.

#### Building and Running Application

Executable jars assembled by ```mvn package``` and could be found in a target directories of ```server``` and ```client``` modules:

```./server/target/server-1.0-SNAPSHOT-jar-with-dependencies.jar```

```./client/target/client-1.0-SNAPSHOT-jar-with-dependencies.jar```

It could be run by:

```java -jar /path/to/app/server-1.0-SNAPSHOT-jar-with-dependencies.jar```

```java -jar /path/to/app/client-1.0-SNAPSHOT-jar-with-dependencies.jar```

This way server will start on default port 2323 and client will try to connect to localhost:2323 respectively.

There is possible to pass specific port to server and hostname:port to client as arguments:

```java -jar /path/to/app/server-1.0-SNAPSHOT-jar-with-dependencies.jar 3434```

```java -jar /path/to/app/client-1.0-SNAPSHOT-jar-with-dependencies.jar localhost 3434```
