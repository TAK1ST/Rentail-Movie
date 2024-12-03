USE movierentalsystemdb;

INSERT INTO Accounts (account_id, username, password, role, email, status, online_at, created_at, updated_at, creability)
VALUES
('AD000001', 'admin', '1', 'ADMIN', 'admin1@example.com', 'OFFLINE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 50),
('CU000002', 'staff_user1', 'securepass2', 'STAFF', 'staff1@example.com', 'OFFLINE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 20),
('CU000003', 'customer_user1', 'securepass3', 'CUSTOMER', 'customer1@example.com', 'ONLINE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 10),
('CU000004', 'customer_user2', 'securepass4', 'CUSTOMER', 'customer2@example.com', 'ONLINE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 15),
('CU000005', 'customer_user3', 'securepass5', 'CUSTOMER', 'customer3@example.com', 'OFFLINE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 8),
('ST000006', 'customer_user4', 'securepass6', 'CUSTOMER', 'customer4@example.com', 'ONLINE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 12),
('ST000007', 'customer_user5', 'securepass7', 'CUSTOMER', 'customer5@example.com', 'OFFLINE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5),
('PR000008', 'premium_user1', 'securepass8', 'PREMIUM', 'premium1@example.com', 'OFFLINE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 30),
('PR000009', 'premium_user2', 'securepass9', 'PREMIUM', 'premium2@example.com', 'ONLINE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 35),
('CU000010', 'premium_user3', 'securepass10', 'PREMIUM', 'premium3@example.com', 'ONLINE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 40);

INSERT INTO Movies (movie_id, title, description, avg_rating, release_year, rental_price, available_copies, created_at, updated_at)
VALUES
('MV000001', 'The Great Adventure', 'An epic journey through unknown lands.', 4.5, '2024-01-15', 5.99, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('MV000002', 'Mystery at Midnight', 'A thrilling mystery story with unexpected twists.', 4.2, '2024-03-10', 3.99, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('MV000003', 'Romantic Dream', 'A love story that defies the odds.', 4.8, '2024-02-14', 6.99, 15, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('MV000004', 'Sci-Fi Chronicles', 'Exploring the possibilities of the future.', 4.1, '2024-05-22', 7.99, 12, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('MV000005', 'Action Force', 'An action-packed thrill ride with intense combat scenes.', 4.6, '2024-06-17', 5.49, 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('MV000006', 'Fantasy World', 'A world full of magic, mystery, and danger.', 4.7, '2024-07-25', 6.99, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('MV000007', 'Comedic Moments', 'Laugh-out-loud moments that will keep you entertained.', 4.4, '2024-08-05', 4.99, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('MV000008', 'Documentary Truth', 'A documentary exposing the hidden truths of the world.', 4.3, '2024-09-12', 3.49, 12, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('MV000009', 'Superhero Rise', 'A story of a young hero discovering their powers.', 4.9, '2024-10-18', 7.49, 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('MV000010', 'Horror Night', 'A terrifying tale of survival in the wild.', 4.0, '2024-11-01', 5.99, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO Actors (actor_id, actor_name, actor_rank, actor_description)
VALUES
('AT000001', 'John Doe', 'A', 'A veteran actor known for his versatility and range.'),
('AT000002', 'Jane Smith', 'B', 'An up-and-coming actress with a bright future ahead.'),
('AT000003', 'Michael Johnson', 'C', 'An actor who brings humor and charm to every role.'),
('AT000004', 'Emily Davis', 'A', 'Award-winning actress with decades of experience.'),
('AT000005', 'James Wilson', 'B', 'A seasoned actor known for action roles.'),
('AT000006', 'Olivia Brown', 'C', 'A rising star in the industry, gaining popularity fast.'),
('AT000007', 'Robert Clark', 'A', 'Iconic actor with a career spanning multiple decades.'),
('AT000008', 'Sophia Lee', 'D', 'A talented but lesser-known actor in indie films.'),
('AT000009', 'David Harris', 'B', 'An actor specializing in dramatic roles.'),
('AT000010', 'Ava White', 'A', 'An award-winning actress known for her deep emotional performances.');

INSERT INTO Genres (genre_name, description)
VALUES
('Action', 'Fast-paced movies with lots of physical stunts and excitement.'),
('Comedy', 'Movies designed to make the audience laugh.'),
('Drama', 'Movies that focus on serious storytelling and emotional narratives.'),
('Fantasy', 'Stories involving magic, mythical creatures, and fantastical worlds.'),
('Horror', 'Movies designed to evoke fear and suspense.'),
('Sci-Fi', 'Movies that explore futuristic concepts, technology, and space travel.'),
('Romance', 'Movies focused on romantic relationships between characters.'),
('Documentary', 'Non-fiction films that explore real-world subjects.'),
('Thriller', 'Movies designed to keep the audience on the edge of their seat.'),
('Animation', 'Movies using animation techniques to tell their stories.');

INSERT INTO Languages (language_code, language_name)
VALUES
('EN', 'English'),
('ES', 'Spanish'),
('FR', 'French'),
('DE', 'German'),
('IT', 'Italian'),
('JP', 'Japanese'),
('KR', 'Korean'),
('CN', 'Chinese'),
('RU', 'Russian'),
('PT', 'Portuguese');

INSERT INTO Movie_Actor (movie_id, actor_id, role)
VALUES
('MV000001', 'AT000001', 'MAIN'),
('MV000001', 'AT000002', 'SUPPORT'),
('MV000001', 'AT000003', 'MAIN'),
('MV000002', 'AT000004', 'VILLAIN'),
('MV000003', 'AT000005', 'MAIN'),
('MV000003', 'AT000006', 'SUPPORT'),
('MV000003', 'AT000001', 'MAIN'),
('MV000003', 'AT000004', 'CAMEO'),
('MV000004', 'AT000009', 'MAIN'),
('MV000004', 'AT000010', 'SUPPORT');

INSERT INTO Movie_Genre (movie_id, genre_name)
VALUES
('MV000001', 'Action'),
('MV000001', 'Documentary'),
('MV000002', 'Thriller'),
('MV000002', 'Comedy'),
('MV000002', 'Romance'),
('MV000003', 'Drama'),
('MV000004', 'Sci-Fi'),
('MV000005', 'Action'),
('MV000008', 'Horror'),
('MV000010', 'Thriller');

INSERT INTO Movie_Language (movie_id, language_code)
VALUES
('MV000001', 'EN'),
('MV000001', 'JP'),
('MV000002', 'EN'),
('MV000002', 'DE'),
('MV000002', 'ES'),
('MV000003', 'FR'),
('MV000004', 'ES'),
('MV000008', 'DE'),
('MV000009', 'IT'),
('MV000010', 'JP');

INSERT INTO Discounts (discount_code, discount_type, discount_value, start_date, end_date, quantity, is_active)
VALUES
('FDAFDFDS', 'PERCENT', 10.00, '2024-12-01', '2024-12-15', 100, TRUE),
('IOE1123D', 'FIXED_AMOUNT', 5.00, '2024-11-20', '2024-12-10', 50, TRUE),
('YYJCM93F', 'BUY_X_GET_Y_FREE', 1.00, '2024-12-05', '2024-12-25', 20, TRUE),
('PJNKLL12', 'PERCENT', 15.00, '2024-11-15', '2024-12-20', 70, TRUE),
('GKJODNCD', 'FIXED_AMOUNT', 10.00, '2024-12-01', '2024-12-31', 30, TRUE),
('UTNVIDTC', 'PERCENT', 20.00, '2024-11-25', '2024-12-10', 80, TRUE),
('OINOIDN2', 'BUY_X_GET_Y_FREE', 2.00, '2024-12-10', '2024-12-30', 40, TRUE),
('DEGDCGDE', 'PERCENT', 25.00, '2024-12-01', '2024-12-20', 60, TRUE),
('OKOMCU19', 'FIXED_AMOUNT', 15.00, '2024-11-30', '2024-12-15', 10, TRUE),
('ODNC479W', 'PERCENT', 30.00, '2024-12-05', '2024-12-25', 90, TRUE);

INSERT INTO Discount_Account (customer_id, discount_code)
VALUES
('CU000002', 'FDAFDFDS'),
('CU000002', 'OINOIDN2'),
('CU000004', 'GKJODNCD'),
('CU000004', 'ODNC479W'),
('CU000004', 'YYJCM93F'),
('CU000003', 'YYJCM93F'),
('PR000008', 'GKJODNCD'),
('PR000008', 'DEGDCGDE'),
('PR000009', 'DEGDCGDE'),
('PR000009', 'ODNC479W');

INSERT INTO Discount_Movie (movie_id, discount_code)
VALUES
('MV000001', 'FDAFDFDS'),
('MV000002', 'OINOIDN2'),
('MV000003', 'GKJODNCD'),
('MV000004', 'ODNC479W'),
('MV000005', 'YYJCM93F'),
('MV000002', 'YYJCM93F'),
('MV000008', 'GKJODNCD'),
('MV000007', 'DEGDCGDE'),
('MV000009', 'DEGDCGDE'),
('MV000006', 'ODNC479W');

INSERT INTO Rentals (rental_id, movie_id, staff_id, customer_id, due_date, rental_date, return_date, status, total_amount, late_fee)
VALUES
('RT000001', 'MV000001', 'ST000006', 'CU000002', '2024-12-15', '2024-12-01', NULL, 'PENDING', 5.99, 0.00),
('RT000002', 'MV000002', 'ST000007', 'CU000002', '2024-12-10', '2024-12-02', NULL, 'PENDING', 3.99, 0.00),
('RT000003', 'MV000003', 'ST000007', 'CU000003', '2024-12-20', '2024-12-03', NULL, 'PENDING', 6.99, 0.00),
('RT000004', 'MV000004', 'ST000007', 'CU000003', '2024-12-25', '2024-12-04', NULL, 'PENDING', 7.99, 0.00),
('RT000005', 'MV000005', 'ST000006', 'CU000003', '2024-12-30', '2024-12-05', NULL, 'PENDING', 5.49, 0.00),
('RT000006', 'MV000006', 'ST000006', 'CU000004', '2024-12-12', '2024-12-06', NULL, 'PENDING', 6.99, 0.00),
('RT000007', 'MV000007', 'ST000006', 'CU000004', '2024-12-15', '2024-12-07', NULL, 'PENDING', 4.99, 0.00),
('RT000008', 'MV000008', 'ST000006', 'CU000005', '2024-12-18', '2024-12-08', NULL, 'PENDING', 3.49, 0.00),
('RT000009', 'MV000009', 'ST000007', 'CU000005', '2024-12-22', '2024-12-09', NULL, 'PENDING', 7.49, 0.00),
('RT000010', 'MV000010', 'ST000006', 'CU000005', '2024-12-28', '2024-12-10', NULL, 'PENDING', 5.99, 0.00);

INSERT INTO Payments (rental_id, payment_method)
VALUES
('RT000001', 'CARD'),
('RT000002', 'ONLINE'),
('RT000003', 'BANKING'),
('RT000004', 'CARD'),
('RT000005', 'ONLINE'),
('RT000006', 'BANKING'),
('RT000007', 'CARD'),
('RT000008', 'ONLINE'),
('RT000009', 'BANKING'),
('RT000010', 'CARD');

INSERT INTO Profiles (account_id, full_name, birthday, address, phone_number, credit)
VALUES
('CU000002', 'Bob Smith', '1985-06-25', '456 Oak Rd, Shelbyville', '5552345678', 30.00),
('CU000003', 'Charlie Davis', '1992-08-14', '789 Pine Ln, Capital City', '5553456789', 25.00),
('CU000004', 'Diana Martinez', '1993-11-10', '101 Maple Ave, Springfield', '5554567890', 100.00),
('CU000005', 'Ethan Brown', '1988-01-22', '202 Birch Blvd, Shelbyville', '5555678901', 75.00),
('ST000006', 'Fiona Taylor', '1987-09-30', '303 Cedar St, Capital City', '5556789012', 40.00),
('ST000007', 'George White', '1995-12-05', '404 Elm St, Springfield', '5557890123', 60.00),
('PR000008', 'Hannah Wilson', '1991-04-10', '505 Pine Ave, Shelbyville', '5558901234', 20.00),
('PR000009', 'Ivy Clark', '1994-02-18', '606 Oak Rd, Capital City', '5559012345', 90.00),
('CU000010', 'Jack Lewis', '1989-07-03', '707 Maple St, Springfield', '5550123456', 110.00);

INSERT INTO Reviews (review_id, movie_id, customer_id, review_text, rating, review_date)
VALUES
('RV000001', 'MV000010', 'CU000003', 'A thrilling adventure! Highly recommended!', 5, '2024-12-01'),
('RV000002', 'MV000007', 'CU000002', 'An exciting mystery with a twist at the end.', 4, '2024-12-02'),
('RV000003', 'MV000003', 'CU000003', 'A beautiful love story with a happy ending.', 5, '2024-12-03'),
('RV000004', 'MV000001', 'PR000009', 'A great exploration of futuristic possibilities.', 4, '2024-12-04'),
('RV000005', 'MV000002', 'CU000005', 'An action-packed film that keeps you at the edge of your seat.', 5, '2024-12-05'),
('RV000006', 'MV000006', 'CU000002', 'A magical world full of wonder and danger.', 5, '2024-12-06'),
('RV000007', 'MV000007', 'CU000005', 'A fun comedy that made me laugh from start to finish.', 4, '2024-12-07'),
('RV000008', 'MV000008', 'CU000005', 'A documentary that sheds light on important issues.', 4, '2024-12-08'),
('RV000009', 'MV000009', 'PR000008', 'A superhero movie that’s full of action and heart.', 5, '2024-12-09'),
('RV000010', 'MV000010', 'CU000002', 'A terrifying tale of survival in the wild. I couldn’t sleep after watching it!', 5, '2024-12-10');

INSERT INTO Wishlists (wishlist_id, customer_id, movie_id, added_date, priority)
VALUES
('WL000001', 'PR000008', 'MV000001', '2024-12-01', 'HIGH'),
('WL000002', 'CU000003', 'MV000002', '2024-12-02', 'MEDIUM'),
('WL000003', 'CU000003', 'MV000001', '2024-12-03', 'LOW'),
('WL000004', 'CU000004', 'MV000004', '2024-12-04', 'HIGH'),
('WL000005', 'CU000005', 'MV000005', '2024-12-05', 'MEDIUM'),
('WL000006', 'CU000005', 'MV000006', '2024-12-06', 'LOW'),
('WL000007', 'CU000002', 'MV000007', '2024-12-07', 'HIGH'),
('WL000008', 'CU000002', 'MV000009', '2024-12-08', 'MEDIUM'),
('WL000009', 'CU000003', 'MV000009', '2024-12-09', 'LOW'),
('WL000010', 'PR000009', 'MV000006', '2024-12-10', 'HIGH');
