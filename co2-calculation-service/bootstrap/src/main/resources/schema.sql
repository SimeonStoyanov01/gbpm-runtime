CREATE TABLE IF NOT EXISTS resource_profiles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
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
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    reference_set_id VARCHAR(255) NOT NULL,
    fuel_type VARCHAR(255) NOT NULL,
    unit VARCHAR(255) NOT NULL,
    factor NUMERIC(19, 8) NOT NULL,
    CONSTRAINT uk_emission_factors_reference_set_fuel_type UNIQUE (reference_set_id, fuel_type)
);

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
VALUES
    ('popescu-demo-v1', 'Diesel', 'l', 2.70553),
    ('popescu-demo-v1', 'Natural gas', 'm3', 2.02135),
    ('popescu-demo-v1', 'Aviation turbine fuel', 'l', 2.54514),
    ('popescu-demo-v1', 'Bioethanol', 'l', 0.00901),
    ('popescu-demo-v1', 'Biodiesel ME', 'l', 0.16751),
    ('popescu-demo-v1', 'Wood pellets', 't', 72.61754),
    ('popescu-demo-v1', 'Grass/straw', 't', 49.23656),
    ('popescu-demo-v1', 'Biogas', 't', 1.21518),
    ('popescu-demo-v1', 'Energy_Romania', 'kwh', 0.2895),
    ('popescu-demo-v1', 'Energy_Netherlands', 'kwh', 0.2029)
ON CONFLICT (reference_set_id, fuel_type) DO NOTHING;
