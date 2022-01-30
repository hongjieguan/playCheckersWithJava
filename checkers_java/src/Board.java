import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class Board {
    // log(singleton) is kept to implement the "back" function:
    public static MementoCaretaker mementoCaretaker = new MementoCaretaker();

    private static Piece[][] board = new Piece[8][8];
    private static List<Piece> jump = new ArrayList<Piece>();
    private static String errorMessage;
    private static int canKeepJumping=0;
    private static List<Observer> observerList = new ArrayList<Observer>();

    public static void attach(Observer observer){
        observerList.add(observer);
    }

    public static void notifyAllObservers(){
        for (Observer observer : observerList) {
            observer.update();
        }
    }

    public static boolean isNull(int row, int column) {
        return board[row][column] == null;
    }

    public static boolean isNotNull(int row, int column) {
        return board[row][column] != null;
    }

    public static boolean isRed(int row, int column) {
        try {
            Piece p = board[row][column];
            return p.isRed();
        }catch(NullPointerException e){
            return false;
        }
    }

    public static boolean isWhite(int row, int column) {
        try {
            Piece p = board[row][column];
            return p.isWhite();
        }catch(NullPointerException e){
            return false;
        }
    }

    public static boolean isKing(int row, int column) {
        try {
            Piece p = board[row][column];
            return p.isKing();
        }catch(NullPointerException e){
            return false;
        }
    }


    public static void initBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                if (i % 2 == 0) {
                    if (j % 2 == 1) {
                        Piece redPiece = new Piece(Color.RED, j, i, State.PAWN, 0);
                        board[i][j] = redPiece;
                    } else {
                        board[i][j] = null;
                    }
                } else {
                    if (j % 2 == 0) {
                        Piece redPiece = new Piece(Color.RED, j, i, State.PAWN, 0);
                        board[i][j] = redPiece;
                    } else {
                        board[i][j] = null;
                    }
                }
            }
        }
        for (int i = 3; i < 5; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = null;
            }
        }
        for (int i = 5; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i % 2 == 0) {
                    if (j % 2 == 1) {
                        Piece whitePiece = new Piece(Color.WHITE, j, i, State.PAWN, 0);
                        board[i][j] = whitePiece;
                    } else {
                        board[i][j] = null;
                    }
                } else {
                    if (j % 2 == 0) {
                        Piece whitePiece = new Piece(Color.WHITE, j, i, State.PAWN, 0);
                        board[i][j] = whitePiece;
                    } else {
                        board[i][j] = null;
                    }
                }
            }
        }

        mementoCaretaker.save();
    }

    public static boolean check(List<Integer> move){
        try {
            int inputRow = move.get(0);
            int inputColumn = move.get(1);
            int endRow = move.get(2);
            int endColumn = move.get(3);
            int player = move.get(4);
            if ((Math.abs(endRow - inputRow) == 2) && (Math.abs(endColumn - inputColumn) == 2)) {
                //player has put in a single jump
                if (validJumpInput(inputRow, inputColumn, endRow, endColumn, player)) {
                    return true;
                } else {
                    errorMessage = "Can not Jump this way";
                    return false;
                }
            }
            if ((Math.abs(endRow - inputRow) == 1) && (Math.abs(endColumn - inputColumn) == 1)) {
                //player has put in a single move
                findAllJumps(player);
                if (jump.size() > 0) {
                    errorMessage = "You have a Piece to jump. Please jump it first";
                    return false;
                } else {
                    if (validMoveInput(inputRow, inputColumn, endRow, endColumn, player)) {
                        return true; // go to execute
                    } else {
                        errorMessage = "Your move is not correct";
                        return false;
                    }
                }
            } else {
                //player has put in a random move or multiple jumps
                errorMessage = "Undefined move.If you are doing multiple jumps, please jump step by step.";
                return false;
            }
        }catch(IndexOutOfBoundsException e){
            return false;
        }
    }

    private static void findAllJumps(int player) {
        jump.clear();
        for (Piece[] pieces : board) {
            for (Piece piece : pieces) {
                if (piece != null) {
                    if (piece.canJump(player)) {
                        jump.add(piece);
                    }
                }
            }
        }
    }

    private static boolean validMoveInput(int inputRow, int inputColumn, int endRow, int endColumn,int player) {
        Color color;
        int step;
        if (player == 0) {
            color = Color.RED; step = 1;
        }else{
            color = Color.WHITE; step = -1;
        }
        Piece p = board[inputRow][inputColumn];
        try {
            if (p.getColor() == color) {
                if (p.getState() == State.PAWN) {
                    if (Math.abs(endColumn - inputColumn) == 1 && endRow == inputRow + step && isNull(endRow, endColumn)) {
                        return true;
                    }
                } else if (p.getState() == State.KING) {
                    if (isNull(endRow, endColumn)) {
                        return true;
                    }
                }
            }
        }catch(NullPointerException ignored){}
        return false;
    }

    private static boolean validJumpInput(int inputRow, int inputColumn, int endRow, int endColumn, int player) {
        Color color;
        int step;
        if (player == 0) {
            color = Color.RED; step = 1;
        }else{
            color = Color.WHITE; step = -1;
        }
        int midRow =(inputRow+endRow)/2;
        int midColumn = (inputColumn+endColumn)/2;
        Piece pIn =board[inputRow][inputColumn];
        Piece pMid = board[midRow][midColumn];
        try {
            if (pIn.getColor() == color) { //if the player is using his/her own piece
                if (pIn.getState() == State.PAWN) { //if this piece is pawn
                    return (pMid.getColor() != color && isNull(endRow, endColumn) && endRow == inputRow + 2 * step); //1.if the end point is null. 2.if the midpoint has an enemy piece. 3.if red jumps down and white jumps up
                } else {//KING
                    return (pMid.getColor() != color && isNull(endRow, endColumn));
                }
            }
        }catch(NullPointerException ignored){}
        return false;
    }

    public static void execute(List<Integer>move){
        int inputRow = move.get(0);
        int inputColumn = move.get(1);
        int endRow = move.get(2);
        int endColumn = move.get(3);
        canKeepJumping=0;
        if ((Math.abs(endRow-inputRow)==2)&&(Math.abs(endColumn-inputColumn)==2)) {
            //player has put in a single jump
            int midRow = (inputRow + endRow) / 2;
            int midColumn = (inputColumn + endColumn) / 2;
            board[endRow][endColumn] = board[inputRow][inputColumn];
            board[inputRow][inputColumn] = null;
            board[midRow][midColumn] = null;
            Piece p = board[endRow][endColumn];
            p.updatePiece(endRow, endColumn);
            //check multiple jumps
            if(checkMultiJump(endRow,endColumn)){
                canKeepJumping=1;
            }
        }else{
            //player has put in a single move
            board[endRow][endColumn] = board[inputRow][inputColumn];
            board[inputRow][inputColumn] = null;
            Piece p = board[endRow][endColumn];
            p.updatePiece(endRow, endColumn);
        }
        notifyAllObservers();
        mementoCaretaker.save();

        print();
    }


    public static boolean checkMultiJump(int row, int col) {
        Piece p = board[row][col];
        boolean direction1 = false;
        boolean direction2 = false;
        boolean direction3 = false;
        boolean direction4 = false;
        int step = 0;
        if (p.isRed()) {
            step = 1;
        } else {
            step = -1;
        }
        try {
            if (isNull(row + step*2, col + 2) && isNotNull(row + step, col + 1) && board[row + step][col + 1].getColor() != p.getColor()) {
                direction1 = true;
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        try {
            if (isNull(row + step*2, col - 2) && isNotNull(row + step, col - 1) && board[row + step][col - 1].getColor() != p.getColor()) {
                direction2 = true;
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        if (p.isKing()){
            try {
                if (isNull(row - 2*step, col - 2) && isNotNull(row - step, col - 1) && board[row - step][col - 1].getColor() != p.getColor()) {
                    direction3 = true;
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }

            try {
                if (isNull(row - 2*step, col + 2) && isNotNull(row - step, col + 1) && board[row - step][col + 1].getColor() != p.getColor()) {
                    direction4 = true;
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }
        }
        return (direction1 || direction2 || direction3 || direction4);
    }



    public static boolean ifMultipleJump(){
        if (canKeepJumping==1){
            return true;
        }else{
            return false;
        }
    }


    public static boolean gameOver(){
        int whiteNumber =0;
        int redNumber = 0;
        for (Piece[] pieces : board) {
            for (Piece piece : pieces) {
                if (piece != null) {
                    if(piece.isRed()){
                        redNumber++;
                    }else{whiteNumber++;}
                }
            }
        }
        if (whiteNumber==0||redNumber==0){
            return true;
        }else{
            return false;}
    }


    public static Memento save(){
        return new Memento(board);
    }


    public static void undo(){
        Piece[][] temp;
        temp = mementoCaretaker.sendBoard().getState();
        for (int row = 0 ; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = temp[row][col];
            }
        }
        Game.setSide(mementoCaretaker.getSize());

        notifyAllObservers();
        print();
    }











    //print functions:
    public static void printErrorMessage(){
        System.out.println(errorMessage);
    }

    static String message(int i) {
        return switch (i) {


            case 7 -> "You can only move the same piece during a multiple jump";
            case 8 -> "Please continue to jump this piece";
            default -> "error";
        };
    }

    public static void printMessage(int i){
        System.out.println(message(i));
    }

    static String pieceToString(int row, int col) {
        if(isNull(row,col)){return "[   ] ";}
        else{
            Piece p = board[row][col];
            if (p.isRed()   && !p.isKing()) {return "[R_P] ";}
            if (p.isWhite() && !p.isKing()) {return "[W_P] ";}
            if (p.isRed()   && p.isKing()) {return "[R_K] ";}
            else {return "[W_K] ";}
        }
    }

    public static void print(){
        System.out.print("      a     b     c     d     e     f     g     h      \n" +
                "  +-------------------------------------------------+  \n");
        for (int row = 0; row < 8; row++) {
            System.out.print((8 - row) + " | ");
            for (int col = 0; col < 8; col++) {
                System.out.print(pieceToString(row,col));
                }
            System.out.println("| " + (8 -row));
        }
        System.out.print("  +-------------------------------------------------+  \n" +
                "      a     b     c     d     e     f     g     h      \n");
    }



    //test functions:
    public static void testMove() {

        Piece p1 = new Piece(Color.RED, 5, 0, State.PAWN, 0);
        Piece p2 = new Piece(Color.RED, 2, 1, State.PAWN, 0);
        Piece p3 = new Piece(Color.RED, 4, 1, State.PAWN, 0);
        Piece p4 = new Piece(Color.RED, 1, 6, State.PAWN, 0);
        Piece p5 = new Piece(Color.RED, 4, 7, State.KING, 1);
        Piece p6 = new Piece(Color.RED, 2, 3, State.PAWN, 0);

        Piece p7 = new Piece(Color.WHITE, 3, 0, State.KING, 1);
        Piece p8 = new Piece(Color.WHITE, 3, 6, State.PAWN, 0);
        Piece p9 = new Piece(Color.WHITE, 5, 6, State.PAWN, 0);
        Piece p10 = new Piece(Color.WHITE, 2, 7, State.PAWN, 0);
        Piece p11 = new Piece(Color.WHITE, 6, 1, State.PAWN, 0);

        board[0][5] = p1;
        board[1][2] = p2;
        board[1][4] = p3;
        board[6][1] = p4;
        board[7][4] = p5;
        board[3][2] = p6;
        board[0][3] = p7;
        board[6][3] = p8;
        board[6][5] = p9;
        board[7][2] = p10;
        board[1][6] = p11;
    }

    public static void boardClear() {
        for (Piece[] pieces : board) {
            Arrays.fill(pieces, null);
        }

    }

}
