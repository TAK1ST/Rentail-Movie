FROM mysql:8.0

# Copy sync script
COPY sync/sync-db.sh /sync/
RUN chmod +x /sync/sync-db.sh

# Set character encoding
CMD ["mysqld", "--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci"]