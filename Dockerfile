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
RUN mkdir -p /var/run/mysqld && \
    chown -R mysql:mysql /var/run/mysqld && \
    chmod 777 /var/run/mysqld

COPY sync/sync-db.sh /sync/
RUN chmod +x /sync/sync-db.sh

RUN echo "*/2 * * * * /sync/sync-db.sh >> /var/log/cron.log 2>&1" | crontab -

COPY mysql.cnf /etc/mysql/conf.d/

COPY healthcheck.sh /healthcheck.sh
RUN chmod +x /healthcheck.sh

HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD /healthcheck.sh

CMD ["mysqld"]
