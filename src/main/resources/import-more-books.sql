-- Ce script peut être exécuté manuellement via la console H2

-- Insérer plus de livres dans la catégorie Fiction
INSERT INTO books (title, author, isbn, description, condition, price, category_id, seller_id, listed_date, sold, type)
SELECT 
    'Les Misérables', 
    'Victor Hugo', 
    '9782253096344', 
    'Un chef-d''œuvre de la littérature française qui raconte l''histoire de Jean Valjean.',
    'GOOD',
    15.99,
    c.id,
    u.id,
    CURRENT_TIMESTAMP,
    false,
    'PHYSICAL'
FROM categories c, users u
WHERE c.name = 'Fiction' AND u.email = 'admin@bookstore.com';

INSERT INTO books (title, author, isbn, description, condition, price, category_id, seller_id, listed_date, sold, type)
SELECT 
    'L''Étranger', 
    'Albert Camus', 
    '9782070360024', 
    'Un roman existentialiste qui explore l''absurdité de la vie humaine.',
    'VERY_GOOD',
    11.50,
    c.id,
    u.id,
    CURRENT_TIMESTAMP,
    false,
    'PHYSICAL'
FROM categories c, users u
WHERE c.name = 'Fiction' AND u.email = 'user@bookstore.com';

-- Insérer plus de livres dans la catégorie Science-Fiction
INSERT INTO books (title, author, isbn, description, condition, price, category_id, seller_id, listed_date, sold, type)
SELECT 
    'Dune', 
    'Frank Herbert', 
    '9782266233200', 
    'Un chef-d''œuvre de la science-fiction qui se déroule sur la planète désertique Arrakis.',
    'LIKE_NEW',
    16.99,
    c.id,
    u.id,
    CURRENT_TIMESTAMP,
    false,
    'PHYSICAL'
FROM categories c, users u
WHERE c.name = 'Science-Fiction' AND u.email = 'admin@bookstore.com';

INSERT INTO books (title, author, isbn, description, condition, price, category_id, seller_id, listed_date, sold, type)
SELECT 
    'Fondation', 
    'Isaac Asimov', 
    '9782070415700', 
    'Premier tome de la célèbre série Fondation qui raconte l''effondrement et la renaissance d''un empire galactique.',
    'GOOD',
    13.50,
    c.id,
    u.id,
    CURRENT_TIMESTAMP,
    false,
    'PHYSICAL'
FROM categories c, users u
WHERE c.name = 'Science-Fiction' AND u.email = 'user@bookstore.com';

-- Insérer plus de livres dans la catégorie Non-Fiction
INSERT INTO books (title, author, isbn, description, condition, price, category_id, seller_id, listed_date, sold, type)
SELECT 
    'Homo Deus: Une brève histoire du futur', 
    'Yuval Noah Harari', 
    '9782226393876', 
    'Une exploration des possibilités futures de l''humanité à l''ère de la biotechnologie et de l''intelligence artificielle.',
    'NEW',
    19.99,
    c.id,
    u.id,
    CURRENT_TIMESTAMP,
    false,
    'PHYSICAL'
FROM categories c, users u
WHERE c.name = 'Non-Fiction' AND u.email = 'admin@bookstore.com';

-- Insérer plus de livres dans la catégorie Policier
INSERT INTO books (title, author, isbn, description, condition, price, category_id, seller_id, listed_date, sold, type)
SELECT 
    'Le Nom de la Rose', 
    'Umberto Eco', 
    '9782253033134', 
    'Un roman policier médiéval qui se déroule dans une abbaye bénédictine au XIVe siècle.',
    'ACCEPTABLE',
    10.99,
    c.id,
    u.id,
    CURRENT_TIMESTAMP,
    false,
    'PHYSICAL'
FROM categories c, users u
WHERE c.name = 'Policier' AND u.email = 'user@bookstore.com';

INSERT INTO books (title, author, isbn, description, condition, price, category_id, seller_id, listed_date, sold, type)
SELECT 
    'Millénium, Tome 1: Les hommes qui n''aimaient pas les femmes', 
    'Stieg Larsson', 
    '9782742761579', 
    'Premier tome de la série Millénium, un thriller suédois captivant.',
    'GOOD',
    14.99,
    c.id,
    u.id,
    CURRENT_TIMESTAMP,
    false,
    'PHYSICAL'
FROM categories c, users u
WHERE c.name = 'Policier' AND u.email = 'admin@bookstore.com';

-- Insérer plus de livres dans la catégorie Jeunesse
INSERT INTO books (title, author, isbn, description, condition, price, category_id, seller_id, listed_date, sold, type)
SELECT 
    'Le Petit Nicolas', 
    'René Goscinny', 
    '9782070612765', 
    'Les aventures humoristiques d''un petit garçon et de ses camarades de classe.',
    'GOOD',
    8.99,
    c.id,
    u.id,
    CURRENT_TIMESTAMP,
    false,
    'PHYSICAL'
FROM categories c, users u
WHERE c.name = 'Jeunesse' AND u.email = 'user@bookstore.com';

INSERT INTO books (title, author, isbn, description, condition, price, category_id, seller_id, listed_date, sold, type)
SELECT 
    'Matilda', 
    'Roald Dahl', 
    '9782070601585', 
    'L''histoire d''une petite fille extraordinairement intelligente avec des pouvoirs télékinésiques.',
    'VERY_GOOD',
    9.50,
    c.id,
    u.id,
    CURRENT_TIMESTAMP,
    false,
    'PHYSICAL'
FROM categories c, users u
WHERE c.name = 'Jeunesse' AND u.email = 'admin@bookstore.com';

