# playCheckersWithJava
This is my very first time to build a game with java.

![c_pic](https://user-images.githubusercontent.com/49683560/151717417-2b076708-e558-4047-b45e-755174c671b6.png)

Three design patterns, Singleton, Observer and Memento, are used here:

- Singleton: to make sure only one instance of graphic user interface(gui) is generated.

- Observer: the graphic user interface (gui) is a observer watching the "Board" class, while the game are played on a private 2-dimensional arraylist variable named board in the "Board" class.

- Memento: the Memento design pattern here is used to implement the "undo" function. There is a "Memento" class used to record the board. Whenever a move is executed, a new instance of Memento is generated to record the board. The class MementoCaretaker will push this instance into a stack. Whenever the "undo" button is hit, the stack will pop the top memento out and sent the second-to-top momento to the board. 

If you have any advises to improve this game or find some bugs in it, please let me know. 
