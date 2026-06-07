# TrackTime API — Project Context

## What this project is

TrackTime is a read-only REST API for UK motorsport track day events. It exposes data about racing circuits, track layouts, event organisers, and individual track day events (dates, sessions, noise limits, booking links). The intended consumer is a frontend app (likely a Vite-based SPA given the CORS config for ports 5173/4173).

## Tech stack

- **Language / Runtime**: Java 25
- **Framework**: Spring Boot 4.0.6
- **Database**: PostgreSQL 17 (via Docker Compose locally)
- **ORM**: Spring Data JPA / Hibernate
- **Migrations**: Flyway (scripts under `src/main/resources/db/migration/2026/`)
- **Build**: Maven (with Maven Wrapper)
- **API Docs**: springdoc-openapi with Scalar UI at `/scalar`; raw spec at `/api-docs`

## Domain model

Four entities, all in the `tracktime` schema:

| Entity | Key fields |
|---|---|
| `Track` | id (VARCHAR), name, region, track_map_url |
| `TrackLayout` | id (VARCHAR), track_id (FK), name |
| `Organiser` | id (VARCHAR), name, website_url, logo_url |
| `Event` | id (BIGSERIAL), track_id, track_layout_id (nullable), organiser_id, event_name, start_datetime, end_datetime, session_type, noise limits (NUMERIC 5,1), is_sold_out, booking_url (unique), source, is_verified |

IDs for Track, TrackLayout, and Organiser are short human-readable strings (e.g. `TR_1`, `TL_3`). Event IDs are auto-incrementing bigints.

## Seed data (from Flyway V2)

11 UK tracks: Anglesey, Bedford Autodrome, Brands Hatch, Cadwell Park, Donington Park, Knockhill, Oulton Park, Silverstone, Snetterton, Thruxton, Croft — with 26 layouts across them.

## Package layout

```
com.tracktime.api
├── model/          # JPA entities (Track, TrackLayout, Organiser, Event)
├── dto/            # Java Records for API responses
│   └── shared/     # PagedResponse<T> generic wrapper
├── repository/     # Spring Data JPA repositories
├── mapper/         # Static entity→DTO mappers
├── service/        # Business logic layer
└── web/
    ├── controller/ # REST controllers
    ├── config/     # CorsConfig
    └── exception/  # GlobalExceptionHandler, ResourceNotFoundException, ApiError
```

## REST endpoints

All endpoints are read-only (GET). Base path: `/api`

**Tracks**
- `GET /api/tracks` — all tracks (with nested layouts)
- `GET /api/tracks/{id}` — single track
- `GET /api/tracks/region/{region}` — filter by region
- `GET /api/tracks/{id}/layouts` — layouts for a track

**Organisers**
- `GET /api/organisers` — all organisers
- `GET /api/organisers/{id}` — single organiser

**Events**
- `GET /api/events/{id}` — single event
- `GET /api/events?page=0&size=20` — paginated list
- `GET /api/events/track/{trackId}?page=0&size=20` — paginated by track
- `GET /api/events/organiser/{organiserId}` — by organiser
- `GET /api/events/track/{trackId}/range?start=...&end=...` — by track + date range

## Error handling

`GlobalExceptionHandler` returns a standard `ApiError` record (`status`, `error`, `message`, `path`, `timestamp`) for:
- `ResourceNotFoundException` → 404
- `MethodArgumentNotValidException` → 400
- `MethodArgumentTypeMismatchException` → 400
- Generic exceptions → 500

## Local development

Requires a `.env` file at project root (not committed). Default values:

```
DB_HOST=localhost
DB_PORT=5432
DB_NAME=tracktime_db
DB_USER=admin
DB_PASSWORD=tracktime
DB_SCHEMA=tracktime
```

Start the database: `docker compose up -d`  
Run the app: `./mvnw spring-boot:run`  
Flyway migrations run automatically on startup.

JPA is set to `ddl-auto=validate` — schema must exist before the app starts (Flyway handles this).

## What doesn't exist yet

- No write endpoints (POST/PUT/DELETE)
- No authentication / authorization
- No tests
- No event scraping logic (the `source` and `last_scraped_at` fields suggest scraping is planned)
