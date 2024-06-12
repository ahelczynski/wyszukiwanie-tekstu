# wyszukiwanie-tekstu

## Opis

wyszukiwanie-tekstu to aplikacja JavaFX, która przeszukuje wszystkie pliki o rozszerzeniu `.txt` w wskazanym katalogu w poszukiwaniu wzorca podanego przez użytkownika. Wzorzec może zawierać `*` jako oznaczenie ciągu dowolnych znaków (również ciągu pustego) lub `?` jako oznaczenie dokładnie jednego dowolnego znaku. Aplikacja uruchamia wyszukiwanie po naciśnięciu przycisku "Szukaj" lub po zatwierdzeniu wzorca klawiszem Enter.

## Funkcje

- Wybór katalogu do przeszukiwania
- Wprowadzanie wzorca wyszukiwania
- Obsługa wzorców z `*` (dowolny ciąg znaków) i `?` (dokładnie jeden znak)
- Wyświetlanie wyników w formacie:
  - Nazwa pliku
  - Numer linii
  - Znaleziony fragment spełniający wzorzec

## Wymagania

- Java
- JavaFX 17

## Instalacja

1. Skopiuj kod źródłowy do odpowiednich plików w katalogu `src/main/java`.
2. Należy zmodyfikować ścieżke w pliku launch.json "vmArgs": "--module-path \"C:/Users/Admin/Desktop/AEH/Programowanie/wyszukiwanie-tekstu/javafx-sdk-22.0.1/lib\" --add-modules javafx.controls,javafx.fxml", tak aby wskazywała prawidłowo umieszczony katalog javafx-sdk-22.0.1/lib

# Użytkowanie
1. Uruchom aplikację.
2. Kliknij przycisk "Wybierz katalog" i wybierz katalog do przeszukania.
3. Wprowadź wzorzec wyszukiwania w polu tekstowym "Wprowadź szukany tekst:".
4. Naciśnij przycisk "Szukaj" lub klawisz Enter, aby rozpocząć wyszukiwanie.
5. Wyniki wyszukiwania zostaną wyświetlone w obszarze tekstowym po prawej stronie.

# Autor
Adam Ostoja-Hełczyński