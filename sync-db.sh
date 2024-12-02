#!/bin/bash

# Perform a MySQL database backup using mysqldump
TIMESTAMP=$(date +'%Y%m%d%H%M%S')
BACKUP_DIR="/sync/backup"

# Create backup directory if it doesn't exist
mkdir -p "$BACKUP_DIR"

# Run the MySQL dump
mysqldump -u root -p$MYSQL_ROOT_PASSWORD movierentalsystemdb >"$BACKUP_DIR/movierentalsystemdb_$TIMESTAMP.sql"

# You can add rsync or other sync methods to move the backup to a remote server, if needed
# rsync -avz "$BACKUP_DIR" user@backup-server:/path/to/backup/

echo "Backup completed: movierentalsystemdb_$TIMESTAMP.sql"
