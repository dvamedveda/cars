[![Build Status](https://travis-ci.com/dvamedveda/cars.svg?branch=master)](https://travis-ci.com/dvamedveda/cars)
[![codecov](https://codecov.io/gh/dvamedveda/cars/branch/master/graph/badge.svg?token=EEEWU0TEIR)](https://codecov.io/gh/dvamedveda/cars)

# Car adverts board

MVC Java web application realizing car adverts board.
This application developed with layered architecture:
- persistance-layer
- service-layer
- controller-layer
- view-layer

This application have tests for each layer, including controller layer.

### Application features:

- you can register in application as user and use register credentials to authenticate;
- user can add advert that need to sale on special page, by specifying advert details and hit "Submit" button;
- application can show to user adverts with various filter modes, including realtime search by brand name;
- user can edit his own adverts;
- user can choose for advert publish\close options while editing, to hide an advert from list on main page;
- user can view details about any advert in small modal window;
- user can add few photos to his own advert;
- user can log out from application;
- application validates creating advert;
- user cannot edit other user's task, but can view it's details;
- application supports automatic refreshing of adverts list with chosen filter mode;  
- web app automatically update/check database state on startup.

### Application screenshots:
- register page shows as
![reg](https://github.com/dvamedveda/screenshots/blob/main/cars/register.png?raw=true) 
![reg1](https://github.com/dvamedveda/screenshots/blob/main/cars/register1.png?raw=true) 
- auth page shows as
![auth](https://github.com/dvamedveda/screenshots/blob/main/cars/auth.png?raw=true)
- ads shows both for logged\not logged user
![board](https://github.com/dvamedveda/screenshots/blob/main/cars/board.png?raw=true)
![notlogged](https://github.com/dvamedveda/screenshots/blob/main/cars/not_logged.png?raw=true)
- ad details shows as
![details](https://github.com/dvamedveda/screenshots/blob/main/cars/details.png?raw=true)
- editing ad shows as
![ad_edit1](https://github.com/dvamedveda/screenshots/blob/main/cars/edit_ad.png?raw=true)
![ad_edit2](https://github.com/dvamedveda/screenshots/blob/main/cars/edit_existing.png?raw=true)
- filtering shows as
![filter](https://github.com/dvamedveda/screenshots/blob/main/cars/filter.png?raw=true)

### Used frameworks, libs, technologies:
- Java, Servlets, Maven, JUnit, JSP, JSTL, Log4j, Mockito, PowerMock, Jackson
- JS, jQuery, UI Kit
- Hibernate/JPA, Postgresql, in-memory HSQL, JDBC, Flyway
