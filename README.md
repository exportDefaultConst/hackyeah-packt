# PacktBackSB – Backend projektu zespołowego

## Spis treści
1. [Opis projektu](#opis-projektu)
2. [Architektura i struktura systemu](#architektura-i-struktura-systemu)
3. [Schemat systemu](#schemat-systemu)
4. [Użyte technologie](#użyte-technologie)
5. [Instrukcja instalacji środowiska](#instrukcja-instalacji-środowiska)
6. [Proces deploymentu](#proces-deploymentu)
7. [Endpointy API](#endpointy-api)
8. [Licencja](#licencja)

---

## Opis projektu
PacktBackSB to backendowa aplikacja napisana w Javie z wykorzystaniem Spring Boot, służąca do zarządzania grupami, wydarzeniami, dokumentami oraz rozliczeniami w ramach projektu zespołowego. Celem jest zapewnienie bezpiecznego i skalowalnego API dla aplikacji frontendowej oraz automatyzacja procesów grupowych.

## Architektura i struktura systemu
Projekt oparty jest o architekturę warstwową:
- **controllers/** – kontrolery REST API
- **dto/** – obiekty transferowe danych
- **models/** – encje JPA (mapowanie na bazę danych)
- **repositories/** – repozytoria Spring Data
- **services/** – logika biznesowa
- **config/** – konfiguracje bezpieczeństwa, integracje
- **utils/** – klasy pomocnicze

Zależności:
- Kontrolery korzystają z serwisów
- Serwisy korzystają z repozytoriów
- Repozytoria operują na encjach
- DTO służą do komunikacji z frontendem

## Schemat systemu
```
+-------------------+
|   controllers     |
+-------------------+
          |
          v
+-------------------+
|     services      |
+-------------------+
          |
          v
+-------------------+
|   repositories    |
+-------------------+
          |
          v
+-------------------+
|     models        |
+-------------------+
```

## Użyte technologie
- **Java 21** ([dokumentacja](https://docs.oracle.com/en/java/))
- **Spring Boot 3.4.3** ([dokumentacja](https://spring.io/projects/spring-boot))
- **PostgreSQL** (driver zarządzany przez Spring Boot) ([dokumentacja](https://www.postgresql.org/docs/))
- **Firebase Admin SDK 9.2.0** ([dokumentacja](https://firebase.google.com/docs/admin/setup))
- **Maven** ([dokumentacja](https://maven.apache.org/))
- **Docker** ([dokumentacja](https://docs.docker.com/))
- **docker-compose** ([dokumentacja](https://docs.docker.com/compose/))

## Instrukcja instalacji środowiska
1. Zainstaluj JDK 21+, Maven, Docker oraz docker-compose.
2. Sklonuj repozytorium:
   ```bash
   git clone <adres_repozytorium>
   cd PacktBackSB
   ```
3. Skonfiguruj plik `src/main/resources/application.properties` (przykład poniżej).
4. **[OBOWIĄZKOWE]** Wygeneruj i dodaj plik `packt-firebase-adminsdk.json`:
   - Przejdź do [Firebase Console](https://console.firebase.google.com/)
   - Wybierz swój projekt lub utwórz nowy
   - Przejdź do **Project Settings** → **Service Accounts**
   - Kliknij **Generate New Private Key**
   - Zapisz pobrany plik jako `packt-firebase-adminsdk.json` w katalogu `src/main/resources/`
   - ⚠️ **UWAGA**: Ten plik zawiera wrażliwe dane i NIE MOŻE być commitowany do repozytorium (jest w `.gitignore`)


## Proces deploymentu
### Lokalnie
Najszybszy sposób uruchomienia aplikacji lokalnie to użycie Docker Compose:
Ta komenda automatycznie:
- Zbuduje obraz aplikacji
- Uruchomi bazę danych PostgreSQL
- Uruchomi aplikację Spring Boot
- Połączy wszystkie usługi
```bash
docker-compose up --build
```
- Zatrzymywanie oraz wyczyszczenie pamięci:
  ```bash
  docker-compose down
  ```
- Sprawdzanie logów:
  ```bash
  docker-compose logs app
  ```
  
Aplikacja będzie dostępna pod adresem `http://localhost:8081`

#### Troubleshooting
- Sprawdź czy porty 8081 (backend) i 5433 (Postgres) są wolne.
- W razie problemów z migracją bazy, usuń wolumen: `docker volume rm packtbacksb_postgres-data`
- Upewnij się, że plik `packt-firebase-adminsdk.json` jest obecny.

## Endpointy API

### Kody odpowiedzi HTTP
Aplikacja zwraca standardowe kody statusu HTTP:

| Kod | Status              | Opis                                                    |
|-----|---------------------|---------------------------------------------------------|
| 200 | OK                  | Operacja zakończona sukcesem                            |
| 201 | Created             | Zasób został pomyślnie utworzony                        |
| 400 | Bad Request         | Nieprawidłowe dane w żądaniu                            |
| 401 | Unauthorized        | Brak autoryzacji (brak tokenu lub token nieprawidłowy)  |
| 403 | Forbidden           | Brak dostępu do zasobu, nieodpowiednia rola             |
| 404 | Not Found           | Brak strony/brak rekordu w bazie danych                 |
| 409 | Conflict            | Rekord już istnieje (np. duplikat email/username)       |
| 500 | Internal Server Error | Błąd serwera, nieoczekiwany problem po stronie backendu |

### Lista endpointów
| Metoda | Endpoint                | Opis                        | Autoryzacja | Statusy |
|--------|------------------------|-----------------------------|-------------|---------|
| GET    | /api/users             | Lista użytkowników          | Tak         | 200, 401, 403|
| POST   | /api/groups            | Tworzenie grupy             | Tak         | 201, 400, 401|
| GET    | /api/events            | Lista wydarzeń              | Tak         | 200, 401|
| POST   | /api/invitations       | Zaproszenie do grupy        | Tak         | 201, 400, 401, 409|
| GET    | /api/documents         | Pobranie dokumentów         | Tak         | 200, 401, 404|
| POST   | /api/expenses          | Dodanie wydatku             | Tak         | 201, 400, 401|
| ...    | ...                    | ...                         | ...         | ...     |

Przykładowe zapytanie:
```http
GET /event/<group_id>
Authorization: Bearer <token>
```
```json
[
  {
    "id": 28,
    "eventName": "Muzyka",
    "category": "Koncert Rockowy",
    "description": "Koncert zespołu rockowego w klubie.",
    "localization": "Góralska 5",
    "startEvent": "2025-12-30T20:00:00",
    "endEvent": "2025-12-31T02:00:00",
    "creator": "b8aY27S9hgfYpizkjfoAlxoKR8G2",
    "participants": [
      {
        "user": {
          "UId": "b8aY27S9hgfYpizkjfoAlxoKR8G2",
          "userName": "Damian Kowalski",
          "email": "kowalski@wp.pl"
        },
        "role": "CREATOR"
      },
      {
        "user": {
          "UId": "b8aY27S9hgfYpizkjfoAlxoKR8G",
          "userName": "Jakub Nowak",
          "email": "nowak@wp.pl"
        },
        "role": "MEMBER"
      }
    ]
  }
]
```


## Licencja
Projekt udostępniany na licencji CCB (Czym Chata Bogata)

---
