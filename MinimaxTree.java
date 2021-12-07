public class MinimaxTree {
    // Max
    // Min
    private class Node {

        public Move[] moves;
        public Node[] boards;
        public int depth;

        public boolean isWhite;

        public Integer score = null;

        public Node( Board board, Move[] moves, boolean isWhite, int depth, boolean isMaximizing ) {
            this.moves = moves; 
            this.depth = depth;
            this.isWhite = isWhite;
            if ( depth != 0 && !board.isGameOver() ) { 
                boards = new Node[ moves.length ];
                // Generate the new board based on the moves.
                for ( int i = 0; i < moves.length; i++ ) {
                    
                    Move move = moves[i];
                    Board newBoard = board.copy();
                    newBoard.move( move );

                    Move[] nextMoves = getMoves( board, isWhite );
                    

                    boards[i] = new Node( newBoard, nextMoves, !isWhite, depth-1, !isMaximizing );
                    score = (score == null) ? boards[i].score : 
                        isMaximizing ? Math.max( score, boards[i].score ) : Math.min( score, boards[i].score );
                }
                // System.out.println( "Best score for depth [" + depth + "]\t" + score );

            } else {
                score = calcScore( board, isWhite, isMaximizing );
                
            }
        }

    }

    

    private Node root;
    private boolean isMaximizing = true;
    private int searchDepth;
    private Board b;

    public MinimaxTree( Board board, int depth, boolean isWhite ) {
        Board copyBoard = board.copy();
        b = copyBoard;
        searchDepth = depth;
        root = new Node( copyBoard, getMoves( copyBoard, isWhite ), isWhite, depth, true );
    }

    private Move[] getMoves( Board board, boolean isWhite ) {
        Move[] legals = board.legalMoves();
        int[] moves = isWhite ? board.white() : board.black();
        
        int allowed = 0;
        for ( int j = 0; j < legals.length; j++ ) {
            
            for ( int w : moves ) {
                if ( legals[j].from() == w )
                    allowed++;
            }
            
        }
        Move[] onlyAllowed = new Move[allowed];
        for ( int j = 0; j < legals.length; j++ ) {
            
            for ( int w : moves ) {
                if ( legals[j].from() == w )
                    onlyAllowed[--allowed] = legals[j];
            }
            
        }
        return onlyAllowed;
    } 

    private int calcScore( Board board, boolean isWhite, boolean max ) {
        if ( board.isGameOver() ) {
            if ( board.white().length == 0 )
                return max ? ( isWhite ? -10 : 10 ) : ( isWhite ? 10 : -10 );
            else if ( board.black().length == 0 )
                return max ? ( !isWhite ? -10 : 10 ) : ( !isWhite ? 10 : -10 );
            else
                return max ? 6 : -6;
        }
        if ( board.black().length > board.white().length )
            return max ? ( isWhite ? -4 : 4 ) : ( isWhite ? 4 : -4 ); // Maybe the one who has the most pieces left.
        return 1;
    }


    public Move next() {
        Move move = null;
        for ( int i = 0; i < root.boards.length && move == null; i++ ) {
            if ( root.boards[i].score == root.score ) {
                move = root.moves[i];
                b.move( move );
                if ( root.depth < searchDepth / 2 ) {
                    root = new Node( b, getMoves( b, root.boards[i].isWhite), root.boards[i].isWhite, searchDepth, true );
                } else {
                    root = root.boards[i];

                }
                System.out.println( "Chosen score \t" + root.score );
                isMaximizing = !isMaximizing;
                return move;
            }
        }
        return null;
    }
}
