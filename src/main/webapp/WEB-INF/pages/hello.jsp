<html>
<head>


    <link rel='stylesheet' type='text/css' href='${pageContext.request.contextPath}/resources/css/fullcalendar.css'/>
    <link rel='stylesheet' type='text/css' href='${pageContext.request.contextPath}/resources/css/application.css'/>
    <link rel='stylesheet' type='text/css' href='${pageContext.request.contextPath}/resources/css/jquery-ui-1.8.13.custom.css'/>
    <link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/themes/smoothness/jquery-ui.css" />


    <script type='text/javascript' src="${pageContext.request.contextPath}/resources/js/jquery-1.5.1.min.js"></script>
    <script type='text/javascript' src="${pageContext.request.contextPath}/resources/js/jquery-ui.custom.min.js"></script>
    <script type='text/javascript' src="${pageContext.request.contextPath}/resources/js/underscore-min.js"></script>
    <script type='text/javascript' src="${pageContext.request.contextPath}/resources/js/backbone-min.js"></script>
    <script type='text/javascript' src="${pageContext.request.contextPath}/resources/js/application.js"></script>
    <script type='text/javascript' src="${pageContext.request.contextPath}/resources/js/fullcalendar.min.js"></script>



</head>
<body>
<div class="logo"></div>
<div class="title">
    <h1>Sistema di prenotazione Conf room</h1>
</div>
<div id='calendar'></div>
<div id='eventDialog' class='dialog ui-helper-hidden'>
    <form>
        <div>
            <label>Titolo:</label>
            <input id='title' class="field" type="text" maxlength="20"></input>
        </div>
        <div>
            <label>Room:</label>
            <input id='confRoom' class="field" type="text" maxlength="3"></input>
        </div>
        <div>
            <label>Pin:</label>
            <input id='pin' class="field" type="text"></input>
        </div>
        <div>
            <label>Ora Inizio:</label>
            <input id='oraInizio' class="field" type="text" maxlength="5"></input>
        </div>
        <div>
            <label>Ora Fine:</label>
            <input id='oraFine' class="field" type="text" maxlength="5"></input>
        </div>
        <div>
            <label>Email:</label>
            <input id='email' class="field" type="text" ></input>
        </div>
    </form>
</div>
</body>
</html>