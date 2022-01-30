
enum Color {RED, WHITE}

enum State {PAWN, KING}


public class Piece {

    private Color color;
    private State state;
    private int column;
    private int row;
    private int reachBottom;

    public Piece(Color color, int column, int row, State state, int reachBottom) {
        this.color = color;
        this.column = column;
        this.row = row;
        this.state = state;
        this.reachBottom = reachBottom;
    }


    public boolean isRed() {
        return this.color == Color.RED;
    }

    public boolean isWhite() {
        return this.color == Color.WHITE;
    }

    public Color getColor(){
        return this.color;
    }

    public State getState(){
        return this.state;
    }

    public boolean isKing() {
        return this.state == State.KING;
    }

    public boolean canJump(int player) {
        Color color;
        int step;
        if (player == 0) {
            color = Color.RED; step = 1;
        }else{
            color = Color.WHITE; step = -1;
        }
        if (this.color==color) {
            int column = this.column;
            int row = this.row;
            if (this.color==Color.RED) {
                if (this.state == State.PAWN) {
                    try{
                        return (Board.isWhite(row + step, column + 1) && Board.isNull(row + step * 2, column + 2))
                                || (Board.isWhite(row + step, column - 1) && Board.isNull(row + step * 2, column - 2));
                    }catch (ArrayIndexOutOfBoundsException e){
                        return false;
                    }

                } else {//red king
                    try{
                        return (Board.isWhite(row + 1, column + 1) && Board.isNull(row + 2, column + 2))
                                || (Board.isWhite(row + 1, column - 1) && Board.isNull(row + 2, column - 2))
                                || (Board.isWhite(row - 1, column + 1) && Board.isNull(row - 2, column + 2))
                                || (Board.isWhite(row - 1, column - 1) && Board.isNull(row - 2, column - 2));
                    }catch (ArrayIndexOutOfBoundsException e){
                        return false;
                    }
                }
            } else {
                if (this.state == State.PAWN) {
                    try {
                        return (Board.isRed(row + step, column + 1) && Board.isNull(row + step * 2, column + 2))
                                || (Board.isRed(row + step, column - 1) && Board.isNull(row + step * 2, column - 2));
                    }catch (ArrayIndexOutOfBoundsException e){
                        return false;
                    }
                } else {//white king
                    try {
                        return (Board.isRed(row + 1, column + 1) && Board.isNull(row + 2, column + 2))
                                || (Board.isRed(row + 1, column - 1) && Board.isNull(row + 2, column - 2))
                                || (Board.isRed(row - 1, column + 1) && Board.isNull(row - 2, column + 2))
                                || (Board.isRed(row - 1, column - 1) && Board.isNull(row - 2, column - 2));
                    }catch (ArrayIndexOutOfBoundsException e){
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public void  updatePiece(int row, int column) {
        this.row = row;
        this.column = column;
        if ((this.color == Color.RED && this.row == 7) || (this.color == Color.WHITE && this.row == 0)) {
            this.state=State.KING;
            this.reachBottom += 1;
        }
    }
}
