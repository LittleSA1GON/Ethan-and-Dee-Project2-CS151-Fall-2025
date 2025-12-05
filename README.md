Blackjack and Snake using JavaFX

Overview:

For CS 151 Project 2, our group, Ethan and Dee, was tasked with creating a game manager with JavaFX. Specifically, we were tasked with creating two functioning games, blackjack and snake, while also incorporating a login and scoreboard. In addition, we included the functionality of encryption, file saving, and music playing during our project.

—------------------------------------------—------------------------------------------

Design:

To begin, our team decided to use Maven for easier running of our JavaFX project, including a pom.xml file. Holistically, our project's source folder is split up into three main packages: gamemanager, snake, and blackjack.

Within gamemanager, files that control the initial login and the main menu are present, such as LoginScreen and MainMenuScreen. In addition, files that all scenes are present in gamemanger, including ToolBarC (which is a toolbar used in every scene except the login), MusicManager, and FileManager. Lastly, for the scoreboard, a ScoreRow and UserScore objects are also found. Particularly, FileManager allows the saving of a Blackjack game at any point in the game, encrypted by the current user.

Within snake, the main game logic can be found in SnakeGame, allowing the user to make actions with the arrow keys. In addition, small classes, such as Direction, Food, and SnakeBody are also found to streamline the game logic and flow of the game.

Within blackjack, the main files are found in the gamelogic files, where BlackJackApp handles the javaFX UI, and BlackJackGame handles the game logic. There is also an abstract Player class, that Computer, Dealer, and Human all inheret from. A Gambler interface is present as well to differentiate the abilites of Computer and Human from Dealer. Lastly, supportingfiles includes a Card, that can be created and shuffled into a stack.

Finally, we also have a folder for txtfiles, to house all created save files, resources, that holds all the images and music we included, and test, which houses our JUnit files.

—------------------------------------------—------------------------------------------

Installation Instructions:

#1) Clone:  git clone : 

https://github.com/LittleSA1GON/Ethan-and-Dee-Project2-CS151-Fall-2025.git

#2) cd <your-repo>

#3) Compile the .java files into bytecode
E.g:
javac app/*.java 
java app.Main

#4) Make sure you have an up-to-date version of JavaFX installed

#5) Run "mvn clean javafx:run" in a new terminal

#6) Run "mvn test" in a new terminal for JUnit tests


—------------------------------------------—------------------------------------------

Usage:

Initially, the user will prompted to either login or create an account. Once an account exists, either previously or currently created, the user will be able to log in into the main menu. There, they will be able to look at the scoreboard, and play either blackjack or snake.

With snake, the user move the snake head around with arrow key, collecting fruits. Each time fruit is eaten, the snake grows larger. If the snake hits the wall or itself, the game ends.

With blackjack, the user can either load a previous game or play a new game, initiated with 2 new computers. After placing a bet from 10 to 500, every player is dealt 2 cards, with the dealer having 1 card hidden. Each gambling player may hit, draw a card, or stand, stop drawing, until the reach 21. If a score of 21 is passed, then that player loses, or busts. Once all gambling players either stand or bust, the dealer draws until they either reach 17 or busts. If a gambling player has a higher score or the dealer busts, they double their bet. If they match with the dealer, a push occurs, and the money is returned. Play until the human player is bankrupt.

Finally, the user may log out or delete their current account.

—------------------------------------------—------------------------------------------

Team Contributions:

—Ethan—
- Implemented functional blackjack game
- Created FileManager encryption using SHA-256 and UTF-8
- Made MusicManager, ensuring music is playing at every stage
- Added logout, delete account, and back buttons
- Fixed scoring implementation


—Dee—
- **Functional Snake Game**
    Complete movement, collsion handling, food spawning, game loop and UI integration

-  **BackgroundSetUp (Abstract Class)**
    Provides consistent background image setup, dim overlay and styling shared across multiple screens

-   **UI screens**
    Implemented full layout, styling and interaction logic for: 
    - `FirstScreen`  
    - `CreateAccountScreen`  
    - `LoginScreen`  
    - `MainMenuScreen`  
    - `ToolBarC`

- **GameManger**
    Manages screen initialization and navigation, linking all screens together:
    `FirstScreen -> Login/Create Account -> Main Menu -> Snake Game/Blackjack`

—------------------------------------------—------------------------------------------

### Project Demo Video
[![YouTube Video](https://img.youtube.com/vi/Q6W0qv5xLw0/0.jpg)](https://youtu.be/Q6W0qv5xLw0)