![Spirits](https://i.imgur.com/5UgocDY.jpg)
# 
This branch is a separately updated version of Spirits for the MysticEmpire server.

# Changes
Below are a list of changes that set this branch apart from its master.

## Additions
#### Abilities
- DarkBlast
- LightBlast
- Levitation
#### Commands
- Choosing Elements
  - When choosing the 'Spirit' element you'll be prompted to choose a sub-element.
  - When choosing the 'LightSpirit' or 'DarkSpirit' element you'll automatically be given the 'Spirit' element.
#### Methods
- `advanceLocationToDirection` to the `Methods` class which will return a location that updates towards a desired direction.
- `advanceLocationToPoint` to the `Methods` class which will return a location that updates towards a desired point.
- `setVelocity` variant that allows for the manipulation of a `height` parameter.

## Removals
#### Abilities
- (coming soon)
#### Methods
- 

## Edits
#### Abilities
- Alleviate
  - Additional swirl animation.
- Intoxicate
  - Additional swirl animation.
- Possess
  - Updated to use spectator mode instead of teleporting on a loop.
  - New animations.
- Shelter
  - Uses ProjectKorra's method for pushing entities away from the shield.
#### Methods
- Using the Spigot method of spawning particles instead of the ProjectKorra library.
- Simplified the `createItem` methods and made them work together.
- Simplified the `playSpiritParticles` methods and made them work together; changed parameters from `float` to `double`.
- Changed the `setSpiritDescriptionColor` to `getSpiritColor` for simplicity.
