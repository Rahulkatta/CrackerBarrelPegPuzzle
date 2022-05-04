
import java.util.*;

class Move {
    
    public int Previous;
    public int done;
    public int next;
    
    public Move(int Previous, int done, int next) {
        this.Previous = Previous;
        this.done = done;
        this.next   = next;
    }
    
    public Move inverted() {
        return new Move(next, done, Previous);
    }
}

class Board {
    
    public int totalPegs;
    public int[] blocks;
    
    public Board(int totalPegs, int[] blocks) {
        this.totalPegs = totalPegs;
        this.blocks = blocks.clone();
    }
    
    public Board(int emptyCell) {
        blocks = new int[15];
        totalPegs = 14;
        for (int i = 0; i < 15; i++) {
            if (i == emptyCell)
                blocks[i] = 0;
            else
                blocks[i] = 1;
        }
    }
    
    public Board move(Move m) {
        if (blocks[m.done] == 1 && blocks[m.Previous] == 1 && blocks[m.next]   == 0)
            return getBoard(m, 1, 0, 0);
        else
            return null;
    }
    
    private Board getBoard(Move m, int x, int y, int z) {
        Board postBoard = new Board(totalPegs-1, blocks.clone());
        postBoard.blocks[m.next] = x;
        postBoard.blocks[m.Previous] = y;
        postBoard.blocks[m.done] = z;
        return postBoard;
    }
}

class StepIterator implements Iterator<Move> {
    
    private Move[] moves;
    private Move inverted;
    private int i;
    
    public StepIterator(Move[] moves) {
        this.moves = moves;
        this.i = 0;
    }
    
    @Override
    public boolean hasNext() {
        return (inverted != null && i == moves.length) || i < moves.length;
    }
    
    @Override
    public Move next() {
        if (inverted == null) {
            Move m = moves[i++];
            inverted = m.inverted();
            return m;
        } else {
            Move result = inverted;
            inverted = null;
            return result;
        }
    }
}

class StepList implements Iterable<Move> {
    
    public static final Move[] moves = {
        new Move(0, 1, 3),
        new Move(0, 2, 5),
        new Move(1, 3, 6),
        new Move(1, 4, 8),
        new Move(2, 4, 7),
        new Move(2, 5, 9),
        new Move(3, 6, 10),
        new Move(3, 7, 12),
        new Move(4, 7, 11),
        new Move(4, 8, 13),
        new Move(5, 8, 12),
        new Move(5, 9, 14),
        new Move(3, 4, 5),
        new Move(6, 7, 8),
        new Move(7, 8, 9),
        new Move(10, 11, 12),
        new Move(11, 12, 13),
        new Move(12, 13, 14)
    };
    
    @Override
    public StepIterator iterator() {
        return new StepIterator(moves);
    }
}

public class CrackerBarrelPegPuzzle {
    
    public static void main(String[] args) {
        go();
        terse();
    }
    
    static void go() {
        for (int i = 0; i < 5; i++) {
            System.out.println("=== " + i + " ===");
            Board b = new Board(i);
            replay(startPuzzle(b), b);
            System.out.println();
        }
    }
    
    static void terse() {
        for (int i = 0; i < 15; i++) {
            Board b = new Board(i);
            printBoard(b);
            List<Move> moves = startPuzzle(b);
            for (Move m : moves) {
                System.out.println(m);
                b = b.move(m);
            }
            printBoard(b);
            System.out.println();
        }
    }
    
    static void replay(List<Move> moves, Board b) {
        show(b);
        for (Move m : moves) {
            b = b.move(m);
            show(b);
        }
    }

    static void show(Board b) {
        int[][] lines = { {4,0,0}, {3,1,2}, {2,3,5}, {1,6,9}, {0,10,14} };
        for (int[] l : lines) {
            String space = new String();
            for (int i = 0; i < l[0]; i++)
                space += " ";
            System.out.print(space);
            for (int i = l[1]; i <= l[2]; i++)
                System.out.print(b.blocks[i] == 0 ? ". " : "x ");
            System.out.println();
        }
        System.out.println();
    }
    
    static StepList steps() {
        return new StepList();
    }
    
    static ArrayList<LinkedList<Move>> solve(Board b) {
        ArrayList<LinkedList<Move>> moves = new ArrayList<LinkedList<Move>>();
        solve(b, moves, 0);
        return moves;
    }
    
    static LinkedList<Move> startPuzzle(Board b) {
        ArrayList<LinkedList<Move>> moves = new ArrayList<LinkedList<Move>>();
        solve(b, moves, 1);
        if (moves.size() != 0)
            return moves.get(0);
        else
            return null;
    }
    
    static void solve(Board b, ArrayList<LinkedList<Move>> sols, int c) {
        if (b.totalPegs != 1) {
            for (Move m : steps()) {
                Board postBoard = b.move(m);
                if (postBoard != null) {
                    ArrayList<LinkedList<Move>> lastSols = new ArrayList<LinkedList<Move>>();
                    solve(postBoard, lastSols, c);
                    for (LinkedList<Move> sol : lastSols) {
                        sol.add(0, m);
                        sols.add(sol);
                        if (sols.size() == c)
                            return;
                    }
                }
            }
        }
        else {
            sols.add(new LinkedList<Move>());
            return;
        }
    }

    static void printBoard(Board b) {
        System.out.print("(" + b.totalPegs + ", [");
        for (int i = 0; i < b.blocks.length; i++)
            if (b.blocks.length-1 > 0)
                System.out.print(b.blocks[i] + ", ");
            else
                System.out.print(b.blocks[i] + "])");
        System.out.println();
    }
    
}
