#!/bin/bash

# Cấu hình
SOURCE_HOST="localhost"
SOURCE_PORT="3306"
SOURCE_USER="root"
SOURCE_PASS="1"
DB_NAME="movierentalsystemdb"
BACKUP_DIR="/sync/backup"
LATEST_DUMP="/sync/backup/latest_dump.sql"

# Tạo thư mục backup nếu chưa tồn tại
mkdir -p $BACKUP_DIR

# Tạo backup với timestamp
timestamp=$(date +%Y%m%d_%H%M%S)
backup_file="$BACKUP_DIR/db_backup_$timestamp.sql"

# Thực hiện backup
mysqldump -h $SOURCE_HOST -P $SOURCE_PORT -u $SOURCE_USER -p$SOURCE_PASS $DB_NAME >$backup_file

# Tạo symbolic link đến backup mới nhất
ln -sf $backup_file $LATEST_DUMP

# Nén file backup
gzip -f $backup_file

# Giữ lại 5 bản backup gần nhất
ls -t $BACKUP_DIR/db_backup_*.sql.gz | tail -n +6 | xargs -r rm

# Ghi log
echo "[$(date)] Backup created: $backup_file.gz" >>/var/log/db-sync.log
