# Paddock Marketplace — Task Breakdown

**Project:** Paddock Motorsport Marketplace  
**Stack:** Java 25 / Spring Boot 4 · React + shadcn/Tailwind · PostgreSQL · Auth0 · Cloudflare R2 · Stripe · Resend  
**Approach:** Backend-first, vertical slices — each phase is end-to-end shippable  
**Last Updated:** June 2026

---

## How to Read This Document

- `[ ]` = not started
- `[x]` = complete
- **B:** = Backend (Spring Boot)
- **F:** = Frontend (React)
- **I:** = Infrastructure / config
- Tasks within a phase are roughly ordered — complete top to bottom

---

## Phase 1 — Foundation

> Everything else depends on this. No visible UI at the end, but the skeleton is solid.

### Database

- `[ ]` **B:** Create `paddock_api` Spring Boot module for marketplace (or confirm integration with existing calendar app structure)
- `[ ]` **I:** Provision PostgreSQL instance (Railway or Supabase) for UK deployment
- `[ ]` **I:** Set up `.env` / `application.yml` environment variable structure with market config (`MARKET`, `CURRENCY`, `BASE_URL`)
- `[ ]` **B:** Add Flyway (or Liquibase) for database migration management
- `[ ]` **B:** Write migration `V1__create_users_table.sql`
  - `id` (UUID), `email`, `auth0_id`, `display_name`, `phone_number`, `email_verified`, `role` (ENUM: `USER`, `ADMIN`), `banned`, `created_at`
- `[ ]` **B:** Write migration `V2__create_listings_table.sql`
  - Core fields: `id`, `discipline`, `category`, `title`, `description`, `price`, `price_type`, `location`, `status`, `listing_tier`, `seller_id`, `created_at`, `expires_at`, `extended_at`, `sold_at`, `view_count`, `report_count`
- `[ ]` **B:** Write migration `V3__create_listing_images_table.sql`
  - `id`, `listing_id` (FK), `url`, `display_order`, `created_at`
- `[ ]` **B:** Write migration `V4__create_vehicle_details_table.sql`
  - `id`, `listing_id` (FK, unique), `make`, `model`, `year`, `engine`, `gearbox`, `spec_class`, `cage`, `cage_spec`, `history`, `logbook`
- `[ ]` **B:** Write migration `V5__create_kart_details_table.sql`
  - `id`, `listing_id` (FK, unique), `chassis_make`, `chassis_model`, `engine_class`, `year`
- `[ ]` **B:** Write migration `V6__create_watchlist_table.sql`
  - `id`, `user_id` (FK), `listing_id` (FK), `created_at`
- `[ ]` **B:** Write migration `V7__create_reports_table.sql`
  - `id`, `listing_id` (FK), `reporter_id` (FK → users), `reason` (ENUM), `notes`, `resolved`, `created_at`
- `[ ]` **B:** Write migration `V8__create_payments_table.sql`
  - `id`, `listing_id` (FK), `stripe_payment_intent_id`, `amount`, `currency`, `status`, `tier`, `created_at`
- `[ ]` **B:** Write migration `V9__add_search_vector_to_listings.sql`
  - Add `search_vector` tsvector column to listings
  - Add GIN index on `search_vector`
  - Add trigger to auto-update `search_vector` on insert/update from `title`, `description`, `make`, `model`, `spec_class`

### Auth0 Integration

- `[ ]` **I:** Create Auth0 tenant for UK (`paddock-uk`)
- `[ ]` **I:** Configure Auth0 application (Single Page App for React frontend)
- `[ ]` **I:** Configure Auth0 API (for Spring Boot to validate JWTs)
- `[ ]` **I:** Enable email verification in Auth0 tenant settings
- `[ ]` **B:** Add `spring-security-oauth2-resource-server` dependency
- `[ ]` **B:** Configure Spring Security to validate Auth0 JWTs (`jwk-set-uri`, `issuer-uri`)
- `[ ]` **B:** Create `SecurityConfig.java` — public routes vs authenticated routes
- `[ ]` **B:** Create `UserSyncFilter` or Auth0 post-login Action — on first login, create a `User` record in Postgres from Auth0 profile (email, auth0_id, display_name)
- `[ ]` **F:** Install `@auth0/auth0-react` SDK
- `[ ]` **F:** Wrap app in `Auth0Provider` with correct domain + clientId
- `[ ]` **F:** Create `useAuth` hook (wraps Auth0 SDK, exposes `user`, `isAuthenticated`, `loginWithRedirect`, `logout`, `getAccessTokenSilently`)
- `[ ]` **F:** Create `AuthGuard` component — redirects to login if not authenticated
- `[ ]` **F:** Confirm access token is attached to all API requests via Axios interceptor or fetch wrapper

### Cloudflare R2 Setup

- `[ ]` **I:** Create Cloudflare account (if not exists)
- `[ ]` **I:** Create R2 bucket `paddock-uk-images`
- `[ ]` **I:** Generate R2 API token (read + write)
- `[ ]` **I:** Configure public access or CDN URL for image delivery
- `[ ]` **B:** Add AWS SDK v2 S3 client dependency (R2 is S3-compatible)
- `[ ]` **B:** Create `R2StorageService.java` — `generatePresignedUploadUrl(fileName, contentType)` and `deleteObject(key)` methods
- `[ ]` **B:** Create `POST /api/images/presign` endpoint — accepts filename + content type, returns pre-signed upload URL and final CDN URL
- `[ ]` **F:** Create `useImageUpload` hook — calls `/api/images/presign`, then PUTs file directly to R2 pre-signed URL, returns final CDN URL

### Resend Setup

- `[ ]` **I:** Create Resend account
- `[ ]` **I:** Add and verify sending domain (e.g. `mail.paddock.co.uk`)
- `[ ]` **I:** Store Resend API key in environment variables
- `[ ]` **B:** Add Resend Java SDK dependency (or use `RestTemplate` / `WebClient` against Resend HTTP API)
- `[ ]` **B:** Create `EmailService.java` with a generic `sendEmail(to, subject, htmlBody)` method
- `[ ]` **B:** Smoke test — send a test email on app startup (dev profile only)

---

## Phase 2 — Listings (Core)

> By the end of this phase: a user can register, post a listing, and another user can view it. The product exists.

### Listing Creation — Backend

- `[ ]` **B:** Create `Listing` JPA entity + repository
- `[ ]` **B:** Create `VehicleDetail` JPA entity + repository
- `[ ]` **B:** Create `KartDetail` JPA entity + repository
- `[ ]` **B:** Create `ListingImage` JPA entity + repository
- `[ ]` **B:** Create `ListingService.java` with `createDraftListing(userId, createListingRequest)` method
  - Validates discipline + category combination
  - Creates listing in `DRAFT` status
  - Attaches vehicle or kart details based on discipline
  - Attaches image URLs
- `[ ]` **B:** Create `POST /api/listings` endpoint — authenticated, creates draft listing, returns listing ID
- `[ ]` **B:** Create `GET /api/listings/{id}` endpoint — public, returns full listing detail
  - Increments `view_count` (async, non-blocking)
  - Returns phone number only if caller is authenticated
- `[ ]` **B:** Create `GET /api/listings` endpoint — public, paginated browse
  - Query params: `discipline`, `category`, `page`, `size`, `sort` (`newest`, `price_asc`, `price_desc`)
  - Only returns `ACTIVE` listings
- `[ ]` **B:** Create `GET /api/disciplines/{discipline}/categories` endpoint — returns valid categories for a given discipline
- `[ ]` **B:** Create `PATCH /api/listings/{id}/status` endpoint — authenticated (owner only), allows `SOLD` transition
- `[ ]` **B:** Write unit tests for `ListingService` — discipline/category validation, status transitions

### Listing Creation — Frontend

- `[ ]` **F:** Create discipline taxonomy config file — maps each discipline to its valid categories and required fields
- `[ ]` **F:** Create `/sell` route — protected by `AuthGuard`
- `[ ]` **F:** Build multi-step listing form (shadcn `Stepper` or tab-based):
  - Step 1: Choose discipline (5 discipline cards)
  - Step 2: Choose category (discipline-scoped)
  - Step 3: Core details (title, description, price, location)
  - Step 4: Discipline-specific fields (vehicle details or kart details — rendered conditionally)
  - Step 5: Image upload (up to 10 images, drag-and-drop, reorderable)
  - Step 6: Review & pay
- `[ ]` **F:** Image upload component — drag-and-drop, preview, reorder, delete, calls `useImageUpload` hook
- `[ ]` **F:** Form validation (React Hook Form + Zod) — all required fields, price must be positive integer, at least 1 image required
- `[ ]` **F:** On form completion, `POST /api/listings` to create draft, then redirect to Stripe checkout (Phase 3)

### Listing Browse — Frontend

- `[ ]` **F:** Create marketplace homepage `/marketplace` — 5 discipline panels (large, visual cards)
- `[ ]` **F:** Create discipline browse page `/marketplace/[discipline]` — category list + listings grid
- `[ ]` **F:** Create category browse page `/marketplace/[discipline]/[category]` — listing cards + filter sidebar
- `[ ]` **F:** Build `ListingCard` component — thumbnail, title, price, location, discipline badge, featured badge, age
- `[ ]` **F:** Build filter sidebar — price range, location, listed within; discipline-specific filters rendered conditionally
- `[ ]` **F:** Build pagination component
- `[ ]` **F:** Create listing detail page `/marketplace/[discipline]/[category]/[slug]-[id]`
  - Image gallery (lightbox on click)
  - Full details panel (all vehicle/kart fields)
  - Seller info panel (display name, member since, phone if logged in)
  - "Send a Message" button
  - "Save to Watchlist" button (logged-in only)
  - Report listing link

### Enquiry (Send a Message)

- `[ ]` **B:** Create `POST /api/listings/{id}/enquire` endpoint — authenticated
  - Accepts `{ message: string }`
  - Sends email to seller via `EmailService` with buyer's display name, email, message, and listing link
- `[ ]` **F:** Build enquiry modal — textarea + submit, shown on "Send a Message" click
- `[ ]` **F:** Success/error toast on enquiry send

---

## Phase 3 — Payments

> Listings become real. Nothing goes live without payment.

### Stripe Integration — Backend

- `[ ]` **I:** Create Stripe account, get API keys (test + live)
- `[ ]` **I:** Store `STRIPE_SECRET_KEY` and `STRIPE_WEBHOOK_SECRET` in environment variables
- `[ ]` **B:** Add Stripe Java SDK dependency
- `[ ]` **B:** Create `StripeService.java`
  - `createCheckoutSession(listingId, tier, userId)` — creates Stripe Checkout session with correct price, success URL, cancel URL, metadata (`listingId`, `tier`)
  - Returns session URL
- `[ ]` **B:** Create `POST /api/payments/checkout` endpoint — authenticated
  - Accepts `{ listingId, tier }` (`STANDARD` | `FEATURED`)
  - Calls `StripeService.createCheckoutSession`
  - Returns `{ checkoutUrl }`
- `[ ]` **B:** Create `POST /api/webhooks/stripe` endpoint — public (Stripe-signed)
  - Verify webhook signature
  - Handle `checkout.session.completed` event:
    - Create `Payment` record
    - Trigger auto-moderation check on listing (see Phase 6)
    - If passes → set listing status to `ACTIVE`, set `expires_at` to now + 90 days
    - If `FEATURED` → set `listing_tier` to `FEATURED`
    - Send "listing goes live" email to seller
  - Handle `payment_intent.payment_failed` event — log failure
- `[ ]` **B:** Write integration test for webhook handler — mock Stripe event payloads

### Stripe Integration — Frontend

- `[ ]` **F:** On listing form completion (Step 6 review), call `POST /api/payments/checkout` to get Stripe checkout URL
- `[ ]` **F:** Redirect to Stripe Checkout (external)
- `[ ]` **F:** Create `/sell/success` page — shown after successful payment, links to new listing
- `[ ]` **F:** Create `/sell/cancelled` page — shown if user cancels checkout, CTA to return to listing form

---

## Phase 4 — Listing Management (Seller Dashboard)

> Sellers can manage their listings. The expiry lifecycle is fully operational.

### Seller Dashboard — Backend

- `[ ]` **B:** Create `GET /api/users/me/listings` endpoint — authenticated, returns seller's own listings (all statuses, paginated)
- `[ ]` **B:** Create `PUT /api/listings/{id}` endpoint — authenticated (owner only)
  - Allows editing `title`, `description`, `price`, `location`, `images`, vehicle/kart details
  - Only allowed when status is `ACTIVE`
- `[ ]` **B:** Create `POST /api/listings/{id}/extend` endpoint — authenticated (owner only)
  - Validates: listing is `ACTIVE`, `extended_at` is null (not already extended), listing expires within 30 days
  - Sets `extended_at` = now, `expires_at` = `expires_at` + 30 days
  - Sends "listing extended" confirmation email
- `[ ]` **B:** Create `PATCH /api/listings/{id}/status` endpoint (extend to support `SOLD`)
  - Sets `sold_at` = now, status = `SOLD`
  - Sends "listing sold" confirmation email

### Expiry Scheduler — Backend

- `[ ]` **B:** Create `ListingExpiryScheduler.java` using Spring `@Scheduled`
  - Runs daily at midnight
  - Finds all `ACTIVE` listings where `expires_at` is in 10, 5, 2, or 1 day(s) — send reminder emails
  - Finds all `ACTIVE` listings where `expires_at` < now — set status to `EXPIRED`, send expiry email
- `[ ]` **B:** Create email templates (HTML via Resend) for:
  - Expiry reminder (parametrised — works for 10, 5, 2, 1 day variants)
  - Listing expired + re-list CTA
  - Listing extended confirmation
  - Listing marked as sold confirmation
- `[ ]` **B:** Write unit tests for scheduler logic — mock clock, test boundary conditions

### Seller Dashboard — Frontend

- `[ ]` **F:** Create `/dashboard` route — protected by `AuthGuard`
- `[ ]` **F:** Build dashboard layout — tabs or sidebar: My Listings, Watchlist, Account
- `[ ]` **F:** Build "My Listings" tab
  - Table/card list of all listings with status badges
  - Actions per listing: View, Edit, Mark as Sold, Extend (shown only when eligible), Re-list (shown when expired)
  - Expiry countdown shown for active listings (e.g. "Expires in 12 days")
- `[ ]` **F:** Build listing edit form — pre-populated from existing listing data, same field structure as create form
- `[ ]` **F:** Build account tab — display name, phone number, email (read-only from Auth0)

---

## Phase 5 — Search & Discovery

> Users can find things. The platform becomes useful at scale.

### Search — Backend

- `[ ]` **B:** Confirm `search_vector` tsvector trigger is working correctly (from Phase 1 migration)
- `[ ]` **B:** Create `GET /api/search` endpoint — public
  - Query params: `q` (search term), `page`, `size`
  - Uses `to_tsquery` / `plainto_tsquery` against `search_vector`
  - Returns results across all disciplines, ordered by relevance (`ts_rank`)
  - Each result includes discipline label for grouping in UI
- `[ ]` **B:** Update `GET /api/listings` (browse endpoint) to support all discipline-specific filter params:
  - `minPrice`, `maxPrice`
  - `location` (partial match on county/region)
  - `listedWithin` (7 | 30 | all)
  - `make`, `yearMin`, `yearMax`, `specClass`, `cage`, `logbook` (vehicle disciplines)
  - `chassisMake`, `engineClass` (kart)
  - `transportType` (transport discipline)
  - `condition` (parts)
- `[ ]` **B:** Add database indexes for commonly filtered columns: `discipline`, `category`, `status`, `expires_at`, `price`, `make`

### Search — Frontend

- `[ ]` **F:** Build global search bar component — persistent in site header
  - Debounced input (300ms)
  - Calls `GET /api/search?q=...`
  - Shows results in dropdown (typeahead) — grouped by discipline, max 5 per group
  - "See all results for X" link at bottom
- `[ ]` **F:** Create `/search` results page — full results page for a search query
  - Results grouped by discipline with counts
  - Uses `ListingCard` component
  - Pagination
- `[ ]` **F:** Wire up all discipline-specific filter sidebar fields from Phase 2 to the actual API filter params
- `[ ]` **F:** Persist active filters in URL query params (so search results are shareable/bookmarkable)

### Watchlist — Backend

- `[ ]` **B:** Create `POST /api/watchlist/{listingId}` — authenticated, adds listing to watchlist
- `[ ]` **B:** Create `DELETE /api/watchlist/{listingId}` — authenticated, removes from watchlist
- `[ ]` **B:** Create `GET /api/users/me/watchlist` — authenticated, returns paginated watchlist listings
- `[ ]` **B:** Ensure watchlist entries are deleted when a listing is removed or expired

### Watchlist — Frontend

- `[ ]` **F:** Wire up "Save to Watchlist" button on listing detail page — toggle saved/unsaved, optimistic update
- `[ ]` **F:** Build "Watchlist" tab in `/dashboard` — listing cards with "Remove" action
- `[ ]` **F:** Show saved count badge on watchlist nav item

---

## Phase 6 — Moderation & Admin

> The platform is safe. You have tools to manage it.

### Auto-Moderation — Backend

- `[ ]` **B:** Create `ModerationService.java`
  - `moderateListing(listing)` — runs checks, returns `ModerationResult` (PASS | FLAG)
  - Keyword filter: check `title` + `description` against a configurable blocklist (stored in config or DB table)
  - Image check: basic file type + size validation (NSFW API integration optional at MVP)
- `[ ]` **B:** Add `PENDING_REVIEW` to listing status enum
- `[ ]` **B:** Integrate `ModerationService` into Stripe webhook handler:
  - After payment confirmed → run moderation
  - PASS → status = `ACTIVE`
  - FLAG → status = `PENDING_REVIEW`, notify admin via email
- `[ ]` **B:** Create `moderation_blocklist` DB table + migration — `id`, `term`, `created_at`

### Reporting — Backend

- `[ ]` **B:** Create `Report` JPA entity + repository
- `[ ]` **B:** Create `POST /api/listings/{id}/report` — authenticated
  - Accepts `{ reason, notes }`
  - Creates `Report` record
  - If listing `report_count` reaches 3 → automatically set status to `PENDING_REVIEW`, notify admin
- `[ ]` **B:** Increment `report_count` on listing when report is created

### Admin Panel — Backend

- `[ ]` **B:** Create `ADMIN` role check — all `/api/admin/**` routes require `ADMIN` role
- `[ ]` **B:** Create `GET /api/admin/listings` — paginated, filterable by `status`, `discipline`
- `[ ]` **B:** Create `GET /api/admin/listings/{id}` — full listing detail including reports
- `[ ]` **B:** Create `DELETE /api/admin/listings/{id}` — sets status to `REMOVED`, sends notification email to seller
- `[ ]` **B:** Create `POST /api/admin/listings/{id}/approve` — sets `PENDING_REVIEW` listing to `ACTIVE`
- `[ ]` **B:** Create `GET /api/admin/reports` — paginated list of unresolved reports
- `[ ]` **B:** Create `PATCH /api/admin/reports/{id}/resolve` — marks report as resolved
- `[ ]` **B:** Create `GET /api/admin/users` — paginated, searchable by email/name
- `[ ]` **B:** Create `POST /api/admin/users/{id}/ban` — sets `banned = true`, suspends all active listings, sends notification email
- `[ ]` **B:** Create `DELETE /api/admin/users/{id}` — deletes user account + anonymises their listings

### Admin Panel — Frontend

- `[ ]` **F:** Create `/admin` route — protected, requires ADMIN role
- `[ ]` **F:** Build admin layout — sidebar nav: Listings, Reports, Users
- `[ ]` **F:** Build listings table — columns: title, discipline, status, seller, created, reports count
  - Filters: status, discipline
  - Actions: View, Approve, Remove
- `[ ]` **F:** Build reports queue — list of flagged listings with report reasons
  - Actions: View listing, Remove listing, Dismiss report
- `[ ]` **F:** Build users table — columns: name, email, joined, listing count, banned status
  - Search by name/email
  - Actions: Ban, Delete account
- `[ ]` **F:** Confirmation dialogs on all destructive admin actions (shadcn `AlertDialog`)

---

## Phase 7 — Polish & SEO

> The platform is production-ready. It ranks on Google. It looks right on a phone.

### SEO — Frontend

- `[ ]` **F:** Implement `<title>` and `<meta name="description">` per page using React Helmet or framework head management
  - Listing page: `{title} — {make} {model} {year} | Paddock`
  - Browse page: `{Discipline} {Category} for sale in the UK | Paddock`
  - Homepage: `Paddock — UK Motorsport Classifieds`
- `[ ]` **F:** Implement Open Graph tags on listing pages (`og:title`, `og:description`, `og:image`)
- `[ ]` **F:** Confirm listing pages are server-rendered or statically generated (SSR/SSG) for crawlability
- `[ ]` **F:** Implement canonical URLs on all listing and browse pages
- `[ ]` **B:** Create `GET /sitemap.xml` endpoint — generates sitemap of all `ACTIVE` listing URLs + discipline/category index pages
- `[ ]` **F:** Add `robots.txt`

### Performance & Images

- `[ ]` **I:** Configure Cloudflare Transform rules — auto-resize listing images to max 1200px wide, compress to WebP
- `[ ]` **I:** Configure Cloudflare caching headers for R2 image URLs
- `[ ]` **F:** Implement lazy loading on listing card images (`loading="lazy"`)
- `[ ]` **F:** Use correct `srcset` / `sizes` for responsive images
- `[ ]` **F:** Audit and fix Lighthouse performance score — target 90+ on mobile

### Mobile Responsiveness

- `[ ]` **F:** Full responsive audit — test all pages at 375px, 768px, 1280px
- `[ ]` **F:** Ensure filter sidebar collapses to a bottom sheet or drawer on mobile (shadcn `Sheet`)
- `[ ]` **F:** Ensure listing form is usable on mobile — full-width steps, large touch targets
- `[ ]` **F:** Ensure admin panel is functional on tablet (doesn't need to be perfect on mobile)

### Error Handling & Edge Cases

- `[ ]` **F:** 404 page — listing not found (removed, expired, or invalid ID)
- `[ ]` **F:** Empty state components — no listings in a category, empty watchlist, empty search results
- `[ ]` **F:** Global error boundary component
- `[ ]` **B:** Global exception handler (`@ControllerAdvice`) — consistent JSON error responses
- `[ ]` **B:** Rate limiting on public endpoints (search, listing browse) — Spring `bucket4j` or similar
- `[ ]` **F:** Loading skeletons on listing cards and listing detail page

### Pre-Launch Checklist

- `[ ]` **I:** Rotate all API keys from test → live (Stripe, Auth0, Resend, R2)
- `[ ]` **I:** Configure production environment variables
- `[ ]` **I:** Set up domain DNS for UK site
- `[ ]` **I:** Configure Auth0 production callback URLs
- `[ ]` **I:** Set up Stripe webhook endpoint for production URL
- `[ ]` **I:** Configure Cloudflare WAF / DDoS protection on domain
- `[ ]` **B:** Enable Spring Boot Actuator health endpoint (`/actuator/health`) — for uptime monitoring
- `[ ]` **I:** Set up uptime monitoring (UptimeRobot or Better Uptime — both have free tiers)
- `[ ]` **B:** Confirm all database migrations run cleanly on production Postgres
- `[ ]` **F:** Smoke test full listing flow end-to-end on production: register → post → pay → view → enquire → mark sold

---

## Backlog (Post-MVP)

These are captured here so they don't get lost, but are out of scope for launch.

- `[ ]` Saved searches with email alerts
- `[ ]` In-app real-time messaging (WebSockets / SSE)
- `[ ]` Trader / dealer profiles
- `[ ]` Social login (Google, Apple) via Auth0
- `[ ]` Native iOS / Android apps
- `[ ]` Advanced admin analytics dashboard
- `[ ]` Motorsport UK licence verification badge
- `[ ]` Listing promotion upsells (homepage slots)
- `[ ]` Seller reputation / review system
- `[ ]` Irish market deployment (paddock.ie) — infrastructure task once UK is stable
- `[ ]` Irish Auth0 tenant, R2 bucket, Postgres instance provisioning

---

*Task list is a living document. Update statuses as work progresses. Reference paddock-marketplace-prd.md for full context on any item.*
