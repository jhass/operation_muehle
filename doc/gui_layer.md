# Die GUI-Schicht

## Konzept

In diesem Abschnitt werden die einzelnen Klassen und ihre Funktion innerhalb der GUI erläutert. Jegliche direkte Kommunikation wird über die Fassade abgewickelt.

### Der GUIController

Der GUIController ist der Dreh- und Angelpunkt, er ist dafür verantwortlich die Aktionen in den einzelnen Fenstern auszulösen, sie anzuzeigen und zu verstecken und Information von ihnen zu sammeln. Daher kennt er alle anderen Klassen, die Fenster  Er sorgt auch für eine saubere Beendung der Schicht.

### Das MainWindow

Dieses Fenster ist die Hauptinteraktionsfläche für den Benutzer. Von hier aus kann er ein neues Spiel starten, ein bisheriges Laden, im Spiel speichern, das Log aufrufen und das Programm beenden. Nicht zu letzt stellt es das Spielfeld dar und stellt die Interaktion mit ihm bereit. Hierzu wird es als Observer für das Gameboard in der Anwendungsschicht registriert und holt sich bei Änderungen über die Fassade das aktuelle Objekt.

### Der NewGameDialog

Dieser Dialog dient zum konfigurieren eines neuen Spiels. Er fragt alle Einstellungsmöglichkeiten ab und liefert diese dann an den Controller zurück der über die Fassade ein neues Spiel in der Anwendungsschicht initialisiert.


### Der GetPathDialog und seine Kinder

Der SaveDialog und LoadDialog haben die gleiche Grundaufgabe, es gilt einen Pfad für einen Speicherstand vom Benutzer abzufragen, in den Kindern wird also nur die Präsentation für den Benutzer entsprechend angepasst.

### Das LogWindow

Das LogWindow ist ein einfaches Textfeld welches das Log anzeigt. Hierzu wird es über die Fassade als Observer für das Logger Objekt registriert und holt sich bei Änderungen über die Fassade das aktuelle Log.

