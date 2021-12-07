public class Minimax {
    private static MinimaxTree tree = null;
    public static Move nextMove( Board board, int depth, boolean isWhite ) {
        if ( tree == null ) 
            tree = new MinimaxTree( board, depth, isWhite );

        return tree.next();
    }
}