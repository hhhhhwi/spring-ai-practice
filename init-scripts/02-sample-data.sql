-- hwiai Sample Data: Products and Sample Reviews
-- This script provides initial test data for MVP
-- File naming: 02-sample-data.sql (runs after 01-data.sql)

-- ===========================================
-- Sample Products (Running Shoes)
-- ===========================================

INSERT INTO product (name, description, category, image_url, created_date, updated_date)
VALUES 
  (
    'Nike Air Zoom Pegasus 40',
    '나이키의 대표적인 러닝화로 편안한 쿠셔닝과 반응성을 제공합니다. 일상적인 러닝과 트레이닝에 적합합니다.',
    'running_shoes',
    'https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,q_auto:eco/7c88aedc-a17a-4d69-bda0-f24e5c3b457a/pegasus-40-road-running-shoes.png',
    NOW(),
    NOW()
  ),
  (
    'Adidas Ultraboost 23',
    '부스트 폼 쿠셔닝으로 에너지 리턴이 뛰어난 프리미엄 러닝화입니다. 장거리 러닝에 최적화되어 있습니다.',
    'running_shoes',
    'https://assets.adidas.com/images/h_840,f_auto,q_auto,fl_lossy,c_fill,g_auto/adidas-ultraboost-23.jpg',
    NOW(),
    NOW()
  ),
  (
    'New Balance Fresh Foam 1080v13',
    '부드러운 Fresh Foam 미드솔이 특징인 편안한 러닝화입니다. 넓은 폭 옵션이 있어 발이 넓은 러너에게 적합합니다.',
    'running_shoes',
    'https://nb.scene7.com/is/image/NB/m1080n13_nb_02_i?$pdpflexf2$',
    NOW(),
    NOW()
  ),
  (
    'Asics Gel-Nimbus 25',
    '아식스의 대표적인 쿠셔닝 러닝화로 GEL 기술이 적용되어 충격 흡수가 뛰어납니다.',
    'running_shoes',
    'https://images.asics.com/is/image/asics/1011B547_020_SR_RT_GLB?$zoom$',
    NOW(),
    NOW()
  ),
  (
    'Hoka Clifton 9',
    '가벼우면서도 뛰어난 쿠셔닝을 제공하는 호카의 베스트셀러입니다. 초보 러너부터 베테랑까지 폭넓게 사랑받습니다.',
    'running_shoes',
    'https://www.hoka.com/on/demandware.static/-/Sites-masterCatalog_Hoka/default/dw123456/clifton-9.jpg',
    NOW(),
    NOW()
  )
ON CONFLICT DO NOTHING;

-- ===========================================
-- Sample Test User (for testing only)
-- Password: testuser123 (bcrypt hash)
-- ===========================================

INSERT INTO users (email, password, username, created_date, updated_date)
VALUES 
  (
    'test@hwiai.com',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GwtJ1yFhyZqy',
    '테스터',
    NOW(),
    NOW()
  )
ON CONFLICT (email) DO NOTHING;

-- ===========================================
-- Sample Reviews
-- Note: user_id will be set based on the test user created above
-- These reviews are meant to be analyzed by AI later
-- ===========================================

-- Get test user ID
DO $$
DECLARE
  test_user_id BIGINT;
  product_id_1 BIGINT;
  product_id_2 BIGINT;
  product_id_3 BIGINT;
BEGIN
  -- Get test user ID
  SELECT id INTO test_user_id FROM users WHERE email = 'test@hwiai.com';
  
  -- Get product IDs
  SELECT id INTO product_id_1 FROM product WHERE name LIKE '%Pegasus%';
  SELECT id INTO product_id_2 FROM product WHERE name LIKE '%Ultraboost%';
  SELECT id INTO product_id_3 FROM product WHERE name LIKE '%Fresh Foam%';
  
  -- Insert sample reviews only if test user exists
  IF test_user_id IS NOT NULL THEN
    -- Nike Pegasus reviews
    INSERT INTO review (product_id, user_id, content, rating, created_date, updated_date)
    VALUES 
      (
        product_id_1,
        test_user_id,
        '페가수스 40은 정말 편안합니다. 쿠셔닝이 적당하고 가벼워서 10km 러닝에 딱 좋네요. 통기성도 괜찮아서 여름에도 발이 덥지 않았어요.',
        5,
        NOW(),
        NOW()
      ),
      (
        product_id_1,
        test_user_id,
        '사이즈가 약간 큰 편이에요. 평소 사이즈보다 0.5 작게 주문하는 게 좋을 것 같아요. 그 외에는 만족스럽습니다.',
        4,
        NOW(),
        NOW()
      ),
      (
        product_id_1,
        test_user_id,
        '내구성이 좋습니다. 6개월 동안 거의 매일 신었는데도 밑창이 튼튼하네요. 가성비가 뛰어난 러닝화입니다.',
        5,
        NOW(),
        NOW()
      );
    
    -- Adidas Ultraboost reviews
    INSERT INTO review (product_id, user_id, content, rating, created_date, updated_date)
    VALUES 
      (
        product_id_2,
        test_user_id,
        '부스트 쿠셔닝이 정말 부드럽고 반발력이 좋습니다. 하지만 무게가 좀 있어서 스피드 러닝보다는 장거리에 적합한 것 같아요.',
        4,
        NOW(),
        NOW()
      ),
      (
        product_id_2,
        test_user_id,
        '디자인이 예쁘고 편안하지만 가격이 비싼 편입니다. 세일 기간에 구매하는 것을 추천합니다.',
        4,
        NOW(),
        NOW()
      );
    
    -- New Balance Fresh Foam reviews
    INSERT INTO review (product_id, user_id, content, rating, created_date, updated_date)
    VALUES 
      (
        product_id_3,
        test_user_id,
        '발이 넓은 편인데 2E 폭으로 구매했더니 딱 맞네요. 쿠셔닝이 부드러워서 장시간 신어도 피로하지 않습니다.',
        5,
        NOW(),
        NOW()
      ),
      (
        product_id_3,
        test_user_id,
        '통기성이 좀 아쉽습니다. 여름에는 발이 좀 답답한 느낌이에요. 가을이나 겨울에 신기 좋을 것 같습니다.',
        3,
        NOW(),
        NOW()
      );
    
    RAISE NOTICE '========================================';
    RAISE NOTICE 'Sample Data Loaded Successfully';
    RAISE NOTICE '========================================';
    RAISE NOTICE 'Products: 5';
    RAISE NOTICE 'Test User: test@hwiai.com (password: testuser123)';
    RAISE NOTICE 'Sample Reviews: 8';
    RAISE NOTICE '========================================';
    RAISE NOTICE 'Next Step: Run AI analysis batch job';
    RAISE NOTICE 'POST /admin/batch/analyze';
    RAISE NOTICE '========================================';
  ELSE
    RAISE NOTICE 'Test user not found, skipping sample reviews';
  END IF;
END $$;
