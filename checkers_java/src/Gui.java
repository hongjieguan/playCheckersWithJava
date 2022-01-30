import java.awt.Color;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;


public class Gui extends JFrame implements MouseListener, MouseMotionListener,ActionListener, Observer {
    JLayeredPane layeredPane;
    static JPanel chessBoard;
    private static JLabel chessPiece;
    int xAdjust;
    int yAdjust;
    int x = 0, y = 0;
    int num = 0;
    public static List<Integer> input = new ArrayList<Integer>();
    //add piece
    static ImageIcon redPawn = new ImageIcon("src/images/redPawnImage.png"); // add an image here
    static ImageIcon whitePawn = new ImageIcon("src/images/whitePawnImage.png");
    static ImageIcon redKing = new ImageIcon("src/images/redKingImage.png");
    static ImageIcon whiteKing = new ImageIcon("src/images/whiteKingImage.png");
    JButton backButton;



    //make the constructor private so that this class cannot be
    //instantiated
    //private GUIBoard(){}

    //Singleton
    private static Gui uniqueInstance;
    public static Gui getInstance(){
        if (uniqueInstance==null){
            uniqueInstance = new Gui();
        }
        return uniqueInstance;
    }


    private Gui() {
        //size of the window
        Dimension boardSize = new Dimension(640, 640);
        // withdraw button
        backButton = new JButton("Back");
        backButton.setBounds(700,50,100,50);
        backButton.addActionListener(this);
        backButton.setFocusable(false);

        //add a layered pane
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(boardSize);
        layeredPane.addMouseListener(this);
        layeredPane.addMouseMotionListener(this);
        getContentPane().add(layeredPane);

        //add background
        chessBoard = new JPanel();
        chessBoard.setLayout(new GridLayout(8, 8));
        chessBoard.setPreferredSize(boardSize);
        chessBoard.setBounds(0, 0, boardSize.width, boardSize.height);
        layeredPane.add(chessBoard, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(backButton, JLayeredPane.DEFAULT_LAYER);

        //draw the background black and white:
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JPanel square = new JPanel(new BorderLayout());
                square.setBackground((i + j) % 2 == 0 ? Color.LIGHT_GRAY : Color.DARK_GRAY);
                chessBoard.add(square);
            }
        }
        //add red pawn:
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 1) {
                    JPanel panel = (JPanel) chessBoard.getComponent(i * 8 + j);
                    JLabel RedPawn = new JLabel(redPawn);
                    panel.add(RedPawn);
                }
            }
        }
        //add white pawn:
        for (int i = 5; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 1) {
                JPanel panel = (JPanel) chessBoard.getComponent(i * 8 + j);
                JLabel WhitePawn = new JLabel(whitePawn);
                panel.add(WhitePawn);
                }
            }
        }
        this.setTitle("Group 19 Checkers Game");
        this.setSize(850, 680);
        this.setBackground(Color.white);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        //this.setVisible(true);
    }

    public void mousePressed(MouseEvent e) {
        input.clear();
        input.add(e.getY()/80);
        input.add(e.getX()/80);
        chessPiece = null;
        Component c = chessBoard.findComponentAt(e.getX(), e.getY());
        if (c instanceof JPanel) return;
        Point parentLocation = c.getParent().getLocation(); //c's parent is the label
        xAdjust = parentLocation.x - e.getX();
        yAdjust = parentLocation.y - e.getY();
        chessPiece = (JLabel) c;
        chessPiece.setLocation(e.getX() + xAdjust, e.getY() + yAdjust);

        layeredPane.add(chessPiece, JLayeredPane.DRAG_LAYER);
        layeredPane.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
    }

    public void mouseDragged(MouseEvent me) {
        if (chessPiece == null) return;

        //  The drag location should be within the bounds of the chess board

        int x = me.getX() + xAdjust;
        int xMax = layeredPane.getWidth() - chessPiece.getWidth();
        x = Math.min(x, xMax);
        x = Math.max(x, 0);

        int y = me.getY() + yAdjust;
        int yMax = layeredPane.getHeight() - chessPiece.getHeight();
        y = Math.min(y, yMax);
        y = Math.max(y, 0);

        chessPiece.setLocation(x, y);
    }

    public void mouseReleased(MouseEvent e){

        layeredPane.setCursor(null);

        if (chessPiece == null) return;

        //  Make sure the chess piece is no longer painted on the layered pane
        chessPiece.setVisible(false);
        layeredPane.remove(chessPiece);
        chessPiece.setVisible(true);
        //  The drop location should be within the bounds of the chess board
        int xMax = layeredPane.getWidth() - chessPiece.getWidth();
        int x = Math.min(e.getX(), xMax);
        x = Math.max(x, 0);

        int yMax = layeredPane.getHeight() - chessPiece.getHeight();
        int y = Math.min(e.getY(), yMax);
        y = Math.max(y, 0);

        Component c = chessBoard.findComponentAt(x, y);

        if (c instanceof JLabel) {
            Container parent = c.getParent();
            parent.remove(0);
            parent.add(chessPiece);
            parent.validate();
        } else {
            Container parent = (Container) c;
            parent.add(chessPiece);
            parent.validate();
        }
        input.add(e.getY()/80);
        input.add(e.getX()/80);
    }


    public void mouseClicked(MouseEvent e) {

//        input.add(e.getY() / 80);
//        input.add(e.getX() / 80);
//
//        System.out.println(ChessBoard.getInput(0));
    }

    @Override
    public void update() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
//                JPanel panel = (JPanel) chessBoard.getComponent(row * 8 + col);
//                panel.removeAll();
                Component c = chessBoard.findComponentAt( col*80, row * 80);
                Container parent;
                if (c instanceof JLabel) {
                    parent = c.getParent();
                    parent.removeAll();
                    parent.validate();
                    parent.repaint();
                } else {
                    parent = (Container) c;
                }
                if (Board.isNull(row, col)) {
                } else if (Board.isRed(row, col) && !Board.isKing(row, col)) {
                    JLabel chessPiece1 = new JLabel(redPawn);
                    parent.add(chessPiece1);
                } else if (Board.isWhite(row, col) && !Board.isKing(row, col)) {
                    JLabel chessPiece2 = new JLabel(whitePawn);
                    parent.add(chessPiece2);
                } else if (Board.isRed(row, col) && Board.isKing(row, col)) {
                    JLabel chessPiece3 = new JLabel(redKing);
                    parent.add(chessPiece3);
                } else if (!Board.isRed(row, col) && Board.isKing(row, col)) {
                    JLabel chessPiece4 = new JLabel(whiteKing);
                    parent.add(chessPiece4);
                }
                parent.validate();
                setVisible(true);
            }
        }
    }

    public static List<Integer> getInput(int player) {
        List<Integer> move = new ArrayList<Integer>();
        if (input.size() == 4) {
            move.addAll(input);
            input.clear();
            move.add(player);
        }
        return move;
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==backButton){
            Board.undo();

        }
    }

//        this.add(PiecePanel);

}




