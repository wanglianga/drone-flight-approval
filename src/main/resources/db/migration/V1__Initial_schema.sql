CREATE TABLE IF NOT EXISTS pilots (
    id BIGSERIAL PRIMARY KEY,
    license_number VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    id_card VARCHAR(20) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    license_issue_date DATE NOT NULL,
    license_expiry_date DATE NOT NULL,
    level VARCHAR(10) NOT NULL,
    qualification_file_url VARCHAR(500),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS drones (
    id BIGSERIAL PRIMARY KEY,
    serial_number VARCHAR(100) NOT NULL UNIQUE,
    model VARCHAR(100) NOT NULL,
    manufacturer VARCHAR(100) NOT NULL,
    max_takeoff_weight DOUBLE PRECISION NOT NULL,
    max_flight_altitude DOUBLE PRECISION NOT NULL,
    max_flight_time INTEGER NOT NULL,
    battery_type VARCHAR(50) NOT NULL,
    battery_count INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    registration_file_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS insurance (
    id BIGSERIAL PRIMARY KEY,
    drone_id BIGINT NOT NULL REFERENCES drones(id),
    policy_number VARCHAR(100) NOT NULL UNIQUE,
    insurance_company VARCHAR(100) NOT NULL,
    coverage_amount DOUBLE PRECISION NOT NULL,
    effective_date TIMESTAMP NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'VALID',
    policy_file_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS airspaces (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    region VARCHAR(200) NOT NULL,
    center_latitude DECIMAL(10, 6) NOT NULL,
    center_longitude DECIMAL(10, 6) NOT NULL,
    radius_meters DOUBLE PRECISION NOT NULL,
    min_altitude_meters DOUBLE PRECISION NOT NULL,
    max_altitude_meters DOUBLE PRECISION NOT NULL,
    type VARCHAR(50) NOT NULL,
    restriction VARCHAR(50) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS no_fly_zones (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    type VARCHAR(50) NOT NULL,
    center_latitude DECIMAL(10, 6) NOT NULL,
    center_longitude DECIMAL(10, 6) NOT NULL,
    radius_meters DOUBLE PRECISION NOT NULL,
    min_altitude_meters DOUBLE PRECISION NOT NULL,
    max_altitude_meters DOUBLE PRECISION NOT NULL,
    effective_from TIMESTAMP,
    effective_to TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS missions (
    id BIGSERIAL PRIMARY KEY,
    mission_number VARCHAR(50) NOT NULL UNIQUE,
    company_name VARCHAR(200) NOT NULL,
    project_name VARCHAR(200) NOT NULL,
    type VARCHAR(50) NOT NULL,
    pilot_id BIGINT NOT NULL REFERENCES pilots(id),
    drone_id BIGINT NOT NULL REFERENCES drones(id),
    planned_start_time TIMESTAMP NOT NULL,
    planned_end_time TIMESTAMP NOT NULL,
    planned_max_altitude DOUBLE PRECISION NOT NULL,
    planned_max_radius DOUBLE PRECISION NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    mission_description TEXT,
    weather_condition VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS flight_routes (
    id BIGSERIAL PRIMARY KEY,
    mission_id BIGINT NOT NULL REFERENCES missions(id),
    route_name VARCHAR(200) NOT NULL,
    total_distance_km DOUBLE PRECISION NOT NULL,
    estimated_duration_minutes INTEGER NOT NULL,
    max_altitude_meters DOUBLE PRECISION NOT NULL,
    cross_region_info TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS route_points (
    id BIGSERIAL PRIMARY KEY,
    route_id BIGINT NOT NULL REFERENCES flight_routes(id) ON DELETE CASCADE,
    sequence_number INTEGER NOT NULL,
    latitude DECIMAL(10, 6) NOT NULL,
    longitude DECIMAL(10, 6) NOT NULL,
    altitude_meters DOUBLE PRECISION NOT NULL,
    speed_mps DOUBLE PRECISION,
    hold_time_seconds INTEGER
);

CREATE TABLE IF NOT EXISTS approvals (
    id BIGSERIAL PRIMARY KEY,
    mission_id BIGINT NOT NULL REFERENCES missions(id),
    approver VARCHAR(100) NOT NULL,
    decision VARCHAR(30) NOT NULL,
    general_comment TEXT,
    approved_start_time TIMESTAMP,
    approved_end_time TIMESTAMP,
    approved_max_altitude DOUBLE PRECISION,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS conflict_segments (
    id BIGSERIAL PRIMARY KEY,
    approval_id BIGINT NOT NULL REFERENCES approvals(id) ON DELETE CASCADE,
    conflict_type VARCHAR(50) NOT NULL,
    start_point_sequence INTEGER,
    end_point_sequence INTEGER,
    start_latitude DECIMAL(10, 6),
    start_longitude DECIMAL(10, 6),
    end_latitude DECIMAL(10, 6),
    end_longitude DECIMAL(10, 6),
    conflict_altitude DOUBLE PRECISION,
    conflict_zone_name VARCHAR(200),
    conflict_zone_code VARCHAR(50),
    description TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS takeoff_reports (
    id BIGSERIAL PRIMARY KEY,
    mission_id BIGINT NOT NULL REFERENCES missions(id),
    pilot_name VARCHAR(100) NOT NULL,
    takeoff_latitude DECIMAL(10, 6) NOT NULL,
    takeoff_longitude DECIMAL(10, 6) NOT NULL,
    actual_takeoff_time TIMESTAMP NOT NULL,
    battery_level_percent DOUBLE PRECISION NOT NULL,
    weather_at_takeoff VARCHAR(200),
    number_of_batteries INTEGER NOT NULL,
    preflight_check_notes TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'REPORTED',
    reported_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(mission_id, reported_at)
);

CREATE TABLE IF NOT EXISTS flight_logs (
    id BIGSERIAL PRIMARY KEY,
    mission_id BIGINT NOT NULL REFERENCES missions(id),
    actual_start_time TIMESTAMP NOT NULL,
    actual_end_time TIMESTAMP NOT NULL,
    actual_duration_minutes INTEGER NOT NULL,
    actual_max_altitude_meters DOUBLE PRECISION NOT NULL,
    actual_distance_km DOUBLE PRECISION NOT NULL,
    battery_used_count INTEGER NOT NULL,
    anomaly_description TEXT,
    flight_status VARCHAR(30) NOT NULL,
    missing_log_reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS track_points (
    id BIGSERIAL PRIMARY KEY,
    flight_log_id BIGINT NOT NULL REFERENCES flight_logs(id) ON DELETE CASCADE,
    sequence_number INTEGER NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    latitude DECIMAL(10, 6) NOT NULL,
    longitude DECIMAL(10, 6) NOT NULL,
    altitude_meters DOUBLE PRECISION NOT NULL,
    speed_mps DOUBLE PRECISION,
    satellite_count INTEGER,
    battery_percent DOUBLE PRECISION,
    is_abnormal BOOLEAN DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_missions_pilot_id ON missions(pilot_id);
CREATE INDEX IF NOT EXISTS idx_missions_drone_id ON missions(drone_id);
CREATE INDEX IF NOT EXISTS idx_missions_status ON missions(status);
CREATE INDEX IF NOT EXISTS idx_flight_routes_mission_id ON flight_routes(mission_id);
CREATE INDEX IF NOT EXISTS idx_route_points_route_id ON route_points(route_id);
CREATE INDEX IF NOT EXISTS idx_approvals_mission_id ON approvals(mission_id);
CREATE INDEX IF NOT EXISTS idx_conflict_segments_approval_id ON conflict_segments(approval_id);
CREATE INDEX IF NOT EXISTS idx_takeoff_reports_mission_id ON takeoff_reports(mission_id);
CREATE INDEX IF NOT EXISTS idx_flight_logs_mission_id ON flight_logs(mission_id);
CREATE INDEX IF NOT EXISTS idx_track_points_flight_log_id ON track_points(flight_log_id);
CREATE INDEX IF NOT EXISTS idx_insurance_drone_id ON insurance(drone_id);
CREATE INDEX IF NOT EXISTS idx_no_fly_zones_status ON no_fly_zones(status);
CREATE INDEX IF NOT EXISTS idx_airspaces_restriction ON airspaces(restriction);
