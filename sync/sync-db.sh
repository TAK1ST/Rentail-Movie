#!/bin/sh
echo "Starting backup process..."
mysqldump -u root -p"$MYSQL_ROOT_PASSWORD" "$MYSQL_DATABASE" > /sync/backup/db_backup_$(date +%Y%m%d_%H%M%S).sql
echo "Backup completed"