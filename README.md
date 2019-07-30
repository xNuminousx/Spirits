![Spirits](https://i.imgur.com/5UgocDY.jpg)
# 
This project will add in the new element "Spirit" as well as a DarkSpirit and LightSpirit element to go with it! This is a WIP so more things to come very soon!

# Contributing
If you wish to contribute to this project, please do so in the "update" branch, thanks for the help!

Don't know how to code? Well, you can also contribute by downloading the project, installing it onto your server, and making sure it works correctly! If you find a problem please create a new issue in the Issues tab above. Please include the following information:
- Minecraft version
- Spigot version
- ProjectKorra version
- Error message in the console (if applicable)
- How to recreate the problem

# Requirements
This is a list of requirements for the Spirits add-on:
- ProjectKorra version 1.8.3+
- Spigot version 1.12+

# Spirits API
Spirits comes with an ever growing API which developers can manipulate for their Spirit addons! Here will be an explanation of what individual aspects of the API means so that you can use them in the most effective manner!

A lot of the customizable aspects of this API are done by doing "Methods." in your IDE. After entering the period, all of the things you're able to manipulate will pop up. You must have Spirits.jar in your library to use any of the API!

- Elements - To create an ability that classifies in any of the spirit elements, you must have your project extend one of the following: SpiritAbility, LightAbility, DarkAbility. When your ability is loaded, one of the following will decide which element your ability goes under.

- createPolygon - This API comes with a method which spawns a polygon without needing to know the math! You can create any form of polygon (Ex: Triangle, Square, Pentagon, Hexagon, Septagon, etc). Additionally, you can classify the radius, height, and particle effect type.

- createRotatingCircle - A polygon isn't all you can make! You can also create a rotating circle with this API! Skip the math! You're able to determine the size, speed at which it rotates, height where it spawns, and particle effect type of this circle.

- getSpiritType - Using this, you're able to get what type of spirit a certain target is. After getting their spirit type, you're able to run certain lines of code that will only happen if YOUR conditions are met!

- playSpiritParticles - By using this, you will be able to play the spirit particles determined by the developers! If you don't feel like designing your own, this is a good back up! There are two methods that go along with this. One will player a different particle type depending on the type of spirit that the player who is attached to this method is. With the second method, you're able to play a specific spirit particle at a location.

- setPlayerVelocity - This is a method you can use when you're wanting to manipulate how a player is moving. You can shoot them forwards or backwards and also determine how quickly they are shot.

- setSpiritDescriptionColor - Spirits, by default, has a unique description format. There are 2 methods that you can use to achieve a very similar format for your ability. By using this one, you're able to change the color of your description to the Spirits theme color by defining what type of ability your describing.

- setSpiritDescription - Along with editing the color format, you're also able to define the ability type. This will appear at the beginning of your ability description in bold.
