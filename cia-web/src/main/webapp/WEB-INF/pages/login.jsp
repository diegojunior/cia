<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>

<head>

    <meta charset="utf-8" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>C.I.A</title>

    <!-- Bootstrap Core CSS -->
    <link href="assets/css/bootstrap.css" rel="stylesheet">

    <!-- MetisMenu CSS -->
    <link href="assets/css/metisMenu.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="assets/css/sb-admin-2.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="assets/css/font-awesome.css" rel="stylesheet" type="text/css">
    
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>

	<div class="container">
		<div class="row">
			<div class="col-md-4 col-md-offset-4">
				<div class="login-panel panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title" align="center" style="height: 70px;">
							<img id="logo" alt="CIA" src="assets/img/logo.png" width="80"
								height="60" />
						</h3>
						<h3 class="panel-title" align="center"
							style="height: 30px; color: #23527c;">C.I.A</h3>
					</div>
					<div class="panel-body">
						<form name="loginForm" action="#" method="post" novalidate>
							<c:if test="${param.error != null}">
                                <div class="alert alert-danger">
                                    <p>Usuário ou senha inválidos.</p>
                                </div>
                            </c:if>
							<c:if test="${param.invalid != null}">
                                <div class="alert alert-danger">
                                    <p>Sessão inválida.</p>
                                </div>
                            </c:if>
							<c:if test="${param.expired != null}">
                                <div class="alert alert-danger">
                                    <p>Sessão expirada.</p>
                                </div>
                            </c:if>
                            <c:if test="${param.logout != null}">
                                <div class="alert alert-success">
                                    <p>Logout efetuado com sucesso!</p>
                                </div>
                            </c:if>
							<fieldset>
								<div class="form-group">
									<input class="form-control" 
										placeholder="Usuário" name="username" type="email" autofocus
										required> 
								</div>
								<div class="form-group">
									<input class="form-control" 
										placeholder="Senha" name="password" type="password" required>
								</div>
								<!-- Change this to a button or input when using this as a form -->
								<button type="submit" id="submit"
									class="btn btn-lg btn-success btn-block"
									style="background-color: rgb(35, 82, 124); border-color: rgb(35, 82, 124);">Entrar</button>
							</fieldset>
						</form>
					</div>
				</div>
			</div>
		</div>
		
	</div>

	<!-- jQuery -->
    <script src="assets/js/lib/jquery.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="assets/js/lib/bootstrap.js"></script>

    <!-- Metis Menu Plugin JavaScript -->
    <script src="assets/js/lib/metisMenu.js"></script>

    <!-- Custom Theme JavaScript -->
    <script src="assets/js/lib/sb-admin-2.js"></script>

</body>

</html>
