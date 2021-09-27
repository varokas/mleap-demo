FROM openjdk:17 AS builder

RUN curl -L -o /opt/sbt-1.5.5.tgz https://github.com/sbt/sbt/releases/download/v1.5.5/sbt-1.5.5.tgz \
    && tar -xvzf /opt/sbt-1.5.5.tgz -C /opt \
    && ln -s /opt/sbt/bin/sbt /usr/bin/sbt \
    && rm /opt/sbt-1.5.5.tgz

COPY build.sbt /lambda/
COPY src /lambda/src/
COPY models /models/
COPY project/plugins.sbt project/build.properties /lambda/project/

WORKDIR /lambda
RUN sbt assembly

FROM public.ecr.aws/lambda/java:11
COPY --from=builder /lambda/target/function.jar ${LAMBDA_TASK_ROOT}/lib/
COPY --from=builder /models/* /models/
CMD ["LambdaHandler::handleRequest"]