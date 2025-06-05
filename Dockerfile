# Використовуємо офіційний образ Java 17 з Maven
FROM eclipse-temurin:17-jdk

# Встановлюємо робочу директорію в контейнері
WORKDIR /app

# Копіюємо pom.xml окремо, щоб кешувати залежності
COPY pom.xml .

# Завантажуємо залежності (кешується окремо)
RUN mvn dependency:go-offline

# Копіюємо весь проєкт
COPY . .

# Збираємо проект, пропускаючи тести
RUN mvn package -DskipTests

# Вказуємо команду запуску — запускаємо твою програму з усіма залежностями
CMD ["java", "-cp", "target/classes:target/dependency/*", "org.example.ZooCampBot"]
