<div align="center">

<Br><Br>
https://boardgamehubui.vercel.app
<a href="TUTAJ_WKLEJ_SWOJ_NOWY_LINK_Z_VERCELA" target="_blank">
    <img src="https://img.shields.io/badge/🔴_LIVE_DEMO-Kliknij_aby_przetestować-FF0000?style=for-the-badge" alt="Live Demo" />
  </a>
https://boardgamehubui.vercel.app



<a href="#english">🇬🇧 English</a> | <a href="#polski">🇵🇱 Polski</a>

  <img src="https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21" />
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/Vue.js_3-4FC08D?style=for-the-badge&logo=vuedotjs&logoColor=white" alt="Vue.js" />
  <img src="https://img.shields.io/badge/Tailwind_CSS-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white" alt="Tailwind" />
  <img src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL" />

</div>

---
**Important information about the Live version:** The app's backend is hosted on a free Render.com server. If the app hasn't been used for the last 15 minutes, the server goes to sleep. **Your first login or game download may take 1 to 2 minutes** to wake up the Java virtual machine. Please be patient!

**Test Account (Admin):**
> * **Email:** `test@test.pl`
> * **Password:** `test`

---

<h1 id="english">BoardGameHub - Board Game Cafe & Rental SaaS</h1>

---
> A comprehensive system for managing a board game rental and cafe. The application combines an e-commerce experience for customers with an advanced administrative panel for the staff.

## Key Features

### For Customers:
* **Game Catalog:** Browse available games with search, category filtering, and pagination.
* **Review System:** Read and add reviews (with star ratings) for individual games.
* **Table Reservations:** Book a table for a specific time with an option to request a game waiting on the table.
* **Loyalty Program:** Earn points for rentals (10 pts / game) and get a free rental after collecting 50 points.
* **User Profile:** Full rental history and contact details management (multiple phone numbers support).

### For Staff & Admin:
* **Command Center:** A dedicated view for processing returns and managing reservations (freeing up tables, marking "no-shows").
* **Automations (Cron Jobs):** Background system that automatically detects late returns, applies financial penalties, and cancels forgotten in-store reservations.
* **Analytics Dashboard:** Key Performance Indicators (KPIs) overview – total games, active rentals, penalty revenues, and an interactive category popularity chart.
* **Data Seeder:** Built-in mechanism to generate test data, games, and statistics with a single click.

## Tech Stack

**Backend:**
* Java 21 / Spring Boot 3
* Spring Data JPA / Hibernate
* Spring Security (BCrypt password hashing)
* Database: PostgreSQL

**Frontend:**
* Vue.js 3 (Composition API)
* TypeScript
* Tailwind CSS (Styling & RWD)
* Axios (API communication)
* Chart.js (Data visualization)

## Local Setup

### 1. Database (PostgreSQL)
Ensure you have a PostgreSQL server running on port `5433`. The app connects to a database named `postgres` (user: `postgres`, password: `admin`). You can change this in:
`api/src/main/resources/application.properties`

### 2. Backend (Spring Boot)
Open the `api` folder in your IDE and run the `ApiApplication.java` class. The app will start on port `8080`.

### 3. Frontend (Vue 3)
In a new terminal, navigate to the `frontend` folder and run:
```bash
npm install
npm run dev
```

The frontend will start at http://localhost:5173.

--- 

<h1 id="polski">BoardGameHub - SaaS dla Kawiarni z Planszówkami</h1>

> Kompleksowy system do zarządzania wypożyczalnią i kawiarnią z grami planszowymi. Aplikacja łączy w sobie e-commerce dla klientów z zaawansowanym panelem administracyjnym dla obsługi lokalu.

**Ważna informacja o wersji Live:** Backend aplikacji jest hostowany na darmowym serwerze Render.com. Jeśli aplikacja nie była używana przez ostatnie 15 minut, serwer zasypia. **Pierwsze logowanie lub pobranie gier może potrwać od 1 do 2 minut** w celu wybudzenia maszyny wirtualnej Javy. Proszę o cierpliwość!

**Konto testowe (Admin):**
> * **Email:** `test@test.pl`
> * **Hasło:** `test1234`


## Główne funkcjonalności
### Dla Klienta:
* **Katalog Gier:** Przeglądanie dostępnych gier z wyszukiwarką, filtrowaniem po kategoriach i paginacją.
* **System Opinii:** Możliwość czytania i dodawania recenzji (z oceną gwiazdkową) do poszczególnych gier.
* **Rezerwacje Stolików:** Rezerwacja miejsca w lokalu na konkretną godzinę z opcją "zamówienia" gry, która będzie czekać na stoliku.
* **Program Lojalnościowy:** Zbieranie punktów za wypożyczenia (10 pkt / gra) i możliwość darmowego wypożyczenia po uzbieraniu 50 punktów.
* **Profil Użytkownika:** Pełna historia wypożyczeń i zarządzanie danymi kontaktowymi (wieloma numerami telefonów).

### Dla Pracownika i Administratora:
* **Centrum Dowodzenia:** Osobny widok do przyjmowania zwrotów i obsługi rezerwacji (zwalnianie stolików, oznaczanie "no-show").
* **Automatyzacja (Cron Jobs):** System działający w tle, który automatycznie wychwytuje opóźnienia, nalicza kary finansowe (Penalty Fees) oraz anuluje "zapomniane" rezerwacje na sali.
* **Dashboard Analityczny:** Zestawienie kluczowych wskaźników (KPI) – liczba gier, aktywne * wypożyczenia, przychody z kar oraz interaktywny wykres popularności kategorii.

* **Data Seeder:** Wbudowany mechanizm jednym kliknięciem generujący testowe dane, gry i statystyki do prezentacji aplikacji.

## Stack Technologiczny
**Backend:**
* Java 21 / Spring Boot 3
* Spring Data JPA / Hibernate
* Spring Security (Hasła szyfrowane BCrypt)
* Baza Danych: PostgreSQL 

**Frontend:**
* Vue.js 3 (Composition API)
* TypeScript
* Tailwind CSS (Styling i RWD)
* Axios (Komunikacja z API)
* Chart.js (Wykresy)

## Uruchomienie lokalne
###  Baza Danych (PostgreSQL)
Upewnij się, że masz uruchomiony serwer PostgreSQL na porcie 5433. Aplikacja łączy się z bazą danych o nazwie postgres (użytkownik: postgres, hasło: admin). Możesz to zmienić w pliku:
api/src/main/resources/application.properties

###  Backend (Spring Boot)
Otwórz folder api w swoim IDE i uruchom klasę ApiApplication.java. Aplikacja uruchomi się na porcie 8080.

###  Frontend (Vue 3)
W terminalu przejdź do folderu frontend i wykonaj:

```Bash
npm install
npm run dev
```

Aplikacja uruchomi się pod adresem http://localhost:5173.