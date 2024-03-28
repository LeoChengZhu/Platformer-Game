# My Personal Project

# 2D Platformer Level Creator

## What will the application do?
The application should be able to allow the user to create a blank level that is within a certain size, then to add objects(terrain), a character, a gameScreen and end point then to be able to control the character in the created level. The application should also be able to save and load multiple different levels individually. 

## Who will use it?
This application will be for people interested in creating a simple 2D platformer of their own as well as people that are interested in this genre as well.

## Why is this project of interest to you?
This project is interesting to me as I am interested in the process of game creation, and I think the process of creating something that can be used as a simple game creator can tell me a lot more about game creation that is different from just playing the games. Making the project a level creator also makes it so that I have some more freedom in making the application instead of trying to make something fixed. 

## User Stories:
- As a user, I want to be able to be able to know how to use the application.
- As a user, I want to be able to create levels to work on. 
- As a user, I want to be able to save level to file.
- As a user, I want to be able to load level from file.
- As a user, I want to be able to add different objects that do different things into the level.
- As a user, I want to be able to remove objects that was added into the level.
- As a user, I want to be able to know what every object does when creating the level.
- As a user, I want to be able to view the list of objects that can be used to create the level. 
- As a user, I want to be able to add a playable character into the level. 
- As a user, I want to be able to actually control the character and play the level I created.
- As a user, I want to be able to complete the levels I created.
- As a user, I want to be able to fail the levels I created.
- As a user, I want to be able to view the levels I have created. 

# Instructions for Grader
- You can generate the first required action related to the user story "adding multiple Xs to a Y" by clicking either 
load, or create new world, then after entering the world editing stage, clicking on any of "Tile", 
"Empty", "Spawn", "Death", or "End" button, and then by clicking the visualization of the world, 
will add the corresponding item into the world, adding Xs(Blocks) to a Y(World).
- You can generate the other required actions by having a spawn placed in the world, then pressing
the "Play" button, will create a player that will interact with the previously placed blcoks,
differently. The game is controlled by 'w'(jump), 'a'(move left), 'd'(move right) and 'o' (end game). Player can move,
through "Empty", player will be put back on top of "Spawn" when touching "Death" in any way, you will be put out of 
playing mode if player touches "End", "Tile" and "Spawn" will stop player.
from moving through them.
- To save the current world, click the "Save" button, or the "Quit" button, 
note that the "Quite" button will put you back to the screen where it gives the option
to select "Load" or "New World"
- To load the application, click the load button when the program begins, or quit button to
go back to the beginning screen.
- You can locate my visual component by having a spawn placed in world then clicking play,
the visual element is the model of the player, the exact files can be found in the data folder, where the saves
are located.
