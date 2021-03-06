# Project_2.2
DKE project 2.2 - 2018

Phase 1 Description:
In the first phase, literature research is performed on modeling and simulation approaches. Attempt to include enough dynamics in your model such that congestion etc. occurs. Furthermore, preferably realistic data of the traffic density or flow is collected. Given a certain street plan, and a list of departure times, points of origin and destinations of drives, algorithms are designed that determine dynamically how to control the traffic and simulate how late each driver arrives. The output may contain measures such as the traveled distance and the amount of time that drives have been waiting etc. At the end of Phase 1 there should be at minimum a simple simulation environment, demonstrated by the case of special interest in a live demo, where only little to no dynamics and static control (e.g. a traffic light that cycles) are needed. Also, a draft introduction and literature reveiw for the final report is delivered. Obviously, you are most welcome to do more. An overview needs to be provided of which group member contributed to what and to which extent. 

Cars:

    Need origins, destinations, and a time frame to reach destination
    Need to detect collisions with each other
    Need to be able to remove themselves from the street once arriving at the destination

Streets: 

    Need stop lights (Green/Yellow/Red) that direct traffic

Research:

    Traffic control algorithms, flow network design, and literature regarding the study of traffic flow and density.

Notes:

    - Ways to introduce cars would be to create "origin/destination objects" with values 
    that determine how popular they are. Houses would have a value of 1, supermarkets could 
    have a value of 10, and squares/markets could have a value of 100. This value determines 
    how likely a car leaving any given origin is to head towards a specific destination. Cars 
    could randomly spawn on origin objects based on their value, and head towards destinations 
    based on those values.
    - Need to implement pathfinding algorithm/graph structure such that each car is able to stay 
    on the road and reach a destination. A* would probably be our best bet since it's easy to 
    implement.
    - Need a graph structure to build the level on top of. Each road is an edge between two 
    intersections (vertices), roundabouts could be a series of vertices in a circle graph structure.
    Either way, there needs to be some way for cars to navigate a given space.
    - Should we decide to do a level editor, the street objects that we can place/move around 
    must have an edge/vertex attached to them, such that if we place objects in a location 
    next to another, they automatically connect. ***OTHERWISE, A LEVEL EDITOR IS GOING TO 
    BE EXTREMELY DIFFICULT IF NOT IMPOSSIBLE TO IMPLEMENT***
