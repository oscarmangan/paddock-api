-- ============================================================
-- RACE
-- ============================================================

-- Top-level categories
INSERT INTO paddock.category (discipline, parent_id, name, label, slug, display_order) VALUES
('race', null, 'CARS',                  'Cars',                  'cars',                  1),
('race', null, 'PARTS_COMPONENTS',      'Parts & Components',    'parts-components',      2),
('race', null, 'TOOLS_EQUIPMENT',       'Tools & Equipment',     'tools-equipment',       3),
('race', null, 'RENTALS_DRIVES',        'Rentals / Drives',      'rentals-drives',        4);

-- Race → Cars subcategories
INSERT INTO paddock.category (discipline, parent_id, name, label, slug, display_order)
SELECT 'race', id, sub.name, sub.label, sub.slug, sub.display_order
FROM paddock.category, (VALUES
    ('FORMULA_SINGLE_SEATER', 'Formula / Single Seater', 'formula-single-seater', 1),
    ('SALOON_TOURING',        'Saloon / Touring',        'saloon-touring',        2),
    ('GT_SPORTS_CAR',         'GT / Sports Car',         'gt-sports-car',         3),
    ('PROTOTYPE',             'Prototype',               'prototype',             4),
    ('STOCK_CAR',             'Stock Car',               'stock-car',             5),
    ('CLASSIC_VINTAGE',       'Classic / Vintage',       'classic-vintage',       6)
) AS sub(name, label, slug, display_order)
WHERE paddock.category.discipline = 'race' AND paddock.category.name = 'CARS' AND paddock.category.parent_id IS NULL;

-- Race → Parts & Components subcategories
INSERT INTO paddock.category (discipline, parent_id, name, label, slug, display_order)
SELECT 'race', id, sub.name, sub.label, sub.slug, sub.display_order
FROM paddock.category, (VALUES
    ('ENGINES',           'Engines',            'engines',            1),
    ('TRANSMISSIONS',     'Transmissions',      'transmissions',      2),
    ('SUSPENSION_BRAKES', 'Suspension & Brakes','suspension-brakes',  3),
    ('WHEELS_TYRES',      'Wheels & Tyres',     'wheels-tyres',       4),
    ('BODYWORK_AERO',     'Bodywork & Aero',    'bodywork-aero',      5),
    ('COCKPIT',           'Cockpit',            'cockpit',            6),
    ('ELECTRONICS',       'Electronics',        'electronics',        7),
    ('OTHER',             'Other',              'other',              8)
) AS sub(name, label, slug, display_order)
WHERE paddock.category.discipline = 'race' AND paddock.category.name = 'PARTS_COMPONENTS' AND paddock.category.parent_id IS NULL;


-- ============================================================
-- RALLY
-- ============================================================

INSERT INTO paddock.category (discipline, parent_id, name, label, slug, display_order) VALUES
('rally', null, 'CARS',                         'Cars',                          'cars',                          1),
('rally', null, 'PARTS_COMPONENTS',             'Parts & Components',            'parts-components',              2),
('rally', null, 'TOOLS_EQUIPMENT',              'Tools & Equipment',             'tools-equipment',               3),
('rally', null, 'RENTALS_DRIVES',               'Rentals / Drives',              'rentals-drives',                4),
('rally', null, 'CO_DRIVER_AVAILABLE',          'Co-Driver / Navigator Available','co-driver-available',          5),
('rally', null, 'CO_DRIVER_WANTED',             'Co-Driver / Navigator Wanted',  'co-driver-wanted',              6),
('rally', null, 'PACENOTES_NAV_EQUIPMENT',      'Pacenotes & Navigation Equipment','pacenotes-nav-equipment',     7);

-- Rally → Cars subcategories
INSERT INTO paddock.category (discipline, parent_id, name, label, slug, display_order)
SELECT 'rally', id, sub.name, sub.label, sub.slug, sub.display_order
FROM paddock.category, (VALUES
    ('RC1_RC3',   'RC1 → RC3', 'rc1-rc3', 1),
    ('RC4_RC6',   'RC4 → RC6', 'rc4-rc6', 2),
    ('WRC',       'WRC',       'wrc',      3),
    ('GROUP_A',   'Group A',   'group-a',  4),
    ('GROUP_N',   'Group N',   'group-n',  5),
    ('MODIFIED',  'Modified',  'modified', 6),
    ('HISTORIC',  'Historic',  'historic', 7)
) AS sub(name, label, slug, display_order)
WHERE paddock.category.discipline = 'rally' AND paddock.category.name = 'CARS' AND paddock.category.parent_id IS NULL;

-- Rally → Parts & Components subcategories
INSERT INTO paddock.category (discipline, parent_id, name, label, slug, display_order)
SELECT 'rally', id, sub.name, sub.label, sub.slug, sub.display_order
FROM paddock.category, (VALUES
    ('ENGINES',           'Engines',            'engines',            1),
    ('TRANSMISSIONS',     'Transmissions',      'transmissions',      2),
    ('SUSPENSION_BRAKES', 'Suspension & Brakes','suspension-brakes',  3),
    ('WHEELS_TYRES',      'Wheels & Tyres',     'wheels-tyres',       4),
    ('BODYWORK_AERO',     'Bodywork & Aero',    'bodywork-aero',      5),
    ('COCKPIT',           'Cockpit',            'cockpit',            6),
    ('ELECTRONICS',       'Electronics',        'electronics',        7),
    ('OTHER',             'Other',              'other',              8)
) AS sub(name, label, slug, display_order)
WHERE paddock.category.discipline = 'rally' AND paddock.category.name = 'PARTS_COMPONENTS' AND paddock.category.parent_id IS NULL;


-- ============================================================
-- DRIFT
-- ============================================================

INSERT INTO paddock.category (discipline, parent_id, name, label, slug, display_order) VALUES
('drift', null, 'CARS',             'Cars',               'cars',             1),
('drift', null, 'PARTS_COMPONENTS', 'Parts & Components', 'parts-components', 2),
('drift', null, 'TOOLS_EQUIPMENT',  'Tools & Equipment',  'tools-equipment',  3);

-- Drift → Cars subcategories
INSERT INTO paddock.category (discipline, parent_id, name, label, slug, display_order)
SELECT 'drift', id, sub.name, sub.label, sub.slug, sub.display_order
FROM paddock.category, (VALUES
    ('COMPETITION',         'Competition',          'competition',          1),
    ('STREET_DUAL_PURPOSE', 'Street / Dual Purpose','street-dual-purpose',  2),
    ('PROJECT',             'Project',              'project',              3)
) AS sub(name, label, slug, display_order)
WHERE paddock.category.discipline = 'drift' AND paddock.category.name = 'CARS' AND paddock.category.parent_id IS NULL;

-- Drift → Parts & Components subcategories
INSERT INTO paddock.category (discipline, parent_id, name, label, slug, display_order)
SELECT 'drift', id, sub.name, sub.label, sub.slug, sub.display_order
FROM paddock.category, (VALUES
    ('ENGINES',           'Engines',            'engines',            1),
    ('TRANSMISSIONS',     'Transmissions',      'transmissions',      2),
    ('SUSPENSION_BRAKES', 'Suspension & Brakes','suspension-brakes',  3),
    ('WHEELS_TYRES',      'Wheels & Tyres',     'wheels-tyres',       4),
    ('BODYWORK_AERO',     'Bodywork & Aero',    'bodywork-aero',      5),
    ('COCKPIT',           'Cockpit',            'cockpit',            6),
    ('ELECTRONICS',       'Electronics',        'electronics',        7),
    ('OTHER',             'Other',              'other',              8)
) AS sub(name, label, slug, display_order)
WHERE paddock.category.discipline = 'drift' AND paddock.category.name = 'PARTS_COMPONENTS' AND paddock.category.parent_id IS NULL;


-- ============================================================
-- KART
-- ============================================================

INSERT INTO paddock.category (discipline, parent_id, name, label, slug, display_order) VALUES
('kart', null, 'COMPLETE_KARTS',    'Complete Karts',     'complete-karts',   1),
('kart', null, 'ENGINES',           'Engines',            'engines',          2),
('kart', null, 'CHASSIS',           'Chassis',            'chassis',          3),
('kart', null, 'PARTS_COMPONENTS',  'Parts & Components', 'parts-components', 4),
('kart', null, 'TOOLS_EQUIPMENT',   'Tools & Equipment',  'tools-equipment',  5),
('kart', null, 'RENTALS_DRIVES',    'Rentals / Drives',   'rentals-drives',   6),
('kart', null, 'WANTED',            'Wanted',             'wanted',           7);

-- Kart → Complete Karts subcategories
INSERT INTO paddock.category (discipline, parent_id, name, label, slug, display_order)
SELECT 'kart', id, sub.name, sub.label, sub.slug, sub.display_order
FROM paddock.category, (VALUES
    ('BAMBINO',     'Bambino',      'bambino',      1),
    ('CADET',       'Cadet',        'cadet',        2),
    ('JUNIOR_MINI', 'Junior / Mini','junior-mini',  3),
    ('SENIOR',      'Senior',       'senior',       4),
    ('SHIFTER',     'Shifter',      'shifter',      5),
    ('SUPERKART',   'Superkart',    'superkart',    6)
) AS sub(name, label, slug, display_order)
WHERE paddock.category.discipline = 'kart' AND paddock.category.name = 'COMPLETE_KARTS' AND paddock.category.parent_id IS NULL;


-- ============================================================
-- TRANSPORT
-- ============================================================

INSERT INTO paddock.category (discipline, parent_id, name, label, slug, display_order) VALUES
('transport', null, 'VANS_TRUCKS',      'Vans & Trucks',      'vans-trucks',      1),
('transport', null, 'TRAILERS',         'Trailers',           'trailers',         2),
('transport', null, 'AWNINGS_SHELTER',  'Awnings / Shelter',  'awnings-shelter',  3),
('transport', null, 'GENERATORS_POWER', 'Generators / Power', 'generators-power', 4),
('transport', null, 'TOOLS_EQUIPMENT',  'Tools & Equipment',  'tools-equipment',  5),
('transport', null, 'WANTED',           'Wanted',             'wanted',           6);

-- Transport → Vans & Trucks subcategories
INSERT INTO paddock.category (discipline, parent_id, name, label, slug, display_order)
SELECT 'transport', id, sub.name, sub.label, sub.slug, sub.display_order
FROM paddock.category, (VALUES
    ('RACE_VANS',           'Race Vans',          'race-vans',          1),
    ('CAMPERS_MOTORHOMES',  'Campers / Motorhomes','campers-motorhomes', 2),
    ('TRUCKS_HGVS',         'Trucks / HGVs',      'trucks-hgvs',        3)
) AS sub(name, label, slug, display_order)
WHERE paddock.category.discipline = 'transport' AND paddock.category.name = 'VANS_TRUCKS' AND paddock.category.parent_id IS NULL;

-- Transport → Trailers subcategories
INSERT INTO paddock.category (discipline, parent_id, name, label, slug, display_order)
SELECT 'transport', id, sub.name, sub.label, sub.slug, sub.display_order
FROM paddock.category, (VALUES
    ('SHUTTLE_ENCLOSED',  'Shuttle / Enclosed', 'shuttle-enclosed', 1),
    ('CAR_TRANSPORTER',   'Car Transporter',    'car-transporter',  2),
    ('OPEN',              'Open',               'open',             3),
    ('BOX_SMALL',         'Box / Small',        'box-small',        4)
) AS sub(name, label, slug, display_order)
WHERE paddock.category.discipline = 'transport' AND paddock.category.name = 'TRAILERS' AND paddock.category.parent_id IS NULL;


-- ============================================================
-- RACEWEAR
-- ============================================================

INSERT INTO paddock.category (discipline, parent_id, name, label, slug, display_order) VALUES
('racewear', null, 'HELMETS',      'Helmets',    'helmets',    1),
('racewear', null, 'SUITS',        'Suits',      'suits',      2),
('racewear', null, 'GLOVES',       'Gloves',     'gloves',     3),
('racewear', null, 'BOOTS',        'Boots',      'boots',      4),
('racewear', null, 'PROTECTION',   'Protection', 'protection', 5),
('racewear', null, 'UNDERWEAR',    'Underwear',  'underwear',  6),
('racewear', null, 'WANTED',       'Wanted',     'wanted',     7);

-- Racewear → Helmets subcategories
INSERT INTO paddock.category (discipline, parent_id, name, label, slug, display_order)
SELECT 'racewear', id, sub.name, sub.label, sub.slug, sub.display_order
FROM paddock.category, (VALUES
    ('OPEN_FACE',   'Open Face',   'open-face',   1),
    ('CLOSED_FACE', 'Closed Face', 'closed-face', 2)
) AS sub(name, label, slug, display_order)
WHERE paddock.category.discipline = 'racewear' AND paddock.category.name = 'HELMETS' AND paddock.category.parent_id IS NULL;

-- Racewear → Suits subcategories
INSERT INTO paddock.category (discipline, parent_id, name, label, slug, display_order)
SELECT 'racewear', id, sub.name, sub.label, sub.slug, sub.display_order
FROM paddock.category, (VALUES
    ('KARTING',      'Karting',      'karting',      1),
    ('MULTI_LAYER',  'Multi-layer',  'multi-layer',  2)
) AS sub(name, label, slug, display_order)
WHERE paddock.category.discipline = 'racewear' AND paddock.category.name = 'SUITS' AND paddock.category.parent_id IS NULL;

-- Racewear → Protection subcategories
INSERT INTO paddock.category (discipline, parent_id, name, label, slug, display_order)
SELECT 'racewear', id, sub.name, sub.label, sub.slug, sub.display_order
FROM paddock.category, (VALUES
    ('HANS',          'HANS',          'hans',          1),
    ('RIB_PROTECTORS','Rib Protectors','rib-protectors', 2)
) AS sub(name, label, slug, display_order)
WHERE paddock.category.discipline = 'racewear' AND paddock.category.name = 'PROTECTION' AND paddock.category.parent_id IS NULL;
