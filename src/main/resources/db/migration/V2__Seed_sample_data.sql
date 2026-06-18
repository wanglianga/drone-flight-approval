INSERT INTO pilots (license_number, name, id_card, phone, email, license_issue_date, license_expiry_date, level, status)
VALUES
    ('P2023001', '张三', '110101199001011234', '13800138001', 'zhangsan@example.com', '2023-01-15', '2028-01-15', 'A', 'ACTIVE'),
    ('P2023002', '李四', '110101199203021235', '13800138002', 'lisi@example.com', '2023-03-20', '2028-03-20', 'B', 'ACTIVE'),
    ('P2022003', '王五', '110101198805031236', '13800138003', 'wangwu@example.com', '2022-06-10', '2027-06-10', 'C', 'ACTIVE')
ON CONFLICT (license_number) DO NOTHING;

INSERT INTO drones (serial_number, model, manufacturer, max_takeoff_weight, max_flight_altitude, max_flight_time, battery_type, battery_count, status)
VALUES
    ('DRONE-SN-001', 'Matrice 300 RTK', 'DJI', 6.3, 7000, 55, 'TB60', 4, 'AVAILABLE'),
    ('DRONE-SN-002', 'Mavic 3 Enterprise', 'DJI', 0.92, 6000, 45, 'Intelligent Battery', 3, 'AVAILABLE'),
    ('DRONE-SN-003', 'Matrice 350 RTK', 'DJI', 6.5, 7000, 58, 'TB65', 4, 'AVAILABLE')
ON CONFLICT (serial_number) DO NOTHING;

INSERT INTO insurance (drone_id, policy_number, insurance_company, coverage_amount, effective_date, expiry_date, status)
VALUES
    (1, 'INS-2024-001', '平安保险', 500000.0, '2024-01-01 00:00:00', '2025-12-31 23:59:59', 'VALID'),
    (2, 'INS-2024-002', '太平洋保险', 300000.0, '2024-01-01 00:00:00', '2025-12-31 23:59:59', 'VALID'),
    (3, 'INS-2024-003', '中国人保', 500000.0, '2024-01-01 00:00:00', '2025-12-31 23:59:59', 'VALID')
ON CONFLICT (policy_number) DO NOTHING;

INSERT INTO airspaces (code, name, region, center_latitude, center_longitude, radius_meters, min_altitude_meters, max_altitude_meters, type, restriction)
VALUES
    ('AS-BJ-001', '首都机场周边空域', '北京顺义', 40.0799, 116.6031, 10000, 0, 500, 'CONTROLLED', 'PROHIBITED'),
    ('AS-BJ-002', '天安门管制空域', '北京东城', 39.9055, 116.3976, 3000, 0, 1000, 'PROHIBITED', 'PROHIBITED'),
    ('AS-BJ-003', '通州通用空域', '北京通州', 39.9025, 116.6586, 5000, 0, 300, 'UNCONTROLLED', 'REQUIRE_APPROVAL'),
    ('AS-SH-001', '虹桥机场周边空域', '上海闵行', 31.1944, 121.3363, 8000, 0, 500, 'CONTROLLED', 'PROHIBITED')
ON CONFLICT (code) DO NOTHING;

INSERT INTO no_fly_zones (name, type, center_latitude, center_longitude, radius_meters, min_altitude_meters, max_altitude_meters, effective_from, effective_to, status, reason)
VALUES
    ('北京市政府办公区', 'GOVERNMENT', 39.9048, 116.4074, 1000, 0, 500, NULL, NULL, 'ACTIVE', '政府机关核心区域'),
    ('军事管理区A', 'MILITARY', 40.0500, 116.3000, 2000, 0, 1000, NULL, NULL, 'ACTIVE', '军事管理区'),
    ('首都机场净空区', 'AIRPORT', 40.0799, 116.6031, 5000, 0, 300, NULL, NULL, 'ACTIVE', '机场净空保护区'),
    ('临时活动禁飞区', 'EVENT', 39.9500, 116.4500, 1500, 0, 200, '2024-06-01 00:00:00', '2024-06-30 23:59:59', 'ACTIVE', '大型活动临时管控')
ON CONFLICT DO NOTHING;
