
-- USE movierentalsystemdb;

INSERT INTO Accounts (account_id, username, password, role, email, status, online_at, created_at, updated_at, creability) VALUES
('AC000001', 'john_doe', 'password123', 'CUSTOMER', 'john@example.com', 'OFFLINE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('AC000002', 'jane_doe', 'password123', 'STAFF', 'jane@example.com', 'ONLINE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('AC000003', 'admin_user', 'adminpass', 'ADMIN', 'admin@example.com', 'OFFLINE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('AC000004', 'premium_guy', 'premium123', 'PREMIUM', 'premium@example.com', 'OFFLINE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('AC000005', 'user1', 'user123', 'CUSTOMER', 'user1@example.com', 'ONLINE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('AC000006', 'user2', 'user123', 'CUSTOMER', 'user2@example.com', 'OFFLINE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('AC000007', 'staff1', 'staff123', 'STAFF', 'staff1@example.com', 'OFFLINE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('AC000008', 'staff2', 'staff123', 'STAFF', 'staff2@example.com', 'ONLINE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('AC000009', 'banned_user', 'banned123', 'CUSTOMER', 'banned@example.com', 'BANNED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0),
('AC000010', 'premium_user', 'premiumpass', 'PREMIUM', 'premium2@example.com', 'OFFLINE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

INSERT INTO Languages (language_code, language_name) VALUES
('EN', 'English'),
('FR', 'French'),
('ES', 'Spanish'),
('DE', 'German'),
('IT', 'Italian'),
('JP', 'Japanese'),
('CN', 'Chinese'),
('RU', 'Russian'),
('PT', 'Portuguese'),
('AR', 'Arabic');

INSERT INTO Genres (genre_name, description) VALUES
('Action', 'High-energy scenes with physical feats'),
('Drama', 'Serious, plot-driven stories'),
('Comedy', 'Humorous or satirical content'),
('Horror', 'Designed to frighten and thrill'),
('Romance', 'Focuses on romantic relationships'),
('Sci-Fi', 'Fictional science and futuristic elements'),
('Fantasy', 'Magical and supernatural elements'),
('Documentary', 'Non-fictional films'),
('Thriller', 'High suspense and tension'),
('Animation', 'Animated films');

INSERT INTO Movies (movie_id, title, description, avg_rating, release_year, rental_price, available_copies, created_at, updated_at) VALUES
('MV000001', 'Action Movie 1', 'Explosive action scenes', 4.5, '2022-01-15', 3.99, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('MV000002', 'Drama Movie 1', 'Touching story', 4.0, '2021-03-12', 4.99, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('MV000003', 'Comedy Movie 1', 'Laugh-out-loud moments', 3.8, '2020-07-19', 2.99, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('MV000004', 'Horror Movie 1', 'Chilling experience', 4.2, '2019-10-31', 3.49, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('MV000005', 'Sci-Fi Movie 1', 'Futuristic adventures', 3.9, '2018-04-21', 5.99, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('MV000006', 'Fantasy Movie 1', 'Magical world', 4.3, '2022-11-10', 4.49, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('MV000007', 'Documentary 1', 'In-depth analysis', 4.8, '2017-05-13', 2.50, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('MV000008', 'Thriller Movie 1', 'Edge of the seat thriller', 4.1, '2021-08-18', 3.75, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('MV000009', 'Romance Movie 1', 'Heartwarming romance', 4.7, '2020-02-14', 3.99, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('MV000010', 'Animation Movie 1', 'Family-friendly animation', 4.6, '2023-06-25', 4.59, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO Discounts (discount_code, discount_type, discount_value, start_date, end_date, quantity, is_active, apply_for_what, apply_for_who) VALUES
('DCX7A9T3', 'PERCENT', 10.00, '2024-01-01', '2024-06-01', 100, TRUE, 'GLOBAL', 'ALL_USERS'),
('DCR5L8N9', 'FIXED_AMOUNT', 5.00, '2024-02-01', '2024-07-01', 50, TRUE, 'GENRE', 'PREMIUM'),
('DCJ3K4F6', 'PERCENT', 15.00, '2024-03-01', '2024-08-01', 200, TRUE, 'SPECIFIC_MOVIES', 'GUESTS'),
('DCH9Y2Z1', 'BUY_X_GET_Y_FREE', 0.00, '2024-04-01', '2024-09-01', 150, TRUE, 'CART_TOTAL', 'ALL_USERS'),
('DCK7P1F3', 'FIXED_AMOUNT', 7.50, '2024-05-01', '2024-10-01', 80, TRUE, 'GLOBAL', 'SPECIFIC_USERS'),
('DCX2A4G8', 'PERCENT', 20.00, '2024-06-01', '2024-11-01', 120, TRUE, 'GENRE', 'ALL_USERS'),
('DCR3L5M7', 'FIXED_AMOUNT', 3.00, '2024-07-01', '2024-12-01', 90, TRUE, 'SPECIFIC_MOVIES', 'PREMIUM'),
('DCJ4H6K9', 'BUY_X_GET_Y_FREE', 0.00, '2024-08-01', '2025-01-01', 60, TRUE, 'CART_TOTAL', 'GUESTS'),
('DCH2Y5Z3', 'PERCENT', 25.00, '2024-09-01', '2025-02-01', 200, TRUE, 'GLOBAL', 'SPECIFIC_USERS'),
('DCK9P2F1', 'FIXED_AMOUNT', 10.00, '2024-10-01', '2025-03-01', 100, TRUE, 'GENRE', 'ALL_USERS');

INSERT INTO Actors (actor_id, actor_name, actor_rank, actor_description) VALUES
('AT000001', 'Robert Downey Jr.', 'A', 'Famous for superhero roles'),
('AT000002', 'Scarlett Johansson', 'A', 'Versatile actress with many action roles'),
('AT000003', 'Chris Hemsworth', 'B', 'Known for action and comedy'),
('AT000004', 'Jennifer Lawrence', 'A', 'Award-winning actress with diverse roles'),
('AT000005', 'Dwayne Johnson', 'A', 'Action hero and former wrestler'),
('AT000006', 'Tom Holland', 'B', 'Young rising star in action films'),
('AT000007', 'Emma Watson', 'A', 'Known for fantasy and drama roles'),
('AT000008', 'Will Smith', 'A', 'Multi-talented actor with a range of genres'),
('AT000009', 'Gal Gadot', 'A', 'Popular for superhero and action roles'),
('AT000010', 'Morgan Freeman', 'A', 'Legendary actor with iconic voice');

INSERT INTO Movie_Actor (movie_id, actor_id, role) VALUES
('MV000001', 'AT000001', 'MAIN'),
('MV000002', 'AT000002', 'SUPPORT'),
('MV000003', 'AT000003', 'MAIN'),
('MV000004', 'AT000004', 'VILLAIN'),
('MV000005', 'AT000005', 'MAIN'),
('MV000006', 'AT000006', 'SUPPORT'),
('MV000007', 'AT000007', 'CAMEO'),
('MV000008', 'AT000008', 'MAIN'),
('MV000009', 'AT000009', 'MAIN'),
('MV000010', 'AT000010', 'SUPPORT');

INSERT INTO Movie_Genre (movie_id, genre_name) VALUES
('MV000001', 'Action'),
('MV000002', 'Drama'),
('MV000003', 'Comedy'),
('MV000004', 'Horror'),
('MV000005', 'Sci-Fi'),
('MV000006', 'Fantasy'),
('MV000007', 'Documentary'),
('MV000008', 'Thriller'),
('MV000009', 'Romance'),
('MV000010', 'Animation');

INSERT INTO Movie_Language (movie_id, language_code) VALUES
('MV000001', 'EN'),
('MV000002', 'FR'),
('MV000003', 'ES'),
('MV000004', 'DE'),
('MV000005', 'IT'),
('MV000006', 'JP'),
('MV000007', 'CN'),
('MV000008', 'RU'),
('MV000009', 'PT'),
('MV000010', 'AR');

INSERT INTO Discount_Account (customer_id, discount_code) VALUES
('AC000001', 'DCX7A9T3'),
('AC000002', 'DCR5L8N9'),
('AC000003', 'DCJ3K4F6'),
('AC000004', 'DCH9Y2Z1'),
('AC000005', 'DCK7P1F3'),
('AC000006', 'DCX2A4G8'),
('AC000007', 'DCR3L5M7'),
('AC000008', 'DCJ4H6K9'),
('AC000009', 'DCH2Y5Z3'),
('AC000010', 'DCK9P2F1');

INSERT INTO Discount_Movie (movie_id, discount_code) VALUES
('MV000001', 'DCX7A9T3'),
('MV000002', 'DCR5L8N9'),
('MV000003', 'DCJ3K4F6'),
('MV000004', 'DCH9Y2Z1'),
('MV000005', 'DCK7P1F3'),
('MV000006', 'DCX2A4G8'),
('MV000007', 'DCR3L5M7'),
('MV000008', 'DCJ4H6K9'),
('MV000009', 'DCH2Y5Z3'),
('MV000010', 'DCK9P2F1');

INSERT INTO Rentals (movie_id, staff_id, customer_id, due_date, rental_date, return_date, status, total_amount, late_fee) VALUES
('MV000001', 'AC000002', 'AC000001', '2024-12-15', '2024-12-01', NULL, 'PENDING', 3.99, 0.00),
('MV000002', 'AC000007', 'AC000005', '2024-12-18', '2024-12-05', NULL, 'APPROVED', 4.99, 0.00),
('MV000003', NULL, 'AC000006', '2024-12-20', '2024-12-08', '2024-12-10', 'APPROVED', 2.99, 0.00),
('MV000004', 'AC000008', 'AC000009', '2024-12-25', '2024-12-12', NULL, 'DENIED', 3.49, 0.00),
('MV000005', 'AC000007', 'AC000010', '2024-12-22', '2024-12-10', NULL, 'PENDING', 5.99, 0.00),
('MV000006', 'AC000008', 'AC000004', '2024-12-30', '2024-12-15', NULL, 'PENDING', 4.49, 0.00),
('MV000007', NULL, 'AC000001', '2024-12-10', '2024-12-02', '2024-12-09', 'APPROVED', 2.50, 0.00),
('MV000008', 'AC000002', 'AC000005', '2024-12-12', '2024-12-01', NULL, 'PENDING', 3.75, 0.00),
('MV000009', NULL, 'AC000006', '2024-12-14', '2024-12-05', NULL, 'APPROVED', 3.99, 0.00),
('MV000010', 'AC000002', 'AC000007', '2024-12-18', '2024-12-06', NULL, 'PENDING', 4.59, 0.00);

INSERT INTO Payments (payment_id, customer_id, amount, payment_method, transaction_time, status) VALUES
('PM000001', 'AC000001', 3.99, 'CARD', CURRENT_TIMESTAMP, 'COMPLETED'),
('PM000002', 'AC000005', 4.99, 'ONLINE', CURRENT_TIMESTAMP, 'COMPLETED'),
('PM000003', 'AC000006', 2.99, 'BANKING', CURRENT_TIMESTAMP, 'COMPLETED'),
('PM000004', 'AC000009', 3.49, 'CARD', CURRENT_TIMESTAMP, 'PENDING'),
('PM000005', 'AC000010', 5.99, 'ONLINE', CURRENT_TIMESTAMP, 'FAILED'),
('PM000006', 'AC000004', 4.49, 'BANKING', CURRENT_TIMESTAMP, 'COMPLETED'),
('PM000007', 'AC000001', 2.50, 'CARD', CURRENT_TIMESTAMP, 'COMPLETED'),
('PM000008', 'AC000005', 3.75, 'ONLINE', CURRENT_TIMESTAMP, 'PENDING'),
('PM000009', 'AC000006', 3.99, 'CARD', CURRENT_TIMESTAMP, 'COMPLETED'),
('PM000010', 'AC000007', 4.59, 'BANKING', CURRENT_TIMESTAMP, 'PENDING');

INSERT INTO Profiles (account_id, full_name, birthday, address, phone_number, credit) VALUES
('AC000001', 'John Doe', '1990-01-01', '123 Main St', '5551234567', 20.00),
('AC000002', 'Jane Doe', '1985-05-15', '456 Elm St', '5552345678', 15.50),
('AC000003', 'Admin User', '1980-09-25', '789 Oak St', '5553456789', 0.00),
('AC000004', 'Premium Guy', '1992-04-12', '101 Maple St', '5554567890', 25.75),
('AC000005', 'User One', '1995-07-18', '202 Birch St', '5555678901', 10.00),
('AC000006', 'User Two', '1988-11-30', '303 Pine St', '5556789012', 5.50),
('AC000007', 'Staff One', '1993-03-22', '404 Cedar St', '5557890123', 30.00),
('AC000008', 'Staff Two', '1979-10-05', '505 Spruce St', '5558901234', 12.75),
('AC000009', 'Banned User', '1991-08-08', '606 Willow St', '5559012345', 0.00),
('AC000010', 'Premium User', '1987-02-20', '707 Palm St', '5550123456', 50.00);

INSERT INTO Reviews (movie_id, customer_id, review_text, rating, review_date) VALUES
('MV000001', 'AC000001', 'Amazing action scenes!', 5, '2024-12-01'),
('MV000002', 'AC000005', 'Touching and emotional.', 4, '2024-12-03'),
('MV000003', 'AC000006', 'Hilarious and fun!', 4, '2024-12-04'),
('MV000004', 'AC000009', 'Scary and intense.', 5, '2024-12-05'),
('MV000005', 'AC000010', 'Mind-blowing visuals.', 5, '2024-12-06'),
('MV000006', 'AC000004', 'Magical experience.', 4, '2024-12-07'),
('MV000007', 'AC000001', 'Informative and engaging.', 5, '2024-12-08'),
('MV000008', 'AC000005', 'Thrilling from start to end.', 4, '2024-12-09'),
('MV000009', 'AC000006', 'Romantic and heartwarming.', 4, '2024-12-10'),
('MV000010', 'AC000007', 'Family-friendly and fun.', 5, '2024-12-11');

INSERT INTO Wishlists (customer_id, movie_id, added_date, priority) VALUES
('AC000001', 'MV000001', '2024-12-01', 'HIGH'),
('AC000005', 'MV000002', '2024-12-02', 'MEDIUM'),
('AC000006', 'MV000003', '2024-12-03', 'LOW'),
('AC000009', 'MV000004', '2024-12-04', 'HIGH'),
('AC000010', 'MV000005', '2024-12-05', 'MEDIUM'),
('AC000004', 'MV000006', '2024-12-06', 'LOW'),
('AC000001', 'MV000007', '2024-12-07', 'HIGH'),
('AC000005', 'MV000008', '2024-12-08', 'MEDIUM'),
('AC000006', 'MV000009', '2024-12-09', 'LOW'),
('AC000007', 'MV000010', '2024-12-10', 'HIGH');
