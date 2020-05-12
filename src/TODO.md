## TODO
1. Castowania na Regiment w Handlerze mi się nie podobają ... (dużo zmian?)
1. Mam dziwne wrażenie że jak są 3 pulki i dwa sojuszcznicze łączą się w jeden to jest coś dziwnego i nagle jedne jednostki dostają jakby boosta i zabijają dużo wrogów nie ginąc ...?
Nie wiem z wykresu nie do końca to widać chyba ... Na podaję kod Simulation do ewentualnego odtworzenia
1. Move without Collision na Regimentach OOoooooo ?????
1. LOW PRIORITY: Simulation.Simulation: Simulation.Handler jako Singleton? Czy jest sens i warto?
1. LOW PRIORITY: Ogólne: GetMinimal? - zauważyłem że często liczymy jakieś minimum ze wszystkich obiektów- może funkcja do tego ...?

```java
class Test{ // Opakowane w klasę żeby IntelliJ się nie czepiał
    public Simulation()
    {
        handler = new Handler();

        Regiment r1 = new Regiment(300,300, Alliance.Red,"Right", handler);
        r1.formationSquare(10, false);
//        r1.addArmyUnit(new Infantry(300,300));
//        Regiment r2 = new Regiment(300,600, Alliance.Red, "Left", handler);
//        r2.formationSquare(5, false);
//        r1.addArmyUnit(new Infantry(300,300));


        Regiment r3 = new Regiment(600,300, Alliance.Red, "Main", handler);
        r3.formationSquare(20, false );
//        r2.addArmyUnit(new Infantry(600,300));
        Regiment r4 = new Regiment(600,600, Alliance.Blue, "Main", handler);
        r4.formationSquare(22, false );
//        r2.addArmyUnit(new Infantry(600,300));

        //Regiment r3 = new Regiment(600, 600 , Alliance.Red, handler);
        //SimulationObjects.Regiment r3 = new SimulationObjects.Regiment(600,450, Enums.Alliance.Red, handler);
        //r2.addArmyUnit(new SimulationObjects.Infantry(1600,700));
        //r2.addArmyUnit(new SimulationObjects.Infantry(1620,700));
        //r2.addArmyUnit(new SimulationObjects.Infantry(1640,700));
        //r2.populateRegimentWithUnits(25);
        //r3.formationSquare(5,false);


        handler.addRegiment(r1);
//        handler.addRegiment(r2);
        handler.addRegiment(r3);
        handler.addRegiment(r4);

        new Window(SCREEN_WIDTH, SCREEN_HEIGHT, "Warriors Simulation.Simulation", this);
    }
}
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
1. Nowa Klasa: Dodanie klasy Spawner, która będzie tworzyła jednostki w Regimencie? Takie wyniesienie metody tworzących żołnierzy do nowej klasy
    HALF_SOLVED: Nie ma czegoś takiego, ale są metody spawnujące w Regiment
1. Cos z tym Simulation Height nie działa chyba do końca - przy ucieczce uciekają trochę za ekran na dole imo :/
    HALF_SOLVED: Jest odejmowana zahardcodowana wartość przy sprawdzaniu czy wyszedł poza krawędź. Myślę, że ma to związek z tym jak Swing/Awt liczą rozmmiar okna... Z tym paskiem chyba i to może być problem

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
1. SimulationObjects.Infantry: Jak zaklinowany, to zmiana focus'a?
    SOLVED: O rajuśku 
1. Get Actual Velocity
    SOLVED: Zrobione
1. Przejżeć logikę chase czasami szuka a nie musi- time complexity
    SOLVED: Zrobione
1. Victory rzucane
    SOLVED: Zrobione
    
