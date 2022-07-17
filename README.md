# monitor
## Project name: Monitor  

## Project Description: 
The maintainers want to collect all the messages send by industry machines(such as measurement machine, Microscope etc.) imedicatly. So we use one websocket server to commucate with the machines to receive and store the upload messages(called push websocket), and we use another websocket for maintainers to read the imedicate messages(called read websocket). Of course, the maintainers will receive history message when he/she login. This is the demo for the scenario above.It helps the maintainer to ingest and store the data, finally exposing it to the frontend dev team via a JSON - based API.

## Code running envirment: 
JDK1.7 + Spring MVC 4.3.18 

## API Description：

### URL:  ws://localhost:8080/monitor/ws/read/{machineId}/{requestUserId}    
 
 function: the read websocket to expose it to the frontend dev team via a JSON  
 
 machineId: the uplaod message machineId (Unique Identiy), if the maintainer want to read one machine use specified machine Id， if he/she want to read all machines, use "all" instead of "machineId" position.  
 
 requestUserId: the maintainer user id

### URL:  ws://localhost:8080/monitor/ws/push/{machineId}  
 
 function: the push websocket to collect message send by machine.  
 
 machineId: the uplaod message machineId (Unique Identiy), if the maintainer want to read one machine use specified machine Id， if he/she want to read all machines, use "all" instead of "machineId" position.  
 
 {machineId}: the uplaod message machineId (Unique Identiy)


## Sequence Diagram:  
