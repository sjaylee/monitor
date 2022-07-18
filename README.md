# monitor

### Project Introduce: 
The maintainers want to collect all the messages send by industry machines(such as measurement machine, Microscope etc.) imedicatly. So we use one websocket server to commucate with the machines to receive and store the upload messages(called push websocket), and we use another websocket for maintainers to read the imedicate messages(called read websocket). Of course, the maintainers will receive history message when he/she login. This is the demo for the scenario above.It helps the maintainer to ingest and store the data, finally exposing it to the frontend dev team via a JSON - based API.

### Envirment:   

The development of this project is based on the Java language, and the specific environment includes:
```
Java: Java 7+
Maven: 3
Spring: Spring MVC 4.3.10+
```
This is a demo project, so I use of local file instead of formal DBMS to store and retrive infos.

### API Description：

#### URL:  ws://localhost:8080/monitor/ws/read/{machineId}/{requestUserId}    
 
 function: the read websocket to expose it to the frontend dev team via a JSON  
 
 machineId: the uplaod message machineId (Unique Identiy), if the maintainer want to read one machine use specified machine Id， if he/she want to read all machines, use "all" instead of "machineId" position.  
 
 requestUserId: the maintainer user id

#### URL:  ws://localhost:8080/monitor/ws/push/{machineId}  
 
 function: the push websocket to collect message send by machine.  
 
 machineId: the uplaod message machineId (Unique Identiy), if the maintainer want to read one machine use specified machine Id， if he/she want to read all machines, use "all" instead of "machineId" position.  
 
 {machineId}: the uplaod message machineId (Unique Identiy)


## Sequence Diagram:  

![Aaron Swartz](https://raw.githubusercontent.com/sjaylee/myImages/main/%E7%AE%80%E5%8D%95%E6%97%B6%E5%BA%8F%E5%88%86%E6%9E%90.png)

### Demo page snapshot：
The image below demonstrate the machine send message and the maintainer read the message in soft real time.
![Aaron Swartz](https://raw.githubusercontent.com/sjaylee/myImages/main/demo.PNG)
