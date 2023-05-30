
# application.properties
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
spring.datasource.url=jdbc:mariadb:
spring.datasource.username=
spring.datasource.password=
spring.mvc.pathmatch.matching-strategy=ant_path_matcher
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.format_sql=true
#JWT related value
jwt.issuer=
jwt.secretKey=
jwt.tokenPrefix=
#Redis related value
spring.redis.host=
spring.redis.port=
# AWS Account Credentials (AWS ?? ?)
#cloud.aws.credentials.accessKey=
#cloud.aws.credentials.secretKey=

# AWS S3 bucket Info (S3 ????)
cloud.aws.s3.bucket=
cloud.aws.region.static=ap-northeast-2
cloud.aws.stack.auto=false

# file upload max size (?? ??? ?? ??)
spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=100MB

# Send temporary password
#spring.mail.host=smtp.gmail.com
#spring.mail.port=587
#spring.mail.username=
#spring.mail.password=
#spring.mail.default-encoding=utf-8
#spring.mail.properties.mail.smtp.auth=true
#spring.mail.properties.mail.smtp.starttls.enable=true