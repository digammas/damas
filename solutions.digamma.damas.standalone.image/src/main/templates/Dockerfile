FROM gcr.io/distroless/java:11

COPY ${libs.dir} /app/libs

COPY ${bins.dir} /app/bins

ENTRYPOINT ["java", "-cp", "/app/bins/*:/app/libs/*", "${entry.class}"]