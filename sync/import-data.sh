#!/bin/bash

# Cấu hình
DB_USER="root"
DB_PASS="1"
DB_NAME="movierentalsystemdb"
LATEST_DUMP="/sync/backup/latest_dump.sql"

# Import dữ liệu
mysql -u $DB_USER -p$DB_PASS $DB_NAME <$LATEST_DUMP

# Ghi log
echo "[$(date)] Data imported from: $LATEST_DUMP" >>/var/log/db-sync.log
