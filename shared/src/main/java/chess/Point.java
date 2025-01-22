package chess;

public class Point {

    private int x;
    private int y;

    public Point() {
        x = 0;
        y = 0;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int value) {
        x = value;
    }

    public int getY() {
        return y;
    }

    public void setY(int value) {
        y = value;
    }


    // Method to move the point in a specific direction
    public Point move(int dx, int dy) {
        return new Point(x + dx, y + dy);
    }

    // Method to check if this point is within the bounds of the chessboard
    public boolean isValid(ChessBoard board) {
        ChessPosition position = new ChessPosition(x, y);
        return board.isValidPosition(position);
    }

    public String toString() {
        return String.format("Point(x = %d, y = %d)", x, y);
    }
}
