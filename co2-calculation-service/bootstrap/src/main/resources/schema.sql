CREATE TABLE IF NOT EXISTS resource_profiles (
    id BIGSERIAL PRIMARY KEY,
    reference_set_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    fuel_per_use NUMERIC(19, 8) NOT NULL,
    fuel_type VARCHAR(255) NOT NULL,
    fuel_unit VARCHAR(255) NOT NULL,
    time_unit VARCHAR(255) NOT NULL,
    CONSTRAINT uk_resource_profiles_reference_set_name UNIQUE (reference_set_id, name)
);

CREATE TABLE IF NOT EXISTS emission_factors (
    id BIGSERIAL PRIMARY KEY,
    reference_set_id VARCHAR(255) NOT NULL,
    fuel_type VARCHAR(255) NOT NULL,
    unit VARCHAR(255) NOT NULL,
    factor NUMERIC(19, 8) NOT NULL,
    CONSTRAINT uk_emission_factors_reference_set_fuel_type UNIQUE (reference_set_id, fuel_type)
);

CREATE OR REPLACE PROCEDURE seed_popescu_demo_reference_catalog()
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO resource_profiles (
        reference_set_id,
        name,
        type,
        fuel_per_use,
        fuel_type,
        fuel_unit,
        time_unit
    )
    VALUES (
        'popescu-demo-v1',
        'Welder',
        'atomic',
        5.0,
        'Diesel',
        'l',
        'hour'
    )
    ON CONFLICT (reference_set_id, name) DO NOTHING;

    INSERT INTO emission_factors (
        reference_set_id,
        fuel_type,
        unit,
        factor
    )
    VALUES (
        'popescu-demo-v1',
        'Diesel',
        'l',
        2.70553
    )
    ON CONFLICT (reference_set_id, fuel_type) DO NOTHING;
END;
$$;

CALL seed_popescu_demo_reference_catalog();
