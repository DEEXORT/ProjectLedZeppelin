<jsp:useBean id="monster" scope="session" type="com.quest.entity.character.Monster"/>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <style>
        .monster-card {
            background: white;
            border-radius: 15px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            padding: 25px;
            width: 200px;
            text-align: center;
            margin: 10px;
            font-family: 'Arial', sans-serif;
        }

        .monster {
            position: relative;
            display: flex;
            justify-content: center;
            align-items: center;
            margin: auto;
            width: 80px;
            height: 120px;
        }

        .monster-health {
            height: 10px;
            background: #e0e0e0;
            border-radius: 5px;
            margin: 10px 0;
            overflow: hidden;
        }

        .monster-health-bar {
            height: 100%;
            width: ${monster.health/monster.maxHealth*100}%;
            background: #e74c3c;
            animation: monsterPulse 2s infinite;
        }

        .monster-stats {
            margin: 15px 0;
            font-size: 14px;
        }

        @keyframes monsterWalk {
            0% { transform: translateY(0) rotate(0deg); }
            100% { transform: translateY(-5px) rotate(5deg); }
        }

        @keyframes monsterSwing {
            0% { transform: rotate(10deg); }
            100% { transform: rotate(-10deg); }
        }

        @keyframes monsterBounce {
            0% { transform: translateY(0); }
            100% { transform: translateY(-5px); }
        }

        @keyframes monsterPulse {
            0% { opacity: 1; }
            50% { opacity: 0.7; }
            100% { opacity: 1; }
        }
    </style>
</head>
<body>
<div class="monster-card">
    <h3>${monster.name}</h3>

    <div class="monster">
        <img src="/images/monster2.jpg" alt="MONSTER" style="height: 120px">
    </div>

    <div class="monster-health">
        <div class="monster-health-bar"></div>
    </div>

    <div class="monster-stats">
        <p><strong>Уровень:</strong> ${monster.level}</p>
        <p><strong>HP:</strong> ${monster.health}/${monster.maxHealth}</p>
    </div>
</div>
</body>
</html>