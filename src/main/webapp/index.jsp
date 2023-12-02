<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Добро пожаловать</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 0;
            text-align: center;
        }

        h2 {
            color: #333333;
        }

        p {
            color: #666666;
        }

        ul {
            list-style-type: none;
            padding: 0;
        }

        li {
            display: inline-block;
            margin: 10px;
        }

        a {
            text-decoration: none;
            padding: 10px 20px;
            border: 1px solid #3498db;
            color: #3498db;
            border-radius: 5px;
            transition: background-color 0.3s, color 0.3s;
        }

        a:hover {
            background-color: #3498db;
            color: #ffffff;
        }
    </style>
</head>
<body>
<h2>Добро пожаловать!</h2>
<p>Выберите действие:</p>
<ul>
    <li><a href="Register">Регистрация</a></li>
    <li><a href="Login">Авторизация</a></li>
</ul>
</body>
</html>


