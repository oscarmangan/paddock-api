ALTER TABLE paddock.track
    ADD COLUMN locale    VARCHAR(100),
    ADD COLUMN latitude  DECIMAL(9, 6),
    ADD COLUMN longitude DECIMAL(9, 6);

UPDATE paddock.track SET locale = 'Tŷ Croes, Anglesey',             latitude = 53.190000,  longitude = -4.497778 WHERE id = 'TR_1';
UPDATE paddock.track SET locale = 'Thurleigh, Bedfordshire',         latitude = 52.233611,  longitude = -0.472222 WHERE id = 'TR_2';
UPDATE paddock.track SET locale = 'Fawkham, Kent',                   latitude = 51.356667,  longitude =  0.262500 WHERE id = 'TR_3';
UPDATE paddock.track SET locale = 'Louth, Lincolnshire',             latitude = 53.308889,  longitude = -0.063056 WHERE id = 'TR_4';
UPDATE paddock.track SET locale = 'Castle Donington, Leicestershire', latitude = 52.829167, longitude = -1.375000 WHERE id = 'TR_5';
UPDATE paddock.track SET locale = 'Dunfermline, Fife',               latitude = 56.128889,  longitude = -3.503611 WHERE id = 'TR_6';
UPDATE paddock.track SET locale = 'Little Budworth, Cheshire',       latitude = 53.177500,  longitude = -2.614444 WHERE id = 'TR_7';
UPDATE paddock.track SET locale = 'Silverstone, Buckinghamshire',    latitude = 52.071111,  longitude = -1.016111 WHERE id = 'TR_8';
UPDATE paddock.track SET locale = 'Snetterton, Norfolk',             latitude = 52.466389,  longitude =  0.948333 WHERE id = 'TR_9';
UPDATE paddock.track SET locale = 'Andover, Hampshire',              latitude = 51.210278,  longitude = -1.600556 WHERE id = 'TR_10';
UPDATE paddock.track SET locale = 'Dalton-on-Tees, North Yorkshire', latitude = 54.455833,  longitude = -1.562778 WHERE id = 'TR_11';