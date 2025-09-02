<jsp:useBean id="player" scope="session" type="com.quest.entity.character.Player"/>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <style>
        .character-card {
            background: white;
            border-radius: 15px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            padding: 25px;
            width: 200px;
            text-align: center;
            margin: 10px;
            font-family: 'Arial', sans-serif;
        }

        .character {
            position: relative;
            display: flex;
            justify-content: center;
            align-items: center;
            margin: auto;
            width: 80px;
            height: 120px;
        }

        .character-health {
            height: 10px;
            background: #e0e0e0;
            border-radius: 5px;
            margin: 10px 0;
            overflow: hidden;
        }

        .character-health-bar {
            height: 100%;
            width: ${player.health/player.maxHealth*100}%;
            background: #e74c3c;
            animation: characterPulse 2s infinite;
        }

        .character-stats {
            margin: 15px 0;
            font-size: 14px;
        }

        @keyframes characterWalk {
            0% { transform: translateY(0) rotate(0deg); }
            100% { transform: translateY(-5px) rotate(5deg); }
        }

        @keyframes characterSwing {
            0% { transform: rotate(10deg); }
            100% { transform: rotate(-10deg); }
        }

        @keyframes characterBounce {
            0% { transform: translateY(0); }
            100% { transform: translateY(-5px); }
        }

        @keyframes characterPulse {
            0% { opacity: 1; }
            50% { opacity: 0.7; }
            100% { opacity: 1; }
        }
    </style>
</head>
<body>
<div class="character-card">
    <h3>${player.name}</h3>

    <div class="character">
        <img src="/images/warrior.png" alt="WARRIOR" style="height: 120px">
    </div>

    <div class="character-health">
        <div class="character-health-bar"></div>
    </div>

    <div class="character-stats">
        <p><strong>Уровень:</strong> ${player.level}</p>
        <p><strong>HP:</strong> ${player.health}/${player.maxHealth}</p>
        <p><strong>Сила атаки:</strong> ${player.attack}</p>
        <p><strong>Опыт:</strong> ${player.experiencePoints}/${player.experienceLevel}</p>
    </div>
</div>
</body>
</html>