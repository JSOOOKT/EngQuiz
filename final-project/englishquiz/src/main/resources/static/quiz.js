const quizQuestions = [
  {
    question: "Choose the correct sentence:",
    options: ["She go to school.", "She goes to school.", "She going to school."],
    correct: 1,
    explanation: "Use 'goes' with he/she/it in present simple tense."
  },
  {
    question: "What is the plural of 'child'?",
    options: ["childs", "children", "childes"],
    correct: 1,
    explanation: "The plural of 'child' is 'children' (irregular plural)."
  },
  {
    question: "Which word is a verb?",
    options: ["happy", "run", "beautiful"],
    correct: 1,
    explanation: "'Run' is a verb that shows action."
  },
  {
    question: "Choose the correct punctuation:",
    options: ["What time is it.", "What time is it?", "What time is it"],
    correct: 1,
    explanation: "Questions end with a question mark."
  },
  {
    question: "Which sentence is in the past tense?",
    options: ["I am eating lunch.", "I eat lunch.", "I ate lunch."],
    correct: 2,
    explanation: "'Ate' is the past tense of 'eat'."
  },
  {
    question: "What is the opposite of 'brave'?",
    options: ["strong", "cowardly", "happy"],
    correct: 1,
    explanation: "The opposite of brave is cowardly."
  },
  {
    question: "Which word is spelled correctly?",
    options: ["recieve", "receive", "recieve"],
    correct: 1,
    explanation: "'Receive' follows the 'i before e except after c' rule."
  },
  {
    question: "What is a synonym for 'big'?",
    options: ["small", "large", "tiny"],
    correct: 1,
    explanation: "'Large' means the same as 'big'."
  },
  {
    question: "Which is a complete sentence?",
    options: ["Running in the park.", "The dog barks loudly.", "After school tomorrow."],
    correct: 1,
    explanation: "A complete sentence needs a subject (the dog) and a verb (barks)."
  },
  {
    question: "What is the adjective in: 'The red ball bounced high'?",
    options: ["ball", "red", "bounced"],
    correct: 1,
    explanation: "'Red' describes the ball, so it's an adjective."
  },
  {
    question: "Choose the correct possessive form:",
    options: ["The dogs bone", "The dog's bone", "The dogs' bone"],
    correct: 1,
    explanation: "Use 'dog's' for singular possessive."
  },
  {
    question: "Which word is a noun?",
    options: ["quickly", "happiness", "jump"],
    correct: 1,
    explanation: "'Happiness' is a thing (noun), while 'quickly' is an adverb and 'jump' is a verb."
  }
];

function getRandomQuestion() {
  return quizQuestions[Math.floor(Math.random() * quizQuestions.length)];
}