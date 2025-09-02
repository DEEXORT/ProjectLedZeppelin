<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <title>Проклятое подземелье</title>
<%--    Fonts--%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/fonts.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Raleway:300italic,400italic,600italic,700italic,800italic,400,300,600,700,800&amp;display=swap">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/ability.scss">

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: "Raleway", serif;
        }

        h1, h2, label {
            color: white;
        }

        a {
            color: #0066cc;
            text-decoration: none;
            transition: color 0.3s;
        }

        input {
            font-family: inherit;
            font-size: inherit;
            padding: 0.5rem;
            border: 1px solid #ddd;
            border-radius: 4px;
        }

        .form-login {
            display: flex;
            flex-direction: column;
            max-width: 600px;
            margin: 10px 0 auto;
            gap: 10px;
            border-radius: 10px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            font-family: 'Segoe UI', system-ui, sans-serif;
        }

        .container {
            display: flex;
            flex-direction: column;
            position: relative;
            align-items: center;
            justify-content: center;
            margin-top: 10vh;
        }

        .quest-container {
            position: relative;
            justify-content: space-around;
            display: flex;
            align-items: center;
            gap: 2rem;
            padding: 1rem;
            max-width: 1200px;
            margin: 10vh auto 0;
        }
        .card {
            background: white;
            border-radius: 15px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            padding: 2rem;
            width: 1000px;
            font-family: 'Arial', sans-serif;
        }

        .end-game-card {
            text-align: center;
        }

        .btn {
            font-family: "Raleway", serif;
            color: white;
            font-size: 16px;
            background: none;
            border: none;
            cursor: pointer;
            margin: 20px auto;
        }

        .btn-menu {
            font-size: 2rem;
        }

        .btn-success {
            position: relative;
            padding: 12px 24px;
            background: #4CAF50;
            color: white;
            border: none;
            border-radius: 6px;
            font-size: 16px;
            cursor: pointer;
            overflow: hidden;
            transition: all 0.3s ease;
        }

        .btn-success:hover {
            animation: pulse 1.5s infinite;
        }

        @keyframes pulse {
            0% {
                box-shadow: 0 0 0 0 rgba(255, 255, 255, 0.7);
            }
            70% {
                box-shadow: 0 0 0 10px rgba(255, 255, 255, 0);
            }
            100% {
                box-shadow: 0 0 0 0 rgba(255, 255, 255, 0);
            }
        }

        .btn-menu:hover {
            color: #dc3545;
        }

        .btn-quest {
            background-color: #014e5d;
            border: none;
            padding: 12px 24px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            margin: 1rem 0;
            border-radius: 8px;
            transition: background-color 0.3s;
        }

        .btn-quest:hover {
            background-color: #00454f;
        }

        .quest-name {
            color: #dc3545;
            font-family: 'Cormorant SC', serif;
            font-size: 58px;
            box-shadow: 0px 0px #000;
            text-shadow: 6px 1px 4px #000;
            filter: blur(0px);
        }

        body {
            background: url("/images/background.png");
        }

        .navbar {
            background-color: rgba(1, 78, 93, 0.9); /* Основной цвет из .btn-quest с прозрачностью */
            padding: 1rem 2rem;
            display: flex;
            justify-content: space-between;
            align-items: center;
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            z-index: 1000;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
            backdrop-filter: blur(5px); /* Эффект размытия фона */
        }

        .navbar-brand {
            font-family: 'Ruslan Display', cursive;
            color: white;
            font-size: 2rem;
            text-decoration: none;
            text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
            transition: all 0.3s ease;
        }

        .navbar-brand:hover {
            color: #dc3545; /* Цвет из .btn-menu:hover */
            text-shadow: 3px 3px 6px rgba(0, 0, 0, 0.7);
        }

        .navbar-nav {
            display: flex;
            gap: 1.5rem;
            list-style: none;
        }

        .nav-item {
            display: flex;
            align-items: center;
        }

        .nav-link {
            font-family: "Raleway", sans-serif;
            color: white;
            font-size: 1.1rem;
            font-weight: 600;
            text-decoration: none;
            padding: 0.5rem 1rem;
            border-radius: 4px;
            transition: all 0.3s ease;
            position: relative;
        }

        .nav-link:hover {
            color: #dc3545;
            background-color: rgba(255, 255, 255, 0.1);
        }

        .nav-link::after {
            content: '';
            position: absolute;
            bottom: 0;
            left: 50%;
            width: 0;
            height: 2px;
            background-color: #dc3545;
            transition: all 0.3s ease;
            transform: translateX(-50%);
        }

        .nav-link:hover::after {
            width: 80%;
        }

        /* Адаптивность */
        @media (max-width: 768px) {
            .navbar {
                flex-direction: column;
                padding: 1rem;
            }

            .navbar-nav {
                margin-top: 1rem;
                flex-wrap: wrap;
                justify-content: center;
            }

            .nav-link {
                padding: 0.5rem;
                font-size: 1rem;
            }
        }

        .stats-container {
            background: rgba(255, 255, 255, 0.9);
            border-radius: 15px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.2);
            padding: 2rem;
            margin: 100px auto 50px;
            max-width: 1200px;
        }

        .stats-title {
            font-family: 'Ruslan Display', cursive;
            color: #014e5d;
            text-align: center;
            font-size: 2.5rem;
            margin-bottom: 1.5rem;
            text-shadow: 2px 2px 4px rgba(0,0,0,0.1);
        }

        .stats-table {
            width: 100%;
            border-collapse: collapse;
            margin: 0 auto;
            font-size: 1rem;
        }

        .stats-table th {
            background-color: #014e5d;
            color: white;
            padding: 12px 15px;
            text-align: left;
            font-weight: 600;
            letter-spacing: 0.5px;
        }

        .stats-table td {
            padding: 12px 15px;
            border-bottom: 1px solid #ddd;
            color: #333;
        }

        .stats-table tr:nth-child(even) {
            background-color: rgba(1, 78, 93, 0.05);
        }

        .stats-table tr:hover {
            background-color: rgba(1, 78, 93, 0.1);
        }

        .stats-table .highlight {
            font-weight: bold;
            color: #dc3545;
        }

        @media (max-width: 768px) {
            .stats-table {
                font-size: 0.9rem;
            }

            .stats-table th,
            .stats-table td {
                padding: 8px 10px;
            }

            .stats-title {
                font-size: 2rem;
            }
        }
    </style>
</head>