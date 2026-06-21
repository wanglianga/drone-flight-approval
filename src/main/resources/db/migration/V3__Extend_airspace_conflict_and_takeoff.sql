-- 扩展冲突段表：增加限制高度、可飞时间窗、绕行建议字段
ALTER TABLE conflict_segments
ADD COLUMN IF NOT EXISTS restricted_min_altitude DOUBLE PRECISION,
ADD COLUMN IF NOT EXISTS restricted_max_altitude DOUBLE PRECISION,
ADD COLUMN IF NOT EXISTS flyable_start_time TIMESTAMP,
ADD COLUMN IF NOT EXISTS flyable_end_time TIMESTAMP,
ADD COLUMN IF NOT EXISTS detour_suggestion TEXT;

-- 扩展任务表：增加报备窗口相关字段
ALTER TABLE missions
ADD COLUMN IF NOT EXISTS report_window_start TIMESTAMP,
ADD COLUMN IF NOT EXISTS report_window_end TIMESTAMP,
ADD COLUMN IF NOT EXISTS last_risk_calculated_at TIMESTAMP;

-- 扩展起飞报备表：增加取消原因字段
ALTER TABLE takeoff_reports
ADD COLUMN IF NOT EXISTS cancel_reason VARCHAR(200);

-- 学校周边禁飞区示例数据
INSERT INTO no_fly_zones (name, type, center_latitude, center_longitude, radius_meters, min_altitude_meters, max_altitude_meters, effective_from, effective_to, status, reason)
VALUES
    ('北京第一实验小学周边', 'SCHOOL', 39.8950, 116.4100, 800, 0, 120, NULL, NULL, 'ACTIVE', '学校周边低空管制，教学时段禁飞'),
    ('清华大学校园周边', 'SCHOOL', 40.0030, 116.3280, 1000, 0, 150, NULL, NULL, 'ACTIVE', '高校校园周边管制区')
ON CONFLICT DO NOTHING;

-- 临时管制区示例数据
INSERT INTO no_fly_zones (name, type, center_latitude, center_longitude, radius_meters, min_altitude_meters, max_altitude_meters, effective_from, effective_to, status, reason)
VALUES
    ('CBD临时管制区', 'TEMPORARY', 39.9150, 116.4600, 2000, 0, 300, '2024-01-01 00:00:00', '2026-12-31 23:59:59', 'ACTIVE', '商务中心区临时空中管制')
ON CONFLICT DO NOTHING;

-- 索引优化
CREATE INDEX IF NOT EXISTS idx_no_fly_zones_type ON no_fly_zones(type);
CREATE INDEX IF NOT EXISTS idx_missions_report_window ON missions(report_window_start, report_window_end);
CREATE INDEX IF NOT EXISTS idx_takeoff_reports_status ON takeoff_reports(status);
