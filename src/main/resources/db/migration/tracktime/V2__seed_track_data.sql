-- Tracks
INSERT INTO paddock.track (id, name, region) VALUES
('TR_1',  'Anglesey Circuit',         'wales'),
('TR_2',  'Bedford Autodrome',        'south'),
('TR_3',  'Brands Hatch',             'south'),
('TR_4',  'Cadwell Park',             'north'),
('TR_5',  'Donington Park',           'midlands'),
('TR_6',  'Knockhill Racing Circuit', 'scotland'),
('TR_7',  'Oulton Park',              'midlands'),
('TR_8',  'Silverstone Circuit',      'midlands'),
('TR_9',  'Snetterton Circuit',       'south'),
('TR_10', 'Thruxton Circuit',         'south'),
('TR_11', 'Croft Circuit',            'north')
ON CONFLICT DO NOTHING;

-- Track Layouts
INSERT INTO paddock.track_layout (id, track_id, name) VALUES
('TL_1',  'TR_1',  'Coastal Circuit'),
('TL_2',  'TR_1',  'International GP Circuit'),
('TL_3',  'TR_2',  'GT Circuit'),
('TL_4',  'TR_2',  'South Circuit'),
('TL_5',  'TR_2',  'South West Circuit'),
('TL_6',  'TR_2',  'West Circuit'),
('TL_7',  'TR_3',  'Grand Prix'),
('TL_8',  'TR_3',  'Indy'),
('TL_9',  'TR_4',  'Full Circuit'),
('TL_10', 'TR_5',  'Grand Prix'),
('TL_11', 'TR_5',  'National'),
('TL_12', 'TR_6',  'International Circuit'),
('TL_13', 'TR_7',  'Fosters Circuit'),
('TL_14', 'TR_7',  'International Circuit'),
('TL_15', 'TR_7',  'Island Circuit'),
('TL_16', 'TR_7',  'Rally Stage'),
('TL_17', 'TR_8',  'Grand Prix Circuit'),
('TL_18', 'TR_8',  'International Circuit'),
('TL_19', 'TR_8',  'National Circuit'),
('TL_20', 'TR_9',  '100'),
('TL_21', 'TR_9',  '200'),
('TL_22', 'TR_9',  '300'),
('TL_23', 'TR_10', 'Full Circuit'),
('TL_24', 'TR_11', 'Full Circuit'),
('TL_25', 'TR_11', 'Club Circuit A'),
('TL_26', 'TR_11', 'Club Circuit B')
ON CONFLICT DO NOTHING;