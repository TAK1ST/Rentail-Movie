#!/bin/bash
# sync-db.sh
timestamp=$(date +%Y%m%d_%H%M%S)
backup_file="/sync/backup/db_backup_$timestamp.sql"

# Backup database
mysqldump -u root -p$MYSQL_ROOT_PASSWORD $MYSQL_DATABASE >$backup_file

# Nén file backup
gzip $backup_file

# Giữ lại 5 file backup gần nhất
ls -t /sync/backup/*.sql.gz | tail -n +6 | xargs -r rm
