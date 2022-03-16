# MissionImpossiable
Implementing a Search agent in a game concept where you have to search for and save injured agents and bring them back to the submarine. The search agent can take the injured agents using a truck.  

Input format:
grid: gridHeight,gridWidth;startingPositionYOfPlayer,startingPositionXOfPlayer;PositionYOfSubmarine,PositionXOfSubmarine;positionsOfAgents;healthOfAgents;capacityOfTruck
Search strategy: can be either: 
  1) Depth first search: DF
  2) Iterative deepening depth-first search: ID
  3) Uniform-Cost Search: UC
  4) Breadth first search: BF
  5) Greedy 1: GR1
  6) Greedy 2: GR2
  7) A* search 1: AS1
  8) A* search 2: AS2
visualize: true or false

Output:
total nodes expanded
Execution time in seconds
Optimum path
