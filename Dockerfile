FROM maven:3.9-eclipse-temurin-11

# Install Chrome
RUN apt-get update && apt-get install -y \
    wget \
    gnupg \
    unzip \
    && wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google.list \
    && apt-get update \
    && apt-get install -y google-chrome-stable \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn dependency:go-offline

CMD ["mvn", "clean", "test", "-DsuiteXmlFile=src/test/resources/testng/testng.xml"]
