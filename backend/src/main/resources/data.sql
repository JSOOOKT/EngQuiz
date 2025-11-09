--  USER TABLE
INSERT INTO users (id, username, email, level, totalScore)
VALUES 
(1, 'Alice', 'alice@example.com', 1, 3),
(2, 'Bob', 'bob@example.com', 2, 3),
(3, 'Charlie', 'charlie@example.com', 2,2);

-- GAME CHARACTER TABLE
INSERT INTO GameCharacter (id, name, classType, healthPoints, attackPower, defense, user_id)
VALUES
(1, 'Alicorn', 'Mage', 100, 40, 10, 1),
(2, 'Bobster', 'Warrior', 150, 50, 30, 2),
(3, 'Charlax', 'Archer', 120, 45, 20, 3);

--  QUEST TABLE
INSERT INTO quest (id, questName, description, difficultyLevel, rewardPoints, learningFocus)
VALUES
(1, 'Grammar Journey', 'Learn English tenses through adventures', 'Easy', 100, 'Grammar'),
(2, 'Vocabulary Quest', 'Collect new words and meanings', 'Medium', 150, 'Vocabulary'),
(3, 'Listening Challenge', 'Understand audio clips to find clues', 'Hard', 200, 'Listening');

--  QUIZ TABLE
INSERT INTO quiz (id, title, topic, totalQuestions, questionType, quest_id)
VALUES
(1, 'Present Tense Test', 'Grammar', 10, 'Multiple Choice', 1),
(2, 'Word Match Game', 'Vocabulary', 15, 'Matching', 2),
(3, 'Audio Comprehension', 'Listening', 8, 'True/False', 3);

-- QUESTION TABLE
INSERT INTO question (id, questionText, correctAnswer, quiz_id)
VALUES
-- Questions for Quiz 1: Present Tense Test (Grammar)
(101, 'She ______ to the store every morning.', 'goes', 1),
(102, 'They ______ playing soccer right now.', 'are', 1),
(103, 'I ______ finished my homework yet.', 'haven''t', 1),

-- Questions for Quiz 2: Word Match Game (Vocabulary)
(201, 'Which word means "very cold"?', 'freezing', 2),
(202, 'What is the opposite of "ancient"?', 'modern', 2),

-- Questions for Quiz 3: Audio Comprehension (Listening)
(301, 'The speaker mentioned the weather was sunny. (True/False)', 'True', 3),
(302, 'What time does the train leave?', '9:30 AM', 3);

-- REWARD TABLE
INSERT INTO reward (id, rewardName, pointRequired, user_id)
VALUES
(1, 'Bronze Badge', 200, 1),
(2, 'Silver Badge', 500, 2),
(3, 'Gold Badge', 1000, 3);
