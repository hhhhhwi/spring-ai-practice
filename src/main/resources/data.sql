
-- Product 테이블에 샘플 데이터 삽입
INSERT INTO product (name, created_date, updated_date) VALUES 
('NIKE Vomero 18', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Review 테이블에 샘플 데이터 삽입 (product_id는 1로 가정)
INSERT INTO review (product_id, text, is_aggregated, created_date, updated_date) VALUES 
(1, 'If you''re on the hunt for a good, fun, very bouncy easy run shoe, this ticks the boxes. It might look a bit dirty after a few runs, and it has a bit of extra weight to it, but the sheer amount of cushioning and support makes it feel like one of the springiest rides we''ve tried this year. It''s smooth, should last for a good couple hundred miles and generally makes us feel less susceptible to the aches and pains of running. Nike''s done it again, folks.', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'Having worn them ourselves now across 5K and 10K runs, as well as at the track, we can confirm that it doesn''t feel at all like a speed shoe. This is very much an easy, long-run shoe that has good support and cushioning, rather than a shoe would want to ramp up the pace in. The weight comes in at 11.5 ounces, which is significantly heavier than some of our other easy run shoes (the Asics Novablast, for example, come in at 9 ounces while the Saucony Triumph 22—a shoe we already found quite heavy—is 10 ounces). In general, though, this isn''t super noticeable over shorter distances, and the extra cushioning and support meant we felt less ruined after longer runs, making it a price worth paying.', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, '발에 가까운 1차 줌엑스폼이 살짝쿵 푹신하고 편하게 느껴지고 지면에 가까운 2차 리액트폼이 탄탄하게 지면을 튕겨줘. 슈블2가 푹신단단탱글하다가 탕밀어주는데. 보메로도 못지않게 제법 튕겨줘. 그래도 리액트니만큼 슈블만큼은 아니지만 80프로 정도로 튕겨준다. 조깅 말미에 기대이상의 반발력이 너무 궁금해서 3분대로 1k 달려봤는데. 확실히 슈블만큼은 못튕겨줘서 롤링이 힘들었다. 빨리 달리니 튕겨지고 발이 올라오는 과정에서 미드솔 무게가 느껴지더라고. 이게 슈블과의 차이점이더라구. 그래서 빠른 페이스의 템포런과는 맞지 않겠더라. 그래도 기대이상의 탄성이 있다는 걸 확인해서 만족했다.아웃솔 접지력과 내구성도 페가와 같은 와플창이라 매우 좋을듯하네. 장거리는 아니지만 10k를 뛰면서 쿠션이 죽거나 약해진것도 없어서 미드솔 내구성도 매우 좋아 장거리에 딱일것같아', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, '별로임', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- -- CharacteristicValue 테이블에 샘플 데이터 삽입
-- -- 사용자가 직접 입력한 점수 (phrase는 null)
-- INSERT INTO characteristic_value (review_id, is_related, value, phrase, created_date, updated_date) VALUES 
-- (1, true, 4, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- (4, true, 2, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);