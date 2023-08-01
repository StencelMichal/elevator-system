# **System sterowania windami**

## Założenia:
* Interakcja z systemem odbywa się poprzez interfejs terminalowy
* W jednym kroku symulacji każda winda pokonuje odległość jednego piętra
* Winda posiada zakres ruchu - minimalne i maksymalne piętro. W przypadku nie podania w konfiguracji informacji o windzie, zostaną jej ustawione, dostarczone w konfirugacji, wartości domyślne dla systemu
* Winda posiada zbiór pięter które powinna odwiedzić
* Winda może znajdować się w 3 stanach
  * jazda w górę
  * jazda w dół
  * bezczynność
* Ruch windy odbywa się cyklicznie góra dół. Oznacza to, że winda będzie jechać tak długo w aktualnym kierunku, dopóki ma złoszenia na kolejnych piętrach. Dopiero kiedy dotrze do ostatnigo piętra w aktualnym kierunku, pojedzie w drugą stronę lub stanie się bezczynna


## Algorytm wyboru windy:
Windy przydzielane są w zachłanny sposób, poprzez wyliczenie najkrótszej "odległości" w do danego piętra i jazdy w określonym kierunku w momencie złgoszenia.
Liczenie odległości:
* Winda jest nieaktywna lub jedzie zgodnie z kierunkiem zgłoszenia, ale jest jeszcze przed piętrem zgłaszającego.
 
  Odległość to liczba pięter między aktualną pozycją windy, a zgłoszonym piętrem


* Winda jedzie w przeciwnym kierunku:
  Odległość to suma:
  * liczby pięter od aktualnej pozucji do ostatniego piętra w aktualnym kierunku
  * liczby pięter od ostatniego piętra do zgłaszanego piętra
  

* Winda jedzie zgodnie z kierunkiem zgłoszenia, ale minęła już piętro zgłaszającego:
  Odległość to suma:
  * liczby pięter do ostatniego zgłoszenia w aktualnym kierunku
  * liczby pięter od między skrajnymi piętrami w kolejce
  * liczby pięter od ostatniego piętra w przeciwnym kierunku do piętra zgłaszającego

## Uruchomienie:
Projekt jest budowany przez SBT. W celu uruchomienia należy wpisać:

`sbt run`

## Interakcja
System można kontrolować poprzez wpisywanie komend w terminalu.
Lista dostępnych komend:
* **pickup:**
  * description: Calls an elevator to a specific floor with the intention of traveling in a particular direction
  * parameters:
    * [1] floor number
    * [2] elevator direction, possible values are `up` or `down`
example: `pickup 3 down`
* **step:**
  * description: Performs simulation step
  * example: `step`
* **status:**
  * description: Show status of current simulation
  * example: `status`
* **update:**
  * description: Updates elevator state of given id
  * parameters:
    * [1] elevatorId
    * [2] the floor to which the elevator state should be changed
    * [3] whitespace-separated array of floor numbers in square brackets - list of floors to which the elevator should go next
  * example: `update 3 1 [4 5]`
* **help:**
  * description: Prints help with all possible commands
  * example: `help`

## Dalsze możliwości rozwinięcia systemu
* Lepsze pokrycie kodu testami. Aktualnie testy sprawdzają poprawność wyznaczania windy, która powinna zostać wybrana przez algorytm. W szczególności potrzebne są testy sprawdzające poprawność odpowiedzi po interakcji użytkownika z systemem.
* W aktualnej postaci CLI komunikuje się bezpośrednio z symulacją. Należałoby rozdzielić CLI od symulacji poprzez API. Dogodnym do tego miejscem byłby `CommandLineDispatcher`, który powinien wykonywać requesty/delegować wykonanie requestów do symulacji. W ten sposób, mielibyśmy możliwość interakcji z systemem na inny sposoby: GUI, HTTP, Aplikacja webowa... Dzięki temu można by również uruchomić dwie instacje CLI, jedna mogłąby stanowić podgląd live aktualnego stanu symulacji, a druga operować symulacją.
* Lepsze wykorzystanie logowania. Zdecydowanie powinno się dodać więcej logowania. W szególności po stronie symulacji.
* Tracing przebiegu requestów.
* Stworzenie interaktywnej dokumentacji po wystawieniu wszystkich operacji na symulacji do API.
* Lepsze zarządzanie stanem symulacji. W obecnej postaci stan nie jest wystarczająco dobrze chroniony. Potencjalnie można wyeliminować konieczność stosowania var'ów poprzez wykorzystanie Akki i przetrzymywanie stanu symulacji jako stanu aktora.
* Powrót windy na domyśle piętro po określonym czasie bezczynności. Aktualnie winda posiada `defaultFloor`, dlatego należałoby dodać zliczanie ilość kroków symulacji bez zmiany stanu windy. Po przekroczeniu konfigurowalnej wartośći winda powinna dostać zgłoszenie powrotu na domyśle piętro. Feature jest o tyle skompikowany, że powinniśmy wprowadzić typowanie tasków dla windy. W ten sposób moglibyśmy anulować powrót windy na domyśle piętro, kiedy dostanie ona jakieś inne zgłosznie.
* Awaryjne wyłączenie systemu. Należałoby dodać możliwość awaryjnego wyłączenia systemu, które powinno się również samo uruchamiać jako "graceful shutdown" przy wyłączniu systemu. Wszystkie windy w tym momencie powinny przestać przyjmować zgłoszenia i zjechać na najniższe piętro w budynku (należałoby to dodać do konfiguracji)
* Przemyślenie odpowiedzialności w systemie. Aktualnie wszystkie requesty są ostatecznie obsługiwane przez `SimulationEngine`. Wina to z faktu jak najmniejszego dostępu do mutowalnego stanu symulacji. Należałoby przemyśleć czy jest to dobre podejście.
* Symulacja live. Można dodać komendę, po wpisaniu której wykonywane byłby okresowo zapytania o stan symulacji i odświeżanie terminala.
* Lepsze zarządzanie wyjątami. Dobrze byłoby łapać wyjątki spowodowane przez użytkownika, np. podanie piątra poza zakresem bydunku i wyświetlać odpowiednie komunikaty w terminalu
* Usprawnienie algorytmu wyboru windy. W metryce "odległości" od zgłaszającego można by uwzględniać liczbę przystanków, które winda będzie musiała pokonwać w celu dotarcia do zgłaszającego
* Okresowe przeliczanie optymalności przydzielonych windzie tasków. Ze względu na zastosowanie zachłannego sposobu wybierania windy, może się okazać, że po kilku krokach symulacji, jest winda, która jest w stanie szybciej obsłużyć nasze zgłoszenie.
