@echo off
echo Starting database backup...
docker exec movie_rental_db bash /sync/sync-db.sh
echo Backup completed
pause