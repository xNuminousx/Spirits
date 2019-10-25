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
  - When choosing the `Spirit` element you'll be prompted to choose a sub-element.
  - When choosing the `LightSpirit` or `DarkSpirit` element you'll automatically be given the `Spirit` element.
#### Methods
- `advanceLocationToDirection` to the `Methods` class which will return a location that updates towards a desired direction.
- `advanceLocationToPoint` to the `Methods` class which will return a location that updates towards a desired point.
- `setVelocity` variant that allows for the manipulation of a `height` parameter.
#### Classes
- `Removal`: A class that runs basic checks to determine if an ability is able to progress further.

## Removals
#### Abilities
- (coming soon)

## Edits
#### Abilities
*The code in multiple abilities has been reduced/simplified. Some even reworked entirely.*
- Agility
  - Fixed Soar and Dash cooldown applications.
- Rejuvenate
  - Additional swirl animation.
- Infest
  - Additional swirl animation.
- Orb
  - Fixed sourcing and block detection.
- Possess
  - Updated to use spectator mode instead of teleporting on a loop.
  - New animations.
- Shelter
  - Changed to use a custom method of knockback. No longer uses ProjectKorra's method.
#### Methods
- Using the Spigot method of spawning particles instead of the ProjectKorra library.
- Simplified the `createItem` methods and made them work together.
- Simplified the `playSpiritParticles` methods and made them work together; changed parameters from `float` to `double`.
- Changed the `setSpiritDescriptionColor` to `getSpiritColor` for simplicity.
- Removed the `generalChekcs` method and replaced it with the `Removal` class.