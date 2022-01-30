public class Memento {
    private Piece[][] state=new Piece[8][8];

    public Memento(Piece[][] board){
        for (int row = 0 ; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                state[row][col] = board[row][col];
            }
        }
    }

    public Piece[][] getState() {
        return state;
    }

}
