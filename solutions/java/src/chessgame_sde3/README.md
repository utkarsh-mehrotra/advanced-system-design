# ♟️ Chess Game — SDE3 Upgraded

## Overview
A two-player chess engine with decoupled board state and rules logic. The `GameEngine` handles move validation, ray-casting path obstruction detection for sliding pieces, and check/checkmate detection via mathematical rollback without modifying the live board.

## SDE3 Upgrades Applied

| Issue | Fix |
|-------|-----|
| Board class contained both state AND rules — God Object | Board manages 2D state only; `GameEngine` owns all rules, validation, and check logic |
| Piece movement checked without verifying path obstruction | Ray-casting in `Board.isValidPath()` detects blocking pieces for Rooks, Bishops, Queens |
| Checkmate detection required full board copy | Simulated king-move rollback: apply → test → unapply without a clone |

## Class Diagram

```mermaid
classDiagram
    class GameEngine {
        -Board board
        -Player whitePlayer
        -Player blackPlayer
        -GameState gameState
        +makeMove(Move) boolean
        +isInCheck(Color) boolean
        +isCheckmate(Color) boolean
        -isKingInDangerAfterMove(Move, Color) boolean
    }
    class Board {
        -Piece[][] grid
        -ReadWriteLock boardLock
        +getPiece(int row, int col) Piece
        +placePiece(Piece, int row, int col)
        +isValidPath(from, to) boolean
    }
    class Piece {
        <<abstract>>
        -Color color
        -int row, col
        +canMove(Board, int toRow, int toCol) boolean
        +isSlidingPiece() boolean
    }
    class King
    class Queen
    class Rook
    class Bishop
    class Knight
    class Pawn
    class Move {
        -Piece piece
        -int fromRow, fromCol
        -int toRow, toCol
        -Piece capturedPiece
    }

    GameEngine --> Board
    Board "1" *-- "many" Piece
    Piece <|-- King
    Piece <|-- Queen
    Piece <|-- Rook
    Piece <|-- Bishop
    Piece <|-- Knight
    Piece <|-- Pawn
    GameEngine ..> Move
```

## Run
```bash
javac $(find chessgame_upgraded -name "*.java")
java chessgame_upgraded.ChessGameDemoUpgraded
```
