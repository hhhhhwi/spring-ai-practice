-- hwiai Initial Data: Characteristics for Running Shoes
-- This script is executed automatically when PostgreSQL container starts for the first time
-- File naming: 01-data.sql (runs first)

-- Characteristics for Running Shoes (운동화)
-- SCORE type: 1-5 numeric rating
-- CHOICE type: predefined options

-- ===========================================
-- SCORE Type Characteristics
-- ===========================================

INSERT INTO characteristic (name, description, value_type, created_date, updated_date)
VALUES 
  ('쿠셔닝', '충격 흡수 능력 및 착용감의 부드러움 정도', 'SCORE', NOW(), NOW()),
  ('내구성', '신발의 오래 사용 가능 여부 및 마모 저항성', 'SCORE', NOW(), NOW()),
  ('통기성', '발의 시원함 및 땀 배출 능력', 'SCORE', NOW(), NOW()),
  ('지면 그립', '미끄럼 방지 및 접지력', 'SCORE', NOW(), NOW()),
  ('안정성', '발목 지지력 및 균형 유지 능력', 'SCORE', NOW(), NOW())
ON CONFLICT DO NOTHING;

-- ===========================================
-- CHOICE Type Characteristics
-- ===========================================

-- 무게감 (Weight Feel)
INSERT INTO characteristic (name, description, value_type, created_date, updated_date)
VALUES 
  ('무게감', '신발을 신었을 때 느껴지는 무게', 'CHOICE', NOW(), NOW())
ON CONFLICT DO NOTHING;

-- 핏 (Fit)
INSERT INTO characteristic (name, description, value_type, created_date, updated_date)
VALUES 
  ('핏', '신발 사이즈의 맞음 정도', 'CHOICE', NOW(), NOW())
ON CONFLICT DO NOTHING;

-- ===========================================
-- Characteristic Options (CHOICE type only)
-- ===========================================

-- 무게감 options
INSERT INTO characteristic_option (characteristic_id, option_value, created_date, updated_date)
SELECT 
  c.id,
  option_values.value,
  NOW(),
  NOW()
FROM 
  characteristic c,
  (VALUES ('가벼움'), ('보통'), ('무거움')) AS option_values(value)
WHERE 
  c.name = '무게감' AND c.value_type = 'CHOICE'
ON CONFLICT DO NOTHING;

-- 핏 options
INSERT INTO characteristic_option (characteristic_id, option_value, created_date, updated_date)
SELECT 
  c.id,
  option_values.value,
  NOW(),
  NOW()
FROM 
  characteristic c,
  (VALUES ('작음'), ('딱맞음'), ('큼')) AS option_values(value)
WHERE 
  c.name = '핏' AND c.value_type = 'CHOICE'
ON CONFLICT DO NOTHING;

-- ===========================================
-- Verification
-- ===========================================

-- Print summary
DO $$
DECLARE
  char_count INTEGER;
  option_count INTEGER;
BEGIN
  SELECT COUNT(*) INTO char_count FROM characteristic;
  SELECT COUNT(*) INTO option_count FROM characteristic_option;
  
  RAISE NOTICE '========================================';
  RAISE NOTICE 'Initial Data Loaded Successfully';
  RAISE NOTICE '========================================';
  RAISE NOTICE 'Characteristics: %', char_count;
  RAISE NOTICE 'Characteristic Options: %', option_count;
  RAISE NOTICE '========================================';
END $$;
