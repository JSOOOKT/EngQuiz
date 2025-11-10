const canvas = document.querySelector('canvas')
const c = canvas.getContext('2d')

canvas.width = 1024
canvas.height = 576

// Battle background
const battleBackgroundImage = new Image()
battleBackgroundImage.src = './img/battleBackground.png'
const battleBackground = new Sprite({
  position: {
    x: 0,
    y: 0
  },
  image: battleBackgroundImage
})

let draggle
let warrior
let renderedSprites
let battleAnimationId
let queue
let healsRemaining = 3
let currentAction = null
let quizActive = false

function initBattle() {
  document.querySelector('#userInterface').style.display = 'block'
  document.querySelector('#dialogueBox').style.display = 'none'
  document.querySelector('#quizInterface').style.display = 'none'
  document.querySelector('#enemyHealthBar').style.width = '100%'
  document.querySelector('#playerHealthBar').style.width = '100%'
  document.querySelector('#attacksBox').replaceChildren()

  draggle = new Monster(monsters.Draggle)
  warrior = new Monster(monsters.Warrior)
  renderedSprites = [draggle, warrior]
  queue = []
  healsRemaining = 3
  quizActive = false
  
  updateHealDisplay()

  const playerAttacks = [attacks.Tackle, actions.Heal]
  
  playerAttacks.forEach((attack) => {
    const button = document.createElement('button')
    button.innerHTML = attack.name
    document.querySelector('#attacksBox').append(button)
  })

  document.querySelectorAll('button').forEach((button) => {
    button.addEventListener('click', (e) => {
      if (quizActive) return; // Prevent actions during quiz
      
      let selectedAttack
      
      if (e.currentTarget.innerHTML === 'Tackle') {
        selectedAttack = attacks.Tackle
      } else if (e.currentTarget.innerHTML === 'Heal') {
        selectedAttack = actions.Heal
        if (healsRemaining <= 0) {
          document.querySelector('#dialogueBox').style.display = 'block'
          document.querySelector('#dialogueBox').innerHTML = 'No more heals remaining!'
          return
        }
      }

      // Store the action and start quiz
      currentAction = selectedAttack
      startQuiz()
    })

    button.addEventListener('mouseenter', (e) => {
      if (quizActive) return;
      
      let selectedAttack
      if (e.currentTarget.innerHTML === 'Tackle') {
        selectedAttack = attacks.Tackle
      } else if (e.currentTarget.innerHTML === 'Heal') {
        selectedAttack = actions.Heal
      }
      
      document.querySelector('#attackType').innerHTML = selectedAttack.type
      document.querySelector('#attackType').style.color = selectedAttack.color
    })
  })
}

function startQuiz() {
  quizActive = true
  document.querySelector('#quizInterface').style.display = 'flex'
  document.querySelector('#userInterface').style.display = 'none'
  
  const question = getRandomQuestion()
  displayQuestion(question)
}

function displayQuestion(question) {
  document.querySelector('#quizQuestion').innerHTML = question.question
  document.querySelector('#quizFeedback').style.display = 'none'
  document.querySelector('#nextQuizButton').style.display = 'none'
  
  const optionsContainer = document.querySelector('#quizOptions')
  optionsContainer.innerHTML = ''
  
  question.options.forEach((option, index) => {
    const button = document.createElement('button')
    button.innerHTML = option
    button.style.padding = '15px'
    button.style.fontSize = '14px'
    button.addEventListener('click', () => checkAnswer(index, question))
    optionsContainer.appendChild(button)
  })
}

function checkAnswer(selectedIndex, question) {
  const options = document.querySelectorAll('#quizOptions button')
  const feedback = document.querySelector('#quizFeedback')
  const nextButton = document.querySelector('#nextQuizButton')
  
  // Disable all options
  options.forEach(button => {
    button.disabled = true
  })
  
  // Show correct/incorrect
  if (selectedIndex === question.correct) {
    options[selectedIndex].style.backgroundColor = '#90EE90'
    feedback.innerHTML = `✓ Correct! ${question.explanation}`
    feedback.style.color = 'green'
    
    // Execute the player's action
    executePlayerAction()
  } else {
    options[selectedIndex].style.backgroundColor = '#FFB6C1'
    options[question.correct].style.backgroundColor = '#90EE90'
    feedback.innerHTML = `✗ Incorrect. ${question.explanation}`
    feedback.style.color = 'red'
    
    // Skip to enemy turn
    skipToEnemyTurn()
  }
  
  feedback.style.display = 'block'
  nextButton.style.display = 'block'
  
  nextButton.onclick = () => {
    quizActive = false
    document.querySelector('#quizInterface').style.display = 'none'
    document.querySelector('#userInterface').style.display = 'block'
  }
}

function executePlayerAction() {
  if (currentAction.name === 'Heal') {
    healsRemaining--
    updateHealDisplay()
    warrior.health = Math.min(100, warrior.health - currentAction.damage)
    
    document.querySelector('#dialogueBox').style.display = 'block'
    document.querySelector('#dialogueBox').innerHTML = 
      warrior.name + ' used ' + currentAction.name + '! (+40 HP)'
    
    gsap.to('#playerHealthBar', {
      width: warrior.health + '%'
    })

    gsap.to(warrior, {
      opacity: 0,
      repeat: 3,
      yoyo: true,
      duration: 0.2,
      onComplete: () => {
        queueEnemyAttack()
      }
    })

  } else {
    // Direct Tackle implementation
    document.querySelector('#dialogueBox').style.display = 'block'
    document.querySelector('#dialogueBox').innerHTML = 
      warrior.name + ' used ' + currentAction.name + '!'
    
    // Apply damage directly
    draggle.health -= currentAction.damage
    
    // Show tackle animation
    const tl = gsap.timeline()
    
    tl.to(warrior.position, {
      x: warrior.position.x + 40,
      duration: 0.1
    })
    .to(warrior.position, {
      x: warrior.position.x - 40,
      duration: 0.1,
      onComplete: () => {
        // Show hit effect on enemy
        gsap.to(draggle, {
          opacity: 0,
          repeat: 5,
          yoyo: true,
          duration: 0.08
        })
        
        gsap.to(draggle.position, {
          x: draggle.position.x + 10,
          yoyo: true,
          repeat: 5,
          duration: 0.08
        })
        
        // Update health bar - ONLY ONCE
        gsap.to('#enemyHealthBar', {
          width: draggle.health + '%'
        })
        
        // Play tackle sound
        audio.tackleHit.play()
      }
    })
    .to(warrior.position, {
      x: warrior.position.x,
      duration: 0.1,
      onComplete: () => {
        if (draggle.health <= 0) {
          queue.push(() => {
            draggle.faint()
          })
          queue.push(() => {
            gsap.to('#overlappingDiv', {
              opacity: 1,
              onComplete: () => {
                setTimeout(() => {
                  resetBattle()
                }, 1000)
              }
            })
          })
        } else {
          queueEnemyAttack()
        }
      }
    })
  }
}

function skipToEnemyTurn() {
  document.querySelector('#dialogueBox').style.display = 'block'
  document.querySelector('#dialogueBox').innerHTML = 
    'Wrong answer! ' + warrior.name + "'s turn was skipped."
  queueEnemyAttack()
}

function queueEnemyAttack() {
  const randomAttack = draggle.attacks[Math.floor(Math.random() * draggle.attacks.length)]
  
  queue.push(() => {
    draggle.attack({
      attack: randomAttack,
      recipient: warrior,
      renderedSprites
    })

    if (warrior.health <= 0) {
      queue.push(() => {
        warrior.faint()
      })
      queue.push(() => {
        gsap.to('#overlappingDiv', {
          opacity: 1,
          onComplete: () => {
            setTimeout(() => {
              resetBattle()
            }, 1000)
          }
        })
      })
    }
  })
}

function updateHealDisplay() {
  const existingHealDisplay = document.querySelector('#healDisplay')
  if (existingHealDisplay) {
    existingHealDisplay.remove()
  }
  
  const healDisplay = document.createElement('div')
  healDisplay.id = 'healDisplay'
  healDisplay.innerHTML = `Heals: ${healsRemaining}/3`
  healDisplay.style.cssText = `
    position: absolute;
    top: 380px;
    right: 50px;
    background-color: white;
    padding: 8px 12px;
    border: 2px solid black;
    font-size: 12px;
    font-family: 'Press Start 2P', cursive;
  `
  
  document.querySelector('#userInterface').appendChild(healDisplay)
}

function resetBattle() {
  gsap.to('#overlappingDiv', {
    opacity: 0,
    duration: 0.4,
    onComplete: () => {
      initBattle()
    }
  })
}

function animateBattle() {
  battleAnimationId = window.requestAnimationFrame(animateBattle)
  battleBackground.draw()

  renderedSprites.forEach((sprite) => {
    sprite.draw()
  })
}

// Initialize battle immediately
initBattle()
animateBattle()

document.querySelector('#dialogueBox').addEventListener('click', (e) => {
  if (queue.length > 0 && !quizActive) {
    queue[0]()
    queue.shift()
  } else if (!quizActive) {
    e.currentTarget.style.display = 'none'
  }
})