# Warriors-BattleSimulation

Warriors-BattleSimulation is a project made for Software Studio classes at AGH-University of Science and Technology.

**Topic:** *Simulation of a historical battle. Project includes agent-base units model and their formation for a chosen historical age.*

**Chosen battle:** [Battle of Karksi](https://en.wikipedia.org/wiki/Battle_of_Karksi_(1600\))

## Project workflow
Project was realized in weekly sprints. Summary of each sprint is available [here (in Polish)](https://drive.google.com/drive/folders/1XckzSlFIfL0pjTU-dsnt97U_lsboZmpH?usp=sharing).

## Project description

- Simulation
    - `Simulation` - contains main class and main loop
    - `Menu` - here you can change simulation details (Regiments' positions and units count). Contains GUI specification
    - `Handler` - manages all `SimulationObjects` lifecycle
- SimulationObjects
    - `SimulationObject` - abstract class, root-parent class for class hierarchy.
    - `ArmyUnit` - class containing basic functionalities of 'attacking units'
    - other classes - self-explanatory
- Statistics
    - `DataCollector` - class responsible for collecting simulation data
    - `ScatterPlot` - class responsible for generating charts from simulation data

SimulationOutput contains example simulation output (*.jpeg files and CSV)

## Installation
```
$ mvn clean install
$ java -jar target/Warriors-BattleSimulation-1-jar-with-dependencies.jar
```

## Simulation VS Master

At some point plain version of simulation (`master` branch) and Karksi specific (`Simulation` branch) have diverged. Main differences are:
- FPS count (5 vs 60)
- Explicit data printing after simulation ends (in `Simulation` branch)
- parameters changes to get plausible simulation results

That changes are rather small, but we wanted to keep as simulation-specific-free version as possible, so that's why we have split it into 2 separate branches. 


## Results

Charts
![Chart_HP](https://github.com/MarcinKozak005/Warriors-BattleSimulation/tree/master/SimulationOutput/Mon_Aug_03_22-32-49_CEST_2020(HP).jpeg)
![Chart_NUMBER](https://github.com/MarcinKozak005/Warriors-BattleSimulation/tree/master/SimulationOutput/Mon_Aug_03_22-32-49_CEST_2020(Number).jpeg)

[YouTube movie with final Karksi simulation](https://www.youtube.com/watch?v=9onQhgWORYE)


## External Libraries and Sources
- [JFreeChart](http://www.jfree.org/jfreechart/) (GNU Lesser General Public Licence)
- [RealTutsGML Java Game tutorial](https://www.youtube.com/watch?v=1gir2R7G9ws&list=PLWms45O3n--6TvZmtFHaCWRZwEqnz2MHa) - core of the project and GUI stuff
- F. W. Lanchester: Aircraft in Warfare: the Dawn of the Fourth Arm (1916)
- An Agent-Based Model of the Battle of Isandlwana - C. J. Scogings and K. A. Hawick 2012
- An Agent-Based Computational Model for the Battle of Trafalgar - G.Trautteur and R. Virgilio
- Towards a Science of Experimental Complexity: An Artificial-Life Approach to Modeling Warfare. - A. Ilachinski
- An Agent-Based Simulation of the Battle of Kokenhausen- Marcin Waniek
- Petro: a multi-agent model of historical warfare - Marcin Waniek
