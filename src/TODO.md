## TODO
1. Simulation.Simulation: Simulation.Handler jako Singleton? Czy jest sens i warto?
1. Nowa Klasa: Dodanie klasy Spawner, która będzie tworzyła jednostki w Regimencie? Takie wyniesienie metody tworzących żołnierzy do nowej klasy
1. Ogólne: GetMinimal? - zauważyłem że często liczymy jakieś minimum ze wszystkich obiektów- może funkcja do tego ...?
1. SimulationObjects.Infantry: Jak zaklinowany, to zmiana focus'a?

    **Materiały**:
    
    - Z SimulationObjects.Infantry
```java
/* else {
    // tried to change unit we're focused on, sometimes it works, sometimes units will get stuck anyway
    this.attackOrder(myEnemy.myRegiment);
} */

// TODO for now the unit won't move as not to walk into any other unit
// possible solutions:
// * change the unit's myEnemy
// * get the unit to move along a curve to the targeted enemy instead of the straight line
// * get the unit to get as close as possible instead of not moving at all
```




## POMYSŁY:
1. Jednostki w pułku poruszają się z np: 80% szybkości, ale w ataku już 100% ?




## HALF-SOLVED:
1. Simulation.Handler: Lista na WSZYSTKIE SimulationObjects.ArmyUnit -> do losowego wykonywania ...?
    HALF-SOLVED: Przeprowadziłem testy- ustawiłem atak jednostki na 120 i nie było tak, żeby pierwsi dodani do Handlera (niebiescy) wybili drugich (czerwonych)
        Generalnie wydaje mi się, że nie trzeba z tym na razie nic robić bo jest pseudolosowość. Zostawiam, bo może kiedyś wyjść problem.
1. SimulationObjects.Infantry: Math.random na Random w dealDMGToEnemy():
    HALF-SOLVED: obrażenia są teraz losowane jako wartości z mniej więcej rozkładu normalnego. Docelowo chciałabym, żeby to nie był dokładnie rozkład normalny, tylko taki przesunięty. (EDIT: wydaje mi się, że jest przesunięty jak chciałam)
1. Ogólne: Wyskalowanie wszystkich wartości np:
    - radiusy Regimentu
    - siła ataku SimulationObjects.Infantry
    - wielkośc SimulationObjects.Infantry itp, itd.
    HALF-SOLVED: prawdopodobnie potrzebne będą następne przeskalowania, dlatego zostawiam to tutaj

## SOLVED:


1. SimulationObjects.Regiment: RozHardcodowanie SimulationObjects.Regiment.formationSquare()
    SOLVED: Zrobione
1. SimulationObjects.Infantry (chyba): Kolizje (overlaping) między nimi- zeby się nie nachodzili:
	- naprawione w obrębie jednego pułku, ale wrogowie nadal nachodzą na siebie, załatwienie tego w taki sam sposób zatyka czasem symulację
	SOLVED: Na razie nie powinni na siebie nachodzić- jak są blisko to się atakują (czyli są nieruchomo). Więc nie powinno raczej dojść do sytuacji, że na siebie "wejdą"	
	
2. SimulationObjects.Regiment: Decyzja na podstawie obserwacji
    SOLVED: Dodano
3. SimulationObjects.Regiment: Żeby się ruszał wtedy kied wydaje MoveToAttackOrder()
    SOLVED: Dodano
4. Simulation.Handler: Zwracanie najblliższego pulku wroga a nie pierwszego lepszego
    SOLVED: Dodano
5. Simulation.Handler/SimulationObjects.Regiment: Usuwanie pustego Regimentu
    SOLVED: Dodano- jak przy usuwaniu zabitych jednostek- za pomocą List<SimulationObjects.Regiment> toRemove i funkcji safeToRemove(). Zeby nie wyskakiwał wyjątek modyfikacji przy iteracji
6. SimulationObjects.Infantry: funkcja do ruchu (jest zduplikowany kod ... ?)
    SOLVED: Dodano funkcję w SimulationObjects.SimulationObject.setDirectionTo(SimulationObjects.SimulationObject)
1. Ogólne: Rozmieszczenie plików do package's
    SOLVED: Zrobione
1. SimulationObjects.Infantry: Atak wrogów i innych Regimentów?
    SOLVED: Zrobione

