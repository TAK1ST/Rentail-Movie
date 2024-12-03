CREATE DATABASE IF NOT EXISTS movierentalsystemdb;
USE movierentalsystemdb;
-- drop database movierentalsystemdb;
CREATE TABLE IF NOT EXISTS Accounts (
    account_id CHAR(8) PRIMARY KEY,
    username NVARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'CUSTOMER', 'STAFF', 'PREMIUM') DEFAULT 'CUSTOMER' NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    status ENUM('ONLINE', 'BANNED', 'OFFLINE') DEFAULT 'OFFLINE' NOT NULL,
    online_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    creability INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS Languages (
    language_code CHAR(2) PRIMARY KEY,
    language_name NVARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS Genres (
    genre_name NVARCHAR(100) PRIMARY KEY,
    description TEXT
);

CREATE TABLE IF NOT EXISTS Movies (
    movie_id CHAR(8) PRIMARY KEY,
    title NVARCHAR(100) NOT NULL,
    description TEXT,
    avg_rating DOUBLE(3, 1) DEFAULT 0.0,
    release_year DATE,
    rental_price DECIMAL(10, 2) NOT NULL,
    available_copies INT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS Discounts (
    discount_code CHAR(8) PRIMARY KEY,
    discount_type ENUM('PERCENT', 'FIXED_AMOUNT', 'BUY_X_GET_Y_FREE') NOT NULL DEFAULT 'PERCENT',
    discount_value DECIMAL(10, 2) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    quantity INT DEFAULT 1,
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS Actors (
    actor_id CHAR(8) PRIMARY KEY,
    actor_name NVARCHAR(100) NOT NULL,
    actor_rank ENUM('A', 'B', 'C', 'D') DEFAULT 'C',
    actor_description NVARCHAR(255)
);

CREATE TABLE IF NOT EXISTS Movie_Actor (
    movie_id CHAR(8),
    actor_id CHAR(8),
    role ENUM('MAIN', 'VILLAIN', 'SUPPORT', 'CAMEO') NOT NULL,
    PRIMARY KEY (movie_id, actor_id),
    FOREIGN KEY (movie_id) REFERENCES Movies (movie_id) ON DELETE CASCADE,
    FOREIGN KEY (actor_id) REFERENCES Actors (actor_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Movie_Genre (
    movie_id CHAR(8) NOT NULL,
    genre_name NVARCHAR(100) NOT NULL,
    PRIMARY KEY (movie_id, genre_name),
    FOREIGN KEY (movie_id) REFERENCES Movies (movie_id) ON DELETE CASCADE,
    FOREIGN KEY (genre_name) REFERENCES Genres (genre_name) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Movie_Language (
    movie_id CHAR(8) NOT NULL,
    language_code CHAR(2) NOT NULL,
    PRIMARY KEY (movie_id, language_code),
    FOREIGN KEY (movie_id) REFERENCES Movies (movie_id) ON DELETE CASCADE,
    FOREIGN KEY (language_code) REFERENCES Languages (language_code) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Discount_Account (
    customer_id CHAR(8) NOT NULL,
    discount_code CHAR(8) NOT NULL,
    PRIMARY KEY (customer_id, discount_code),
    FOREIGN KEY (customer_id) REFERENCES Accounts(account_id) ON DELETE CASCADE,
    FOREIGN KEY (discount_code) REFERENCES Discounts(discount_code) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Discount_Movie (
    movie_id CHAR(8) NOT NULL,
    discount_code CHAR(8) NOT NULL,
    PRIMARY KEY (movie_id, discount_code),
    FOREIGN KEY (movie_id) REFERENCES Movies(movie_id) ON DELETE CASCADE,
    FOREIGN KEY (discount_code) REFERENCES Discounts(discount_code) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Rentals (
    rental_id CHAR(8) PRIMARY KEY,
    movie_id CHAR(8) NOT NULL,
    staff_id CHAR(8),
    customer_id CHAR(8) NOT NULL,
    due_date DATE NOT NULL,
    rental_date DATE NOT NULL,
    return_date DATE,
    status ENUM('PENDING', 'APPROVED', 'DENIED') NOT NULL DEFAULT 'PENDING',
    total_amount DECIMAL(10, 2) DEFAULT 0.00,
    late_fee DECIMAL(10, 2) DEFAULT 0.00,
    FOREIGN KEY (movie_id) REFERENCES Movies (movie_id) ON DELETE CASCADE,
    FOREIGN KEY (staff_id) REFERENCES Accounts (account_id) ON DELETE SET NULL,
    FOREIGN KEY (customer_id) REFERENCES Accounts (account_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Payments (
    rental_id CHAR(8),
    payment_method ENUM('CARD', 'ONLINE', 'BANKING') NOT NULL DEFAULT 'CARD',
    FOREIGN KEY (rental_id) REFERENCES Rentals (rental_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Profiles (
    account_id CHAR(8),
    full_name NVARCHAR(60),
    birthday DATE NOT NULL,
    address NVARCHAR(255),
    phone_number CHAR(10),
    credit DECIMAL(10, 2) DEFAULT 0.00,
    FOREIGN KEY (account_id) REFERENCES Accounts (account_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Reviews (
    review_id CHAR(8) PRIMARY KEY,
    movie_id CHAR(8) NOT NULL,
    customer_id CHAR(8) NOT NULL,
    review_text TEXT,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    review_date DATE NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES Movies (movie_id) ON DELETE CASCADE,
    FOREIGN KEY (customer_id) REFERENCES Accounts (account_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Wishlists (
    wishlist_id CHAR(8) PRIMARY KEY,
    customer_id CHAR(8) NOT NULL,
    movie_id CHAR(8) NOT NULL,
    added_date DATE NOT NULL,
    priority ENUM('HIGH', 'MEDIUM', 'LOW') NOT NULL DEFAULT 'MEDIUM',
    FOREIGN KEY (movie_id) REFERENCES Movies (movie_id) ON DELETE CASCADE,
    FOREIGN KEY (customer_id) REFERENCES Accounts (account_id) ON DELETE CASCADE
);
-- TRIGGER

 -- Giảm số lượng bản sao
DELIMITER //
CREATE TRIGGER reduce_movie_copies
AFTER INSERT ON Rentals
FOR EACH ROW
BEGIN
    UPDATE Movies
    SET available_copies = available_copies - 1
    WHERE movie_id = NEW.movie_id;
END; //
DELIMITER ;

 -- Tăng số lượng bản sao
DELIMITER //
CREATE TRIGGER increase_movie_copies
AFTER UPDATE ON Rentals
FOR EACH ROW
BEGIN
    IF NEW.return_date IS NOT NULL THEN
        UPDATE Movies
        SET available_copies = available_copies + 1
        WHERE movie_id = NEW.movie_id;
    END IF;
END; //
DELIMITER ;

-- Ngừng khuyến mãi khi hết hạn
DELIMITER //
CREATE TRIGGER expire_discount
BEFORE UPDATE ON Discounts
FOR EACH ROW
BEGIN
    IF OLD.end_date < CURRENT_DATE THEN
        SET NEW.is_active = FALSE;
    END IF;
END; //
DELIMITER ;

-- Tạo event tự động kiểm tra tài khoản BANNED sau mỗi ngày
SET GLOBAL event_scheduler = ON;

-- Kiểm tra tài khoản BANNED sau mỗi ngày
DELIMITER //
CREATE EVENT unban_accounts_event
ON SCHEDULE EVERY 1 DAY
DO
BEGIN
    -- Kiểm tra các tài khoản bị BANNED và bị cấm hơn 30 ngày
    UPDATE Accounts
    SET status = 'OFFLINE'
    WHERE status = 'BANNED' 
    AND DATEDIFF(CURRENT_DATE, updated_at) >= 30;
END; //
DELIMITER ;


