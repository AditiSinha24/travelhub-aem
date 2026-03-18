# TravelHub AEM Content Platform

A production-grade AEM headless backend for a fictional global travel brand.
Built to showcase enterprise AEM backend development skills for international job applications.

## Tech Stack

- **AEM 6.5** (cloud-compatible structure)
- **Java 11**
- **OSGi R7** with declarative services
- **Apache Sling Models**
- **Content Fragments + GraphQL**
- **JUnit 5 + io.wcm AEM Mocks**
- **Maven multi-module**
- **GitHub Actions CI**

## Architecture

```
travelhub/
├── core/          → OSGi bundle: Sling Models, Services, Servlets, Schedulers
├── ui.apps/       → AEM component definitions, dialog XML
└── ui.content/    → Content Fragment Models, initial content, OSGi configs
```

### Layer breakdown

```
Content Fragment (JCR)
        ↓
Sling Model (pure Java adapter)
        ↓
OSGi Service (business logic)
        ↓
Sling Servlet (REST/JSON API)
        ↓
Consumer (Frontend / Mobile / GraphQL client)
```

## Modules Built

| Module | Description | Status |
|--------|-------------|--------|
| 1 — Content Models | Destination, TourPackage Sling Models + OSGi Service | ✅ Done |
| 2 — Headless APIs | GraphQL + custom Sling Servlet endpoints | 🔄 In progress |
| 3 — Workflows | Auto-tagging workflow step + Sling Scheduler | ⏳ Upcoming |
| 4 — Tests + CI | JUnit 5, AEM Mocks, GitHub Actions | ⏳ Upcoming |

## API Endpoints

| Method | URL | Description |
|--------|-----|-------------|
| GET | `/bin/travelhub/destinations.json` | All destinations |
| GET | `/bin/travelhub/destinations.json?continent=Asia` | Filter by continent |
| GET | `/bin/travelhub/destinations.json?featured=true` | Featured only |

## Local Setup

### Prerequisites
- Java 11 (Amazon Corretto recommended)
- Maven 3.8+
- AEM 6.5 running on `localhost:4502`
- Node.js 18 LTS

### Run AEM
```bash
java -Xmx4096m -jar aem-author-p4502.jar -gui
```

### Build and deploy
```bash
# Full build + deploy to local AEM
mvn clean install -PautoInstallBundle

# Run tests only
mvn test

# Build without deploying
mvn clean install
```

## Test Coverage

Unit tests use **io.wcm AEM Mocks** — no AEM instance required.

```bash
mvn test
```

Key test classes:
- `DestinationModelImplTest` — Sling Model unit tests
- `DestinationServiceImplTest` — Service layer + filtering logic

## Author

Built as a portfolio project demonstrating enterprise AEM backend skills.
- Sling Models with interface/impl separation
- OSGi services with configurable properties
- Custom Sling Servlets exposing JSON APIs
- Unit testing with AEM Mocks (no AEM instance needed)
