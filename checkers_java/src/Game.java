import java.util.List;

public class Game {
    private static List<Integer> move;

    private static int player = 0;
    public static void switchSide() {
        player=(player+1)%2;
    }

    static String checkSide(int i) {
        return switch (i) {
            case 0 -> "(RED)";
            case 1 -> "(white)";
            default -> "error!";
        };
    }

    public static void setSide(Integer i){player=(i+1)%2;}

    public static int getPlayer(){
        return player;
    }

    public static void init(){
        Board.initBoard();
        // create a GUI (singleton):
        Gui gui = Gui.getInstance();
        gui.setVisible(true);

        //add observer
        Board.attach(gui);

        Board.notifyAllObservers();
        //Board.print(); when the game is first made, we print the game in the terminal
        while (!Board.gameOver()) {
//            move = Input.getInput(player);//get user input
            move = Gui.getInput(player);
            while (move.size()==0){
                move = Gui.getInput(player);
            }
            while (!Board.check(move)){
                //Board.printErrorMessage();
                Board.notifyAllObservers();
                move = Gui.getInput(player);
            }
            Board.execute(move);
            if (Board.ifMultipleJump()){//multiple jump
                move = Gui.getInput(player);
                while (!Board.check(move)){
                    Board.notifyAllObservers();
                    //Board.printErrorMessage(); //you can print the error message here
                    move = Gui.getInput(player);
                }
                Board.execute(move);
            }
            switchSide();
        }
        switchSide();
        System.out.println("Game Over! "+checkSide(player)+" WON!!!");
    }

}
