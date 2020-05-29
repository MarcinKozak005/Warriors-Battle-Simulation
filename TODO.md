## Final TODOs:
- sprzątnij kod
- Simulation VS Master

## SOLVED:

1. main.java.SimulationObjects.Regiment: RozHardcodowanie main.java.SimulationObjects.Regiment.formationSquare()
    SOLVED: Zrobione
1. main.java.SimulationObjects.Peasant (chyba): Kolizje (overlaping) między nimi- zeby się nie nachodzili:
	- naprawione w obrębie jednego pułku, ale wrogowie nadal nachodzą na siebie, załatwienie tego w taki sam sposób zatyka czasem symulację
	SOLVED: Na razie nie powinni na siebie nachodzić- jak są blisko to się atakują (czyli są nieruchomo). Więc nie powinno raczej dojść do sytuacji, że na siebie "wejdą"	
2. main.java.SimulationObjects.Regiment: Decyzja na podstawie obserwacji
    SOLVED: Dodano
3. main.java.SimulationObjects.Regiment: Żeby się ruszał wtedy kied wydaje MoveToAttackOrder()
    SOLVED: Dodano
4. main.java.Simulation.Handler: Zwracanie najblliższego pulku wroga a nie pierwszego lepszego
    SOLVED: Dodano
5. main.java.Simulation.Handler/main.java.SimulationObjects.Regiment: Usuwanie pustego Regimentu
    SOLVED: Dodano- jak przy usuwaniu zabitych jednostek- za pomocą List<main.java.SimulationObjects.Regiment> toRemove i funkcji safeToRemove(). Zeby nie wyskakiwał wyjątek modyfikacji przy iteracji
6. main.java.SimulationObjects.Peasant: funkcja do ruchu (jest zduplikowany kod ... ?)
    SOLVED: Dodano funkcję w main.java.SimulationObjects.SimulationObject.setDirectionTo(main.java.SimulationObjects.SimulationObject)
1. Ogólne: Rozmieszczenie plików do package's
    SOLVED: Zrobione
1. main.java.SimulationObjects.Peasant: Atak wrogów i innych Regimentów?
    SOLVED: Zrobione
1. main.java.SimulationObjects.Peasant: Jak zaklinowany, to zmiana focus'a?
    SOLVED: O rajuśku 
1. Get Actual Velocity
    SOLVED: Zrobione
1. Przejżeć logikę chase czasami szuka a nie musi- time complexity
    SOLVED: Zrobione
1. Victory rzucane
    SOLVED: Zrobione
1. To że jest ten mega kill
    SOLVED: Zrobione
1. Mam dziwne wrażenie że jak są 3 pulki i dwa sojuszcznicze łączą się w jeden to jest coś dziwnego i nagle jedne jednostki dostają jakby boosta i zabijają dużo wrogów nie ginąc ...?
Nie wiem z wykresu nie do końca to widać chyba ... Na podaję kod main.java.Simulation do ewentualnego odtworzenia
    SOLVED: Związane z tym powyżej
1. Jednostki w pułku poruszają się z np: 80% szybkości, ale w ataku już 100% ?
    SOLVED: velocityModificator
1. HP Zchodzi poniżej 0 w wykresie czasami
    SOLVED: Zrobione ArmyUnit#takeDamage()
1. Ziomki co wchodza na siebie przy mergu dwóch oddziałów
    SOLVED: Zrobione- sprawdzamy jeszcze pozostałe oddziały
1. Fix double Retreat (czasami obydwa zaczynają się cofać jak jeden w trakcie odwrotu wybije drugiego ...)
    SOLVED: NaN/Infinity w setDirectionTo
1. Małe blokowania przy gonieniu i wchodzenie w środek regimentu?
    SOLVED: Zrobione
    
## HALF-SOLVED:
1. main.java.Simulation.Handler: Lista na WSZYSTKIE main.java.SimulationObjects.ArmyUnit -> do losowego wykonywania ...?
    HALF-SOLVED: Przeprowadziłem testy- ustawiłem atak jednostki na 120 i nie było tak, żeby pierwsi dodani do Handlera (niebiescy) wybili drugich (czerwonych)
        Generalnie wydaje mi się, że nie trzeba z tym na razie nic robić bo jest pseudolosowość. Zostawiam, bo może kiedyś wyjść problem.
1. main.java.SimulationObjects.Peasant: Math.random na Random w dealDMGToEnemy():
    HALF-SOLVED: obrażenia są teraz losowane jako wartości z mniej więcej rozkładu normalnego. Docelowo chciałabym, żeby to nie był dokładnie rozkład normalny, tylko taki przesunięty. (EDIT: wydaje mi się, że jest przesunięty jak chciałam)
1. Ogólne: Wyskalowanie wszystkich wartości np:
    - radiusy Regimentu
    - siła ataku main.java.SimulationObjects.Peasant
    - wielkośc main.java.SimulationObjects.Peasant itp, itd.
    HALF-SOLVED: prawdopodobnie potrzebne będą następne przeskalowania, dlatego zostawiam to tutaj
1. Nowa Klasa: Dodanie klasy Spawner, która będzie tworzyła jednostki w Regimencie? Takie wyniesienie metody tworzących żołnierzy do nowej klasy
    HALF_SOLVED: Nie ma czegoś takiego, ale są metody spawnujące w Regiment
1. Cos z tym main.java.Simulation Height nie działa chyba do końca - przy ucieczce uciekają trochę za ekran na dole imo :/
    HALF_SOLVED: Jest odejmowana zahardcodowana wartość przy sprawdzaniu czy wyszedł poza krawędź. Myślę, że ma to związek z tym jak Swing/Awt liczą rozmmiar okna... Z tym paskiem chyba i to może być problem
1. Error jak regiment sie spawnował poza zasięgiem ... ?
    HALF_SOLVED: Nie widzę- pewnie coś innego

## POMYSŁY:
1. Move without Collision na Regimentach OOoooooo ?????

## NIE WYKONANE
1. Castowania na Regiment w Handlerze mi się nie podobają ... (dużo zmian?)
1. LOW PRIORITY: main.java.Simulation.main.java.Simulation: main.java.Simulation.Handler jako Singleton? Czy jest sens i warto?
1. LOW PRIORITY: Ogólne: GetMinimal? - zauważyłem że często liczymy jakieś minimum ze wszystkich obiektów- może funkcja do tego ...?
