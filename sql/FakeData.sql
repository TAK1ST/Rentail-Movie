
-- USE movierentalsystemdb;

INSERT INTO Accounts VALUES 
('AD000001', 'admin_user', 'password123', 'ADMIN', 'admin@mail.com', 'OFFLINE', DEFAULT, DEFAULT, DEFAULT, 10),
('CU000002', 'cust_user1', 'password123', 'CUSTOMER', 'cust1@mail.com', 'ONLINE', DEFAULT, DEFAULT, DEFAULT, 5),
('CU000003', 'cust_user2', 'password123', 'CUSTOMER', 'cust2@mail.com', 'BANNED', DEFAULT, DEFAULT, DEFAULT, 3),
('ST000004', 'staff_user', 'password123', 'STAFF', 'staff@mail.com', 'OFFLINE', DEFAULT, DEFAULT, DEFAULT, 7),
('PR000005', 'premium_user1', 'password123', 'PREMIUM', 'premium1@mail.com', 'OFFLINE', DEFAULT, DEFAULT, DEFAULT, 20),
('PR000006', 'premium_user2', 'password123', 'PREMIUM', 'premium2@mail.com', 'ONLINE', DEFAULT, DEFAULT, DEFAULT, 15),
('CU000007', 'cust_user3', 'password123', 'CUSTOMER', 'cust3@mail.com', 'OFFLINE', DEFAULT, DEFAULT, DEFAULT, 4),
('ST000008', 'staff_user2', 'password123', 'STAFF', 'staff2@mail.com', 'OFFLINE', DEFAULT, DEFAULT, DEFAULT, 6),
('CU000009', 'cust_user4', 'password123', 'CUSTOMER', 'cust4@mail.com', 'ONLINE', DEFAULT, DEFAULT, DEFAULT, 8),
('PR000010', 'premium_user3', 'password123', 'PREMIUM', 'premium3@mail.com', 'OFFLINE', DEFAULT, DEFAULT, DEFAULT, 25);

INSERT INTO Languages VALUES
('EN', 'English'),
('FR', 'French'),
('ES', 'Spanish'),
('DE', 'German'),
('IT', 'Italian'),
('RU', 'Russian'),
('CN', 'Chinese'),
('JP', 'Japanese'),
('KR', 'Korean'),
('PT', 'Portuguese');

INSERT INTO Genres VALUES 
('GR001', 'Action'),
('GR002', 'Comedy'),
('GR003', 'Drama'),
('GR004', 'Thriller'),
('GR005', 'Horror'),
('GR006', 'Sci-Fi'),
('GR007', 'Romance'),
('GR008', 'Fantasy'),
('GR009', 'Documentary'),
('GR010', 'Animation');

INSERT INTO Movies VALUES 
('MV000001', 'Movie 1', 'Description 1', 3.5, '2022-01-01', 12.99, 5, DEFAULT, DEFAULT),
('MV000002', 'Movie 2', 'Description 2', 4.0, '2021-05-10', 10.50, 3, DEFAULT, DEFAULT),
('MV000003', 'Movie 3', 'Description 3', 4.5, '2020-07-15', 8.99, 7, DEFAULT, DEFAULT),
('MV000004', 'Movie 4', 'Description 4', 2.5, '2019-02-20', 6.00, 1, DEFAULT, DEFAULT),
('MV000005', 'Movie 5', 'Description 5', 5.0, '2018-11-11', 15.00, 4, DEFAULT, DEFAULT),
('MV000006', 'Movie 6', 'Description 6', 3.0, '2023-03-25', 9.99, 10, DEFAULT, DEFAULT),
('MV000007', 'Movie 7', 'Description 7', 4.2, '2022-08-17', 13.00, 2, DEFAULT, DEFAULT),
('MV000008', 'Movie 8', 'Description 8', 4.8, '2020-12-30', 14.75, 6, DEFAULT, DEFAULT),
('MV000009', 'Movie 9', 'Description 9', 2.0, '2021-06-01', 5.50, 9, DEFAULT, DEFAULT),
('MV000010', 'Movie 10', 'Description 10', 3.7, '2022-04-13', 11.20, 8, DEFAULT, DEFAULT);

INSERT INTO Discounts VALUES
('NN1KD2FD', 'PERCENT', 10.00, '2023-11-01', '2023-12-31', 100, TRUE, 'GLOBAL', 'ALL_USERS'),
('XY8QW9RT', 'FIXED_AMOUNT', 5.00, '2023-10-01', '2023-12-15', 50, TRUE, 'SPECIFIC_MOVIES', 'PREMIUM'),
('ZL4MP3XB', 'PERCENT', 15.00, '2023-09-01', '2024-01-01', 30, TRUE, 'GENRE', 'SPECIFIC_USERS'),
('QR7HT8JN', 'BUY_X_GET_Y_FREE', 1.00, '2023-12-01', '2023-12-25', 10, TRUE, 'CART_TOTAL', 'ALL_USERS'),
('KL2CD4NM', 'FIXED_AMOUNT', 20.00, '2023-12-01', '2024-02-28', 20, TRUE, 'GLOBAL', 'PREMIUM');

INSERT INTO Actors VALUES
('AT000001', 'Actor One', 'A', 'Lead actor in many films.'),
('AT000002', 'Actor Two', 'B', 'Known for supporting roles.'),
('AT000003', 'Actor Three', 'C', 'Upcoming star.'),
('AT000004', 'Actor Four', 'A', 'Multiple award winner.'),
('AT000005', 'Actor Five', 'D', 'Occasional appearances.'),
('AT000006', 'Actor Six', 'B', 'Strong performances in thrillers.'),
('AT000007', 'Actor Seven', 'C', 'Known for indie films.'),
('AT000008', 'Actor Eight', 'A', 'Box office favorite.'),
('AT000009', 'Actor Nine', 'D', 'Comedic roles.'),
('AT000010', 'Actor Ten', 'B', 'Sci-fi genre specialist.');

INSERT INTO Movie_Actor VALUES
('MV000001', 'AT000001', 'MAIN'),
('MV000001', 'AT000002', 'SUPPORT'),
('MV000002', 'AT000003', 'MAIN'),
('MV000002', 'AT000004', 'VILLAIN'),
('MV000003', 'AT000005', 'CAMEO'),
('MV000004', 'AT000006', 'MAIN'),
('MV000005', 'AT000007', 'SUPPORT'),
('MV000006', 'AT000008', 'MAIN'),
('MV000007', 'AT000009', 'CAMEO'),
('MV000008', 'AT000010', 'VILLAIN');

INSERT INTO Movie_Genre VALUES
('MV000001', 'GR001'),
('MV000002', 'GR002'),
('MV000003', 'GR003'),
('MV000004', 'GR004'),
('MV000005', 'GR005'),
('MV000006', 'GR006'),
('MV000007', 'GR007'),
('MV000008', 'GR008'),
('MV000009', 'GR009'),
('MV000010', 'GR010');

INSERT INTO Movie_Language VALUES
('MV000001', 'EN'),
('MV000002', 'FR'),
('MV000003', 'ES'),
('MV000004', 'DE'),
('MV000005', 'IT'),
('MV000006', 'RU'),
('MV000007', 'CN'),
('MV000008', 'JP'),
('MV000009', 'KR'),
('MV000010', 'PT');

INSERT INTO Discount_Account VALUES
('CU000002', 'NN1KD2FD'),
('CU000003', 'XY8QW9RT'),
('PR000005', 'ZL4MP3XB'),
('CU000007', 'QR7HT8JN'),
('CU000009', 'KL2CD4NM'),
('ST000004', 'NN1KD2FD'),
('PR000005', 'XY8QW9RT'),
('ST000004', 'ZL4MP3XB'),
('PR000006', 'QR7HT8JN'),
('AD000001', 'KL2CD4NM');

INSERT INTO Discount_Movie VALUES
('MV000001', 'NN1KD2FD'),
('MV000002', 'XY8QW9RT'),
('MV000003', 'ZL4MP3XB'),
('MV000004', 'QR7HT8JN'),
('MV000005', 'KL2CD4NM'),
('MV000006', 'NN1KD2FD'),
('MV000007', 'XY8QW9RT'),
('MV000008', 'ZL4MP3XB'),
('MV000009', 'QR7HT8JN'),
('MV000010', 'KL2CD4NM');

INSERT INTO Rentals VALUES
('RT000001', 'MV000001', 'ST000004', 'CU000002', '2023-12-10', '2023-12-01', NULL, 'PENDING', 12.99, 0.00),
('RT000002', 'MV000002', 'ST000008', 'CU000003', '2023-12-15', '2023-12-05', NULL, 'APPROVED', 10.50, 0.00),
('RT000003', 'MV000003', NULL, 'PR000005', '2023-12-12', '2023-12-02', '2023-12-10', 'DENIED', 8.99, 0.00),
('RT000004', 'MV000004', 'ST000008', 'CU000007', '2023-12-20', '2023-12-03', NULL, 'PENDING', 6.00, 0.00),
('RT000005', 'MV000005', 'ST000004', 'CU000009', '2023-12-18', '2023-12-04', NULL, 'APPROVED', 15.00, 1.50);

INSERT INTO Payments VALUES
('PM000001', 'CU000002', 12.99, 'ONLINE', DEFAULT, 'COMPLETED'),
('PM000002', 'CU000003', 10.50, 'CARD', DEFAULT, 'COMPLETED'),
('PM000003', 'PR000005', 8.99, 'ONLINE', DEFAULT, 'FAILED'),
('PM000004', 'CU000007', 6.00, 'BANKING', DEFAULT, 'PENDING'),
('PM000005', 'CU000009', 15.00, 'CARD', DEFAULT, 'COMPLETED');

INSERT INTO Profiles VALUES
('AD000001', 'Admin User', '1990-01-01', '123 Admin St', '1234567890', 50.00),
('CU000002', 'Customer User1', '1995-05-05', '456 Customer Ave', '0987654321', 25.00),
('CU000003', 'Customer User2', '1988-03-15', '789 Customer Blvd', '1122334455', 15.00),
('ST000004', 'Staff User1', '1985-08-20', '321 Staff Way', '6677889900', 30.00),
('PR000005', 'Premium User1', '1992-12-12', '654 Premium Lane', '1234432112', 100.00);

INSERT INTO Reviews VALUES
('RV000001', 'MV000001', 'CU000002', 'Great movie!', 5, '2023-12-02'),
('RV000002', 'MV000002', 'CU000003', 'Not bad.', 3, '2023-12-04'),
('RV000003', 'MV000003', 'PR000005', 'Loved it!', 4, '2023-12-06'),
('RV000004', 'MV000004', 'CU000007', 'Could be better.', 2, '2023-12-07'),
('RV000005', 'MV000005', 'CU000009', 'Amazing experience!', 5, '2023-12-09');

INSERT INTO Wishlists VALUES
('WL000001', 'CU000002', 'MV000001', '2023-12-01', 'HIGH'),
('WL000002', 'CU000003', 'MV000002', '2023-12-02', 'MEDIUM'),
('WL000003', 'PR000005', 'MV000003', '2023-12-03', 'LOW'),
('WL000004', 'CU000007', 'MV000004', '2023-12-04', 'HIGH'),
('WL000005', 'CU000009', 'MV000005', '2023-12-05', 'MEDIUM');

