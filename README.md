## INFO6205_519
- Boyang Wei  	001883667
- Changsi Liu		001831955
## Problem Description:  
Magic Tower (http://www.flashgames247.com/misc-game/magic-tower.html), is an interesting flash game with lots of strategy decisions and calculation. 
Player control a hero moving in the magic tower, open doors, kill monsters, solve puzzles and finally save the princess.  
In our genetic algorithm project, we simplified this game, only leave the maze part with a key and door puzzle. 
In detail, our algorithm will try to find a key in the maze first, 
then use this key to open a gate and finally find the way to the next level of the tower.   

## 1.	Genetic code
- Genetic class is one individual hero. Every hero has 150 genes so every hero can move at most 150 steps in the maze. 
Genes in Genetic(hero) class can be created by a random generator or generating from parents. 
- Mutation method is also supported, the rate is 0.001, this means every gene have a 0.1% possibility to mutate. Approximately, the possibility of mutation happened is 86% in one individual hero.

## 2.	Gene expression
- Gene in our algorithm is the direction of moving, coded by int 0,1,2,3 (type), means move DOWN, LEFT, UP, RIGHT (gene expression) in the maze.  
|---- | --- |
|Genotype|phenotype |
|0 | DOWN |
|1 | LEFT |
|2 | UP |
|3 | RIGHT |

## 3.	Fitness function
	The fitness function is in our MagicTower class. Generic (hero) class does not know whether he has a key, but a status and score returned from MagicTower class.
We use Breadth-first search to find the shortest path and distance of every position in the maze to “end”, “key” and “gate” positions. The more achievements the hero get, for example, finding a key worth 1000 point, the more score he will get. 

