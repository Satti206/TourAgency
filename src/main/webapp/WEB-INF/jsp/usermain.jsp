<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Добро пожаловать!</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 0;
            text-align: center;
        }

        h1, h2, h3 {
            color: #333333;
        }

        ul {
            list-style-type: none;
            padding: 0;
        }

        li {
            margin-bottom: 10px;
            color: #666666;
        }

        form {
            max-width: 300px;
            margin: 20px auto;
            padding: 20px;
            background-color: #ffffff;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        input {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            box-sizing: border-box;
            border: 1px solid #cccccc;
            border-radius: 5px;
        }

        input[type="submit"] {
            background-color: #3498db;
            color: #ffffff;
            cursor: pointer;
        }

        input[type="submit"]:hover {
            background-color: #1e87d8;
        }
    </style>
</head>
<body>
<header style="background-color: #3498db; padding: 20px;">
    <h1 style="color: #ffffff">Добро пожаловать на наш сайт бронирования туров!</h1>
</header>

<section>
    <h2>Выбирайте и отправляйтесь в незабываемое приключение:</h2>
    <ul>
        <c:forEach var="tour" items="${tours}">
            <li>
                    ${tour.name} - ${tour.description} - ${tour.price}
                <c:url var="profileUrl" value="/Profile">
                    <c:param name="userId" value="${tour.user.userId}" />
                </c:url>
            </li>
        </c:forEach>
        <li><a href="Profile">Мой профиль</a></li>

    </ul>
</section>


</body>
</html>