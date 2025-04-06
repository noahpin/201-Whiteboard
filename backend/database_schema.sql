DROP DATABASE IF EXISTS whiteboard201;

CREATE DATABASE whiteboard201;

USE whiteboard201;

CREATE TABLE users (

    userId INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT,
    password VARCHAR(30) NOT NULL,
    email VARCHAR(320) NOT NULL -- Theoretical maximum for an email
);

CREATE TABLE whiteboards (

    whiteboardId INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT,
    userId INTEGER NOT NULL,
    content JSON NOT NULL,
    updatedAt DATETIME,

    FOREIGN KEY (userId) REFERENCES users(userId)
);

CREATE TABLE permissions (

    permissionId INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT,
    permissionLevel INTEGER NOT NULL,
    userId INTEGER,
    whiteboardId INTEGER,

    FOREIGN KEY (userId) REFERENCES users(userId),
    FOREIGN KEY (whiteboardId) REFERENCES whiteboards(whiteboardId)

);


CREATE TABLE messages (
    messageId INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT,
    userId INTEGER,
    whiteboardId INTEGER NOT NULL,
    content VARCHAR(500),
    timestamp DATETIME,

    FOREIGN KEY (userId) REFERENCES users(userId),
    FOREIGN KEY (whiteboardId) REFERENCES whiteboards(whiteboardId)
);

-- Some test statements --

-- Create user1 and user2
INSERT INTO users (userId, password, email) VALUES (1, 'qwerty', 'example1@example.com');
INSERT INTO users (userId, password, email) VALUES (2, 'qwerty2', 'example2@example.com');

-- User1 makes a whiteboard at 1:00 pm on April 6
INSERT INTO whiteboards (whiteboardId, userId, content, updatedAt) VALUES (1, 1, '{"testdata": "dataexample"}', '2025-04-06 13:00:00');

-- User1 shares with whiteboard1 with user2 as editor
INSERT INTO permissions(permissionLevel, userId, whiteboardId) VALUES (2, 2, 1);

-- User 2 sends a message to user1 at 1:05 pm.
INSERT INTO messages (userId, whiteboardId, content, timestamp)VALUES (2, 1, 'How are you?', '2025-04-06 13:05:00');

-- Check our work
SELECT * FROM users;
SELECT * FROM whiteboards;
SELECT * FROM messages;
SELECT * FROM permissions;