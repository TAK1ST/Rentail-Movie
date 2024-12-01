FROM mysql:8.0

# Copy SQL script
COPY ./sql/movierentalsystemdb.sql /docker-entrypoint-initdb.d/

# Set character encoding
CMD ["mysqld", "--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci"]
