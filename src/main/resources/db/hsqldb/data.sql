INSERT INTO user_type VALUES ('admin');
INSERT INTO user_type VALUES ('trainer');
INSERT INTO user_type VALUES ('client');

-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(id,username,password,enabled,type,nombre,apellidos,email,dni,fecha_nacimiento) VALUES (1,'admin1','4dm1n',TRUE, 'admin', 'Administrador', 'Primero', null, null, null);
INSERT INTO users(id,username,password,enabled,type,nombre,apellidos,email,dni,fecha_nacimiento) VALUES (2,'trainer1','trainer1',TRUE, 'trainer', 'Entrenador', 'Primero', null, null, null);
INSERT INTO users(id,username,password,enabled,type,nombre,apellidos,email,dni,fecha_nacimiento) VALUES (3,'client1','client1',TRUE, 'client', 'Cliente', 'Primero', null, null, null);
INSERT INTO users(id,username,password,enabled,type,nombre,apellidos,email,dni,fecha_nacimiento) VALUES (4,'manalerod','1234',TRUE, 'admin', 'Manuel', 'Rodriguez Ales', 'manalerod@alum.us.es', '11111111H', '1990-01-01');
INSERT INTO users(id,username,password,enabled,type,nombre,apellidos,email,dni,fecha_nacimiento) VALUES (5,'manoutbar','manoutbar1234',TRUE, 'client', 'Manuel', 'Outeiriño Barneto', 'manoutbar@alum.us.es', '11111111H', '1990-01-01');
INSERT INTO users(id,username,password,enabled,type,nombre,apellidos,email,dni,fecha_nacimiento) VALUES (6,'borvercas','borvercas123',TRUE, 'admin', 'Borja', 'Vera Casal', 'borvercas@alum.us.es', '11111111H', '1990-01-01');

INSERT INTO sala(id,nombre,aforo) VALUES (1,'Musculación', 10);
INSERT INTO sala(id,nombre,aforo) VALUES (2,'Calistenia', 5);

INSERT INTO actividad(id,nombre,descripcion) VALUES (1,'Zumba', 'Clases dirigidas en la que se realizan ejercicios aeróbicos al ritmo de música latina');

-- exercises types
INSERT INTO exercise_type VALUES (1, 'temporary');
INSERT INTO exercise_type VALUES (2, 'repetitive');

-- products
INSERT INTO products(id, name, description, stockage, price) VALUES (1, 'Barra de chocolate', 'Barra energética para recuperar energía tras un entrenamiento', 10, 2.50);
INSERT INTO products(id, name, description, stockage, price) VALUES (2, 'Batidito de Fresa', 'Batido proteico concentrado al 70%', 20, 3.50);
INSERT INTO products(id, name, description, stockage, price) VALUES (3, 'Anacardos 50gr', 'Anacardos crudos para aportar grasa buena al cuerpo', 10, 3.00);


-- purchase
INSERT INTO purchase(id, date, total, vat) VALUES (1, '2021-01-01', 109.2, 4.0);

-- product purchase
INSERT INTO product_purchase(id, amount, price, product_id, purchase_id) VALUES (1, 10, 2.0, 1, 1);
INSERT INTO product_purchase(id, amount, price, product_id, purchase_id) VALUES (2, 20, 3.0, 2, 1);
INSERT INTO product_purchase(id, amount, price, product_id, purchase_id) VALUES (3, 10, 2.5, 3, 1);


-- user types

-- rate
INSERT INTO rate VALUES (1, 'daily');
INSERT INTO rate VALUES (2, 'monthly');
INSERT INTO rate VALUES (3, 'yearly');

-- execises
INSERT INTO exercise (id,name,description,type,num_reps,is_generic) VALUES (1,'Abdominales concentrados','Abdominales tumbados con rodillas flexionadas tocando la parte de atrás de la cabeza y las puntas de los pies', 2, 50,true);
INSERT INTO exercise (id,name,description,type,num_reps,is_generic) VALUES (2,'Abdominales concentrados de manoutbar','Abdominales tumbados con rodillas flexionadas tocando la parte de atrás de la cabeza y las puntas de los pies', 2, 500, false);

-- trainings
INSERT INTO training (id,name,description,is_generic) VALUES (1,'Circuito metabólico','Circuito de inicicación para trabajar core', true);
INSERT INTO training (id,name,description,is_generic) VALUES (2,'Circuito metabólico de manoutbar','Circuito de inicicación para trabajar core', false);
INSERT INTO training_exercises (training_id, exercise_id) VALUES (1, 1);
INSERT INTO training_exercises (training_id, exercise_id) VALUES (2, 2);

-- workouts
INSERT INTO workout(id,name,description,start_date,end_date,user_id) VALUES (1,'Rutina de iniciación','Toma de contacto con todos los grupos musculares','2021-01-01','2021-01-31',5);

-- workout trainings
INSERT INTO workout_training(id,week_day,training_id,workout_id) VALUES (1,1,2,1);
