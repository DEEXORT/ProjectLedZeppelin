# ==========================
# Сборка проекта
# ==========================
# Стадия сборки
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Копирование исходного кода
COPY pom.xml .
COPY src ./src

# Запускаем процесс сборки
RUN mvn clean package -DskipTests=true

# ==========================
# Запуск Tomcat
# ==========================
# Используем образ Tomcat 10
FROM tomcat:10-jdk21-temurin

# Создаем директорию приложения
WORKDIR /app

# Удаляем дефолтные приложения Tomcat (опционально)
RUN rm -rf /usr/local/tomcat/webapps/*

# Копируем WAR файл в папку Tomcat webapps
COPY --from=builder /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Открываем порт, на котором работает приложение
EXPOSE 8080