const monsters = {
  Warrior: {
    position: {
      x: 240,
      y: 275
    },
    image: {
      src: './img/Warrior_Idle.png'
    },
    frames: {
      max: 8,
      hold: 15
    },
    animate: true,
    name: 'Warrior',
    attacks: [attacks.Tackle]
  },
  Draggle: {
    position: {
      x: 800,
      y: 100
    },
    image: {
      src: './img/draggleSprite.png'
    },
    frames: {
      max: 4,
      hold: 30
    },
    animate: true,
    isEnemy: true,
    name: 'Draggle',
    attacks: [attacks.Tackle, attacks.Fireball]
  }
}
