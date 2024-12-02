#!/bin/bash
# start.sh

# Khởi động crond
crond

# Đợi MySQL socket
while [ ! -S /var/run/mysqld/mysqld.sock ]; do
  sleep 1
done

# Khởi động MySQL với các tham số cấu hình
exec mysqld --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci
