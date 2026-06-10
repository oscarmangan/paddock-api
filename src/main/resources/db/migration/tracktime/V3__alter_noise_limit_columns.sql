-- Changing noise limit column values from integer to decimal/numerics
ALTER TABLE track_day
    ALTER COLUMN noise_limit_static_db TYPE NUMERIC(5,1),
    ALTER COLUMN noise_limit_driveby_db TYPE NUMERIC(5,1);