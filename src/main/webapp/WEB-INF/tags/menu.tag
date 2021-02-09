<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!--  >%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%-->
<%@ attribute name="name" required="true" rtexprvalue="true"
	description="Name of the active menu: home, owners, vets or error"%>

<nav
	class="navbar fixed-top navbar-expand-lg navbar-dark scrolling-navbar double-nav"
	role="navigation">

	<!-- SideNav slide-out button
	<div class="float-left">
		<a href="#" data-activates="slide-out" class="button-collapse"> <i
			class="fa fa-bars"></i>
		</a>
	</div>  -->

	<!-- Breadcrumb-->
	<a class="navbar-brand waves-effect pl-3"
		href="<spring:url value="/" htmlEscape="true" />"> <strong>${name}</strong>
	</a>

	<!-- Links -->
	<ul class="nav navbar-nav nav-flex-icons ml-auto">
		<sec:authorize access="!isAuthenticated()">
			<li class="nav-item"><a href="<c:url value="/login" />"
				class="nav-link"> <i class="fa fa-envelope"></i> <span
					class="clearfix d-none d-sm-inline-block">Login</span>
			</a></li>
			<li class="nav-item"><a href="<c:url value="/users/new" />"
				class="nav-link"> <i class="fa fa-envelope"></i> <span
					class="clearfix d-none d-sm-inline-block">Register</span>
			</a></li>
		</sec:authorize>

		<sec:authorize access="isAuthenticated()">
			<li class="nav-item dropdown"><a
				class="nav-link dropdown-toggle" href="#"
				id="navbarDropdownMenuLink" data-toggle="dropdown"
				aria-haspopup="true" aria-expanded="false"> <i
					class="fa fa-user"></i> <span
					class="clearfix d-none d-sm-inline-block"><sec:authentication
							property="name" /></span>
			</a>
				<div class="dropdown-menu dropdown-menu-right"
					aria-labelledby="navbarDropdownMenuLink">
					<a class="dropdown-item" href="#">Profile</a> <a
						class="dropdown-item" href="<c:url value="/logout" />">Logout</a>
				</div></li>
		</sec:authorize>
	</ul>
	<!--/.Navbar-->

	<!-- Sidebar navigation -->
	<div class="sidebar-fixed position-fixed p-0">

		<a class="logo-wrapper waves-effect">
			<h1>MyGym</h1>
		</a>

		<ul>
			<petclinic:menuItem active="${name eq 'error'}" url="/" title="Home">
				<span class="white-text"><i class="fa fa-home fa-fw mr-2"></i>Inicio</span>
			</petclinic:menuItem>
			
			
			<sec:authorize access="isAuthenticated()">
				<petclinic:menuSection active="${name eq 'workout' or name eq 'exercises' or name eq 'trainings'}" title="Ejercicios">
					<petclinic:menuItem active="${name eq 'workout'}" url="/workouts" title="Workouts">
						<i class="fa fa-calendar-alt fa-fw mr-2"></i>Rutinas
					</petclinic:menuItem>
					<sec:authorize access="hasAnyAuthority('admin', 'trainer')">
						<petclinic:menuItem active="${name eq 'trainings'}" url="/trainings" title="Trainings">
							<i class="fa fa-stopwatch fa-fw mr-2"></i>Entrenamientos
						</petclinic:menuItem>
						<petclinic:menuItem active="${name eq 'exercises'}" url="/exercises" title="Exercises">
							<i class="fa fa-dumbbell fa-fw mr-2"></i>Ejercicios
						</petclinic:menuItem>
					</sec:authorize>
				</petclinic:menuSection>
			</sec:authorize>
			
			<sec:authorize access="hasAuthority('admin')">
				<petclinic:menuSection active="${name eq 'products' or name eq 'purchases' or name eq 'sales'}" title="Productos">
					<petclinic:menuItem active="${name eq 'products'}" url="/products" title="Productos">
						<i class="fa fa-clipboard-list fa-fw mr-2"></i>Productos
					</petclinic:menuItem>
					<petclinic:menuItem active="${name eq 'sales'}" url="/sales" title="Ventas">
						<i class="fa fa-shopping-basket fa-fw mr-2"></i>Ventas
					</petclinic:menuItem>
					<petclinic:menuItem active="${name eq 'purchases'}" url="/purchases" title="Compras">
						<i class="fa fa-store fa-fw mr-2"></i>Compras
					</petclinic:menuItem>
				</petclinic:menuSection>
			</sec:authorize>
			<!--<petclinic:menuItem active="${name eq 'owners'}" url="/owners/find" title="Owners">
				<span class="white-text"><i class="fa fa-pie-chart"></i>Owners</span>
			</petclinic:menuItem>
			
			<petclinic:menuItem active="${name eq 'vets'}" url="/vets" title="Vets">
				<span class="white-text"><i class="fa fa-pie-chart"></i>Vets</span>
			</petclinic:menuItem>
			
			<petclinic:menuItem active="${name eq 'error'}" url="/oups" title="Error">
				<span class="white-text"><i class="fa fa-pie-chart"></i>Error</span>
			</petclinic:menuItem>-->
			
			<sec:authorize access="hasAuthority('admin')">
				<petclinic:menuItem active="${name eq 'error'}" url="/usuarios" title="Usuarios">
					<span class="white-text"><i class="fa fa-user fa-fw mr-2"></i>Usuarios</span>
				</petclinic:menuItem>
			</sec:authorize>
			<sec:authorize access="hasAnyAuthority('admin', 'trainer')">
				<petclinic:menuItem active="${name eq 'error'}" url="/salas" title="Salas">
					<span class="white-text"><i class="fa fa-kaaba fa-fw mr-2"></i>Salas</span>
				</petclinic:menuItem>
			</sec:authorize>
			
			<sec:authorize access="isAuthenticated()">
				<petclinic:menuItem active="${name eq 'error'}" url="/actividades" title="Actividades">
					<span class="white-text"><i class="fa fa-running fa-fw mr-2"></i>Actividades</span>
				</petclinic:menuItem>
			</sec:authorize>
		</ul>


	</div>
</nav>
