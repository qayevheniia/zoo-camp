# Використовуємо офіційний образ з Java 17
FROM eclipse-temurin:17-jdk

# Встановлюємо Maven
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

# Встановлюємо робочу директорію
WORKDIR /app

# Копіюємо проєкт у контейнер
COPY . .

# Збираємо проєкт з пропуском тестів
RUN mvn package -DskipTests

# Запускаємо програму
CMD ["java", "-cp", "target/classes", "org.example.ZooCampBot"]
