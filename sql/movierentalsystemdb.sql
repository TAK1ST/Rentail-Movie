-- drop schema movierentalsystemdb;
-- create schema movierentalsystemdb; -- 

USE movierentalsystemdb;

	CREATE TABLE IF NOT EXISTS Accounts (
    account_id CHAR(8) PRIMARY KEY,
    username NVARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('NONE', 'ADMIN', 'CUSTOMER', 'STAFF', 'PREMIUM') NOT NULL,
    email VARCHAR(50) NOT NULL,
    status ENUM('NONE', 'ONLINE', 'BANNED', 'OFFLINE') NOT NULL
);
	CREATE TABLE IF NOT EXISTS Profiles (
    account_id CHAR(8),
    full_name NVARCHAR(60),
    birth_day DATE NOT NULL,
    address NVARCHAR(255),
    phone_number CHAR(10),
    credit DECIMAL(10 , 2 ) DEFAULT 0.00,
    FOREIGN KEY (account_id)
        REFERENCES Accounts (account_id)
);
CREATE TABLE IF NOT EXISTS Movies (
    movie_id CHAR(8) PRIMARY KEY,
    title NVARCHAR(100) NOT NULL,
    description TEXT,
    avg_rating DOUBLE(5 , 1 ),
    release_year DATE,
    rental_price DECIMAL(10 , 2) NOT NULL,
    available_copies INT DEFAULT 1
);
CREATE TABLE IF NOT EXISTS Languages (
    language_code CHAR(2) PRIMARY KEY,
    language_name NVARCHAR(100) NOT NULL
);
CREATE TABLE IF NOT EXISTS Movie_Language (
    movie_id CHAR(8) NOT NULL,
    language_code CHAR(2) NOT NULL,
    FOREIGN KEY (movie_id)
        REFERENCES Movies (movie_id),
    FOREIGN KEY (language_code)
        REFERENCES Languages (language_code),
    PRIMARY KEY (movie_id , language_code)
);

CREATE TABLE IF NOT EXISTS Genres (
    genre_name NVARCHAR(100) PRIMARY KEY,
    description TEXT
);

CREATE TABLE IF NOT EXISTS Movie_Genre (
    movie_id CHAR(8) NOT NULL,
    genre_name NVARCHAR(100) NOT NULL,
    FOREIGN KEY (movie_id)
        REFERENCES Movies (movie_id),
    FOREIGN KEY (genre_name)
        REFERENCES Genres (genre_name),
    PRIMARY KEY (movie_id , genre_name)
);


CREATE TABLE IF NOT EXISTS Actors (
    actor_id CHAR(8) PRIMARY KEY,
    actor_name NVARCHAR(255) NOT NULL,
    actor_rank ENUM('NONE', 'A', 'B', 'C', 'D') NOT NULL
);

CREATE TABLE IF NOT EXISTS Movie_Actor (
    movie_id CHAR(8),
    actor_id CHAR(8),
    role ENUM('NONE','MAIN', 'VILLAIN', 'BACKGROUND', 'SUPPORT', 'CAMEO') NOT NULL,
    PRIMARY KEY (movie_id , actor_id),
    FOREIGN KEY (movie_id)
        REFERENCES Movies (movie_id),
    FOREIGN KEY (actor_id)
        REFERENCES Actors (actor_id)
);

CREATE TABLE IF NOT EXISTS Rentals (
    rental_id CHAR(8) PRIMARY KEY,
    movie_id CHAR(8) NOT NULL,
    staff_id CHAR(8) NOT NULL,
    customer_id CHAR(8) NOT NULL,
    due_date DATE NOT NULL,
    rental_date DATE NOT NULL,
    return_date DATE ,
    status ENUM('NONE', 'PENDING', 'APPROVED', 'DENIED') NOT NULL,
    total_amount DECIMAL(10 , 2) DEFAULT 0.00,
    late_fee DECIMAL(10 , 2) DEFAULT 0.00,
    FOREIGN KEY (movie_id)
        REFERENCES Movies (movie_id),
    FOREIGN KEY (staff_id)
        REFERENCES Accounts (account_id),
    FOREIGN KEY (customer_id)
        REFERENCES Accounts (account_id)
);

CREATE TABLE IF NOT EXISTS Reviews (
    review_id CHAR(8) PRIMARY KEY,
    movie_id CHAR(8) NOT NULL,
    customer_id CHAR(8) NOT NULL,
    review_text TEXT,
    rating INT NOT NULL,
    review_date DATE NOT NULL,
    FOREIGN KEY (movie_id)
        REFERENCES Movies (movie_id),
    FOREIGN KEY (customer_id)
        REFERENCES Accounts (account_id)
);

CREATE TABLE IF NOT EXISTS Payments (
    payment_id CHAR(8) PRIMARY KEY,
    rental_id CHAR(8) NOT NULL,
    payment_method ENUM('NONE', 'CARD', 'ONLINE', 'BANKING') NOT NULL,
    FOREIGN KEY (rental_id)
        REFERENCES Rentals (rental_id)
);

CREATE TABLE IF NOT EXISTS Wishlists (
    wishlist_id CHAR(8) PRIMARY KEY,
    customer_id CHAR(8) NOT NULL,
    movie_id CHAR(8) NOT NULL,
    added_date DATE NOT NULL,
    priority ENUM('NONE', 'HIGH', 'MEDIUM', 'LOW') NOT NULL,
    FOREIGN KEY (movie_id)
        REFERENCES Movies (movie_id),
    FOREIGN KEY (customer_id)
        REFERENCES Accounts (account_id)
);
CREATE TABLE IF NOT EXISTS Discounts (
    discount_code VARCHAR(50) PRIMARY KEY,
    customer_id CHAR(8) NOT NULL,
    discount_type ENUM('NONE', 'PERCENT', 'FITED_AMOUNT', 'BUY_X_GET_Y_FREE') NOT NULL,
    discount_value DECIMAL(10 , 2) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    usage_available INT DEFAULT 1,
    is_active BOOLEAN DEFAULT TRUE,
    foreign key (customer_id) references Accounts (account_id)
)
