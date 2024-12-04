FROM mysql:8.0

USER root

RUN microdnf update && microdnf install -y \
    dnf-utils \
    && microdnf clean all

RUN microdnf install -y \
    cronie \
    rsync \
    && microdnf clean all

RUN mkdir -p /sync/backup
RUN mkdir -p /var/log && touch /var/log/db-sync.log
COPY sync/auto-sync.sh /sync/
COPY sync/import-data.sh /sync/
RUN chmod +x /sync/auto-sync.sh /sync/import-data.sh


RUN mkdir -p /var/run/mysqld && \


    chown -R mysql:mysql /var/run/mysqld && \
    chmod 777 /var/run/mysqld

COPY sync/sync-db.sh /sync/
RUN chmod +x /sync/sync-db.sh

RUN echo "*/1 * * * * /sync/auto-sync.sh" | crontab -

COPY mysql.cnf /etc/mysql/conf.d/

COPY healthcheck.sh /healthcheck.sh
RUN chmod +x /healthcheck.sh

HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD /healthcheck.sh

CMD crond && mysqld
