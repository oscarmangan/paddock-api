# Paddock Marketplace — Product Requirements Document

**Project:** Paddock Motorsport Marketplace  
**Primary Market:** United Kingdom (paddock.co.uk — TBC)  
**Secondary Market:** Ireland (paddock.ie) — clone, post-UK launch  
**Module:** Classifieds / Marketplace  
**Author:** Solo Developer  
**Status:** Pre-development — MVP  
**Last Updated:** June 2026

---

## 1. Overview

Paddock is a motorsport-first classifieds platform for the UK (and later Irish) market. It fills a clear gap: existing solutions (DoneDeal, AutoTrader, eBay Motors) are general-purpose and force motorsport users to repeatedly filter by discipline on every search. Paddock is discipline-native — users self-select their world (Rally, Race, Drift, Kart, Transport) and everything they see is scoped to it.

The Marketplace is the primary and initial module of the wider Paddock platform. A motorsport events calendar has already been built separately (React + shadcn/Tailwind + Spring Boot).

---

## 2. Goals

- Build the UK's leading dedicated motorsport classifieds platform
- Deliver a fast, clean, mobile-responsive buying and selling experience
- Keep the MVP lean: one developer, low infrastructure costs, real users
- Architect for multi-tenancy from day one to support the Irish clone (paddock.ie) without a painful migration
- Generate initial revenue via low-friction listing fees

---

## 3. Non-Goals (MVP)

- Native iOS / Android apps (web-only at launch, apps planned post-launch)
- Trader / dealer profiles and branding pages
- In-app real-time messaging system
- Irish language (Gaeilge) localisation
- Make-an-offer / negotiation flows
- Full CMS admin panel
- Saved searches / email alerts on new listings (watchlist only for MVP)

---

## 4. Markets & Multi-Tenancy

The platform must support multiple regional instances sharing a single codebase and database, distinguished by a `market` / `region` field on relevant data models.

| Market | Domain | Status |
|--------|--------|--------|
| United Kingdom | TBC (paddock.co.uk or similar) | Primary — build first |
| Ireland | paddock.ie | Clone — post-UK launch |

All listings, users, and configuration must be scoped to a market. A UK user's listings do not appear on the Irish site and vice versa.

---

## 5. Disciplines (Top-Level Navigation)

On the marketplace homepage, users are presented with **5 discipline panels**. Selecting a discipline scopes all subsequent browsing, filtering, and listing creation to that world.

| Discipline | Description |
|------------|-------------|
| **Race** | Circuit racing — saloons, single-seaters, GTs, etc. |
| **Rally** | Stage and forest rally — cars, co-driver listings, navigation equipment |
| **Drift** | Drift cars, builds, and components |
| **Kart** | Karting — chassis, engines, kart-specific gear |
| **Transport** | Race trailers, race vans, trucks, and support vehicles |

### Discipline Context
Once a user selects a discipline, that context persists throughout their session. Filters, categories, and listing fields are all scoped to the selected discipline. Users can switch discipline at any time via the top navigation.

---

## 6. Listing Categories

Each discipline shares a **base set of listing types**, with discipline-specific extensions where relevant.

### Base Categories (all disciplines where applicable)
- Vehicles / Cars / Chassis
- Parts & Components
- Safety Equipment (helmets, suits, harnesses, HANS)
- Tools & Garage Equipment
- Merchandise & Clothing

### Discipline-Specific Extensions

| Discipline | Additional Categories |
|------------|----------------------|
| Rally | Co-driver / Navigator wanted, Pacenotes equipment, Intercom systems |
| Kart | Kart chassis (replaces "Car"), Kart engines, Transponders |
| Transport | Trailers, Race vans, Trucks & HGVs, Awnings & pit equipment |
| Drift | Drift builds (replaces standard car listing), Aero & fabrication |

> **Note:** Kart section uses "Chassis" in place of "Car" throughout the UI.

---

## 7. Listing Data Model

### Core Listing Fields (all listing types)

| Field | Type | Notes |
|-------|------|-------|
| `id` | UUID | Primary key |
| `market` | Enum | `UK`, `IE` |
| `discipline` | Enum | `RACE`, `RALLY`, `DRIFT`, `KART`, `TRANSPORT` |
| `category` | Enum | Discipline-scoped category |
| `title` | String | Seller-written |
| `description` | Text | Full listing description |
| `price` | Integer | In pence (GBP) or cent (EUR) |
| `price_type` | Enum | `FIXED`, `POA` (Price on Application) |
| `location` | String | Town / county — stored on listing, not pulled from profile |
| `images` | Array<String> | URLs to Cloudflare R2 |
| `status` | Enum | `PENDING`, `ACTIVE`, `EXPIRED`, `SOLD`, `REMOVED` |
| `listing_tier` | Enum | `STANDARD`, `FEATURED` |
| `seller_id` | UUID | FK → User |
| `created_at` | Timestamp | |
| `expires_at` | Timestamp | 90 days from `created_at` |
| `extended_at` | Timestamp | Nullable — set if seller extends |
| `sold_at` | Timestamp | Nullable |
| `view_count` | Integer | Incremented on each unique view |
| `report_count` | Integer | Incremented on each user report |

### Vehicle-Specific Fields (Race, Rally, Drift)

| Field | Type | Notes |
|-------|------|-------|
| `make` | String | Ford, Subaru, BMW, etc. |
| `model` | String | Fiesta, Impreza, E30, etc. |
| `year` | Integer | Year of manufacture |
| `engine` | String | e.g. "2.0T Duratec" |
| `gearbox` | String | e.g. "Sadev 6-speed sequential" |
| `spec_class` | String | e.g. "R5", "Group N", "S14", "Formula Ford" |
| `cage` | Boolean | Roll cage fitted |
| `cage_spec` | String | Nullable — e.g. "FIA spec, bolted-in" |
| `history` | Text | Event/competition history |
| `logbook` | Boolean | Motorsport UK / Motorsport Ireland logbook present |

### Kart-Specific Fields

| Field | Type | Notes |
|-------|------|-------|
| `chassis_make` | String | Tony Kart, CRG, Birel, etc. |
| `chassis_model` | String | |
| `engine_class` | String | Rotax, X30, Rok, TKM, etc. |
| `year` | Integer | |

---

## 8. Search & Filtering

### Global Search
- Persistent search bar at the top of every page
- Full-text search using **PostgreSQL `tsvector`**
- Searches across: `title`, `description`, `make`, `model`, `spec_class`
- Returns results **across all disciplines** — a search for "Ford" returns Ford listings from Race, Rally, Drift, etc.
- Results are grouped or labelled by discipline
- Scoped to current `market` (UK or IE)

### Discipline-Level Filters
Filters are contextual to the active discipline and category. Shown as a sidebar or filter drawer on listing browse pages.

**Universal filters (all disciplines):**
- Price range (min / max)
- Location (county / region)
- Listed within (7 days / 30 days / all)

**Vehicle listing filters (Race, Rally, Drift):**
- Make
- Year range
- Spec / Class
- Cage fitted (yes / no)
- Logbook present (yes / no)

**Kart filters:**
- Chassis make
- Engine class
- Year range

**Parts filters:**
- Compatible make/model (free text)
- Condition (new / used)

**Transport filters:**
- Type (trailer / van / truck)

---

## 9. Listing Lifecycle

```
DRAFT → PENDING (awaiting payment) → ACTIVE → EXPIRED / SOLD / REMOVED
                                          ↑
                              (30-day extension available)
```

| State | Description |
|-------|-------------|
| `PENDING` | Payment completed, listing awaiting auto-moderation check |
| `ACTIVE` | Live and publicly visible |
| `EXPIRED` | 90 days elapsed without renewal or sale |
| `SOLD` | Manually marked as sold by seller |
| `REMOVED` | Removed by admin |

### Expiry & Reminders
- Listings expire after **90 days**
- Email reminders sent at: **10 days, 5 days, 2 days, 1 day** before expiry
- Seller can extend by **30 days** once before the listing expires (no re-payment required for standard; TBD for featured)
- After expiry, sellers can re-list (new payment required)

---

## 10. Pricing & Payments

| Tier | Price | Description |
|------|-------|-------------|
| Standard | £0.99 | Listed in standard order, expires in 90 days |
| Featured | £9.99 | Pinned to top of category/discipline, badge shown, priority in search results |

- Payments processed via **Stripe**
- Listing is created in `PENDING` state; moves to `ACTIVE` after successful payment and passing auto-moderation
- Featured listings are visually distinct (badge, elevated position)
- Currency is GBP for UK market, EUR for Irish market

---

## 11. Moderation

### Auto-Moderation (on submission)
- Basic image scan (nudity / NSFW detection)
- Keyword filter on title and description (spam, prohibited items)
- If flagged → listing held in `PENDING_REVIEW` state and admin is notified
- If clean → listing moves to `ACTIVE` immediately

### Reporting
- Any logged-in user can report a listing
- Report reasons: `SPAM`, `INCORRECT_CATEGORY`, `PROHIBITED_ITEM`, `SUSPECTED_FRAUD`, `OTHER`
- Reports visible in admin dashboard
- Listings with 3+ reports are automatically flagged for admin review

### Admin Actions
- View all active, pending, flagged, and removed listings
- Remove a listing (moves to `REMOVED` state)
- Ban a user (account suspended, listings removed)
- Delete a user account
- View and resolve flagged reports

---

## 12. User Accounts & Profiles

### Registration
- Email + password registration
- Email verification required before posting a listing
- Auth handled via **Auth0** (free tier)

### User Profile
| Field | Visibility |
|-------|-----------|
| Display name | Public |
| Email address | Hidden — used for system emails only |
| Phone number | Logged-in users only |
| Member since | Public |
| Active listings | Public |
| Location | Not on profile — stored per listing |

### Watchlist
- Logged-in users can save listings to a watchlist
- Watchlist accessible from user dashboard
- No email alerts on watchlisted items (v1) — just a saved list

---

## 13. Buyer–Seller Communication

- **Phone number** displayed on listing to logged-in users only
- **"Send a Message"** button on listing page — opens a form that sends an email to the seller via Resend
  - Buyer's email is included in the forwarded message so seller can reply directly
  - No in-app messaging thread; email is the thread
- Seller email address is never exposed directly in the UI

---

## 14. Tech Stack

| Layer | Technology | Notes |
|-------|-----------|-------|
| Frontend | React + shadcn/ui + Tailwind CSS | Consistent with existing calendar module |
| Backend | Java 25 / Spring Boot 4 | REST API |
| Database | PostgreSQL | Hosted — e.g. Supabase Postgres or Railway |
| Auth | Auth0 (free tier) | Email verification, password reset, JWT |
| Image Storage | Cloudflare R2 | Zero egress fees, S3-compatible API |
| Image Processing | Cloudflare Images / Transform | Resize + compress on delivery |
| Payments | Stripe | Checkout, webhooks for payment confirmation |
| Email | Resend | Transactional email — verification, reminders, enquiries |
| Search | PostgreSQL `tsvector` | Full-text search, no external search service needed at MVP |
| Hosting (Frontend) | Vercel or Cloudflare Pages | TBD |
| Hosting (Backend) | Railway or Fly.io | TBD |
| Repo Structure | Separate repos | `paddock-web` (React), `paddock-api` (Spring Boot) |

---

## 15. Multi-Tenancy Architecture Notes

- Every `User`, `Listing`, and config record carries a `market` field (`UK` | `IE`)
- API requests are market-scoped — the frontend sends a market header or subdomain context
- Pricing, currency, and locale are driven by market config
- Both markets share the same codebase and database; Irish launch requires no schema migration, only data and DNS

---

## 16. Image Handling

- Sellers can upload up to **10 images per listing**
- Images are uploaded directly to Cloudflare R2 via a pre-signed URL (no proxying through the API)
- On delivery, images are resized and compressed via Cloudflare Transform rules
- Minimum image size enforced on upload (e.g. 800×600px)
- First image is the listing thumbnail

---

## 17. Admin Panel (MVP)

A simple, password-protected internal dashboard (separate route, admin role only).

**Features:**
- Listings table — filter by status, discipline, market
- View full listing detail
- Remove listing
- View flagged/reported listings
- Resolve reports
- Users table — search by email/name
- Ban user
- Delete user account

No CMS, no category management UI, no analytics dashboard in v1.

---

## 18. Email Notifications (via Resend)

| Trigger | Recipient | Notes |
|---------|-----------|-------|
| Registration | User | Email verification link |
| Listing goes live | Seller | Confirmation with listing link |
| Listing expiry — 10 days | Seller | Reminder + extend CTA |
| Listing expiry — 5 days | Seller | Reminder + extend CTA |
| Listing expiry — 2 days | Seller | Reminder + extend CTA |
| Listing expiry — 1 day | Seller | Final reminder |
| Listing expired | Seller | Re-list CTA |
| Listing extended | Seller | Confirmation |
| Listing marked as sold | Seller | Confirmation |
| Buyer enquiry | Seller | Forwarded message with buyer email |
| Listing removed by admin | Seller | Notification with reason |
| Account banned | User | Notification |

---

## 19. SEO Considerations

- Listing pages must be server-rendered or statically generated for SEO (Next.js SSR/SSG or equivalent)
- URL structure: `/[discipline]/[category]/[listing-slug]-[id]`
  - e.g. `/rally/cars/subaru-impreza-wrc-s12-2004-a3f9`
- Listing title, make, model, discipline, and location in `<title>` and `<meta description>`
- Sitemap generated for all active listings
- Discipline and category index pages should be crawlable

---

## 20. Future Considerations (Post-MVP)

- Native iOS and Android apps
- Trader / dealer profiles with branding and bulk listing tools
- Saved searches with email alerts on new matching listings
- In-app real-time messaging (WebSockets)
- Stripe Connect for trader subscriptions
- Advanced analytics dashboard for admin
- Irish language (Gaeilge) support for paddock.ie
- Social login (Google, Apple) via Auth0
- Listing promotion upsells (homepage feature slots, social sharing boost)
- Review / reputation system for sellers
- Integration with Motorsport UK licence database for verified seller badges

---

## 21. Open Questions

| # | Question | Status |
|---|----------|--------|
| 1 | UK domain name — what is it? | TBD |
| 2 | Exact subdomain/domain strategy for multi-market (subdomain vs separate domain) | TBD |
| 3 | Is the 30-day extension free for Featured listings too, or does it require re-payment? | TBD |
| 4 | Complete category/sub-category taxonomy per discipline | To be mapped |
| 5 | Hosting provider for frontend and API | TBD |
| 6 | What constitutes a "view" for `view_count` — unique per session, per user, per IP? | TBD |

---

*This document is a living PRD. It will be updated as decisions are made and development progresses.*
