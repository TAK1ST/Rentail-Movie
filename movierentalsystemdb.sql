-- drop schema movierentalsystemdb;
-- create schema movierentalsystemdb;-- 

USE movierentalsystemdb;

CREATE TABLE IF NOT EXISTS Users
(
	user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password_hash VARCHAR(255) NOT NULL ,
    email VARCHAR(100) NOT NULL ,
-- role use integer help flexible and can expand more. 
    role INTEGER NOT NULL,		
    subscription_id INT,
    FOREIGN KEY (subscription_id) REFERENCES Subscription(subscription_id) 
);

CREATE TABLE IF NOT EXISTS Subscription
(
	subscription_id INT AUTO_INCREMENT PRIMARY KEY,
    subsciption_type INTEGER NOT NULL,
    start_date datetime,
    end_date datetime
);


CREATE TABLE IF NOT EXISTS Genre
(
	genre_id INT AUTO_INCREMENT PRIMARY KEY,
    genre_name varchar(100) NOT NULL
);
CREATE TABLE IF NOT EXISTS Movie
(
	movie_id INT AUTO_INCREMENT PRIMARY KEY,
    title nvarchar(100) NOT NULL,
	description TEXT,
    genre_id INT NOT NULL,
    rating ENUM('G', 'PG', 'PG-13', 'R', 'NC-17', 'NR'),
    language varchar(20),
    release_year date,
    rental_price DECIMAL(10,2) NOT NULL,
    -- track the number of available copies for rent
    available_copies INT DEFAULT 1 ,
    FOREIGN KEY (genre_id) REFERENCES Genre(genre_id)
);
-- American rating standard 
-- G: General Audiences. Suitable for all ages. Contains no content that could be considered inappropriate for children.
-- PG: Parental Guidance Suggested. Contains material that may not be suitable for young children.
-- PG-13: Parents Strongly Cautioned. May contain material inappropriate for children under 13.
-- R: Restricted. Under 17 requires accompanying parent or adult guardian.
-- NC-17: No One 17 and Under Admitted. Clearly adult content.   
-- NR: Not Rated. The film has not been rated by the Motion Picture Association of America (MPAA).

-- This table tracks movie rentals, including rental and return dates, charges, and fines.
CREATE TABLE IF NOT EXISTS Rental (
    rental_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    movie_id INT,
    rental_date DATE NOT NULL,
    return_date DATE,
    charges DECIMAL(5, 2),
    overdue_fines DECIMAL(5, 2),
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (movie_id) REFERENCES Movie(movie_id)
);
-- return_date (nullable, for tracking returned movies)
-- charges (calculated from rental duration and price)
-- overdue_fines (calculated if the return is late)

CREATE TABLE IF NOT EXISTS Actor (
    actor_id INT AUTO_INCREMENT PRIMARY KEY,
    actor_name VARCHAR(255) NOT NULL
);

-- change M-N Movie and Actor to 1-N thought Movie_Actor
CREATE TABLE IF NOT EXISTS Movie_Actor (
    movie_id INT,
    actor_id INT,
    role VARCHAR(100),
    PRIMARY KEY (movie_id, actor_id),
    FOREIGN KEY (movie_id) REFERENCES Movie(movie_id),
    FOREIGN KEY (actor_id) REFERENCES Actor(actor_id)
);

CREATE TABLE IF NOT EXISTS Payment (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    rental_id INT,
    payment_date DATE NOT NULL,
    amount DECIMAL(5, 2) NOT NULL,
    FOREIGN KEY (rental_id) REFERENCES Rental(rental_id)
);

CREATE TABLE IF NOT EXISTS Customer_Profile (
    profile_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    full_name VARCHAR(255),
    address VARCHAR(255),
    phone_number VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

CREATE TABLE IF NOT EXISTS Login_History (
    login_history_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    login_time DATETIME NOT NULL,
    logout_time DATETIME,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

CREATE TABLE IF NOT EXISTS Review (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    movie_id INT,
    user_id INT,
    review_text TEXT,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    review_date DATE,
    FOREIGN KEY (movie_id) REFERENCES Movie(movie_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

CREATE TABLE IF NOT EXISTS Wishlist (
    wishlist_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    movie_id INT,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (movie_id) REFERENCES Movie(movie_id)
);

CREATE TABLE IF NOT EXISTS Category (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL
);