import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;



public class Tetris extends Observable {

    private static final int SCORE_ONE_ROW_CLEARED = 100;
    private static final int TETRIS = 800;
    private static final int NB_ROWS_PER_LEVEL = 10;
    private int kolumny;
    private int linie;
    double score = 0;
    private int linieUsuniete;
    private int poziom;
    private double pace=0;
    private Cell[][] field;
    private Tetrimino aktualneTetromino;
    private Tetrimino nastepneTetromino;
    private Pozycja aktualnaPozycja;
    private MainCanvas canvas;
    private Boolean koniecGry;
    private Pozycja initialPozycja = new Pozycja(5, 0);
    long startTime = 0;
    public long displayTimePerFrameMillis = 500;
    private boolean keyTyped;
    private int padding;
    private int totalLines;
    Input input;
    Observer observer;
    boolean gamePaused;

    
    public Tetris(int kolumny, int linie, MainCanvas canvas) throws IOException {
        this.kolumny = kolumny;
        this.linie = linie;
        this.canvas = canvas;
        input = new Input(this);
        resetGame();
        notifyObserver();
    }

    private void resetGame() {

        gamePaused = false;
        koniecGry = false;
        field = new Cell[linie][kolumny];
        for (int i = 0; i < linie; i++) {
            for (int j = 0; j < kolumny; j++) {
                field[i][j] = new Cell();
            }
        }
        score = 0;
        linieUsuniete = 0;
        poziom = 1;
        canvas.init(kolumny, linie);
        totalLines = 0;
        DodajNoweTetromino();
    }

    public void DodajNoweTetromino() {
        aktualnaPozycja = new Pozycja(initialPozycja.x, initialPozycja.y);
        if (nastepneTetromino != null) {
            aktualneTetromino = nastepneTetromino;
        } else {
            aktualneTetromino = Tetriminos.types[(int) Math.round(Math.random() * (Tetriminos.types.length - 1))];

        }
        nastepneTetromino = Tetriminos.types[(int) Math.round(Math.random() * (Tetriminos.types.length - 1))];
        notifyObserver();
    }

    public void rysujPole() {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (!field[i][j].empty) {
                    canvas.drawCell(j, i, field[i][j].type);
                }
            }
        }

    }

    private boolean pelnaLinia(int row) {
        for (int i = 0; i < field[row].length; i++) {
            if (field[row][i].empty == true) {
                return false;
            }
        }
        return true;
    }

    public void rysujAktualneTetro() {
        int nbBlankLines = nbBlankLines(aktualneTetromino);
        int nbBlankCols = nbBlankCols(aktualneTetromino);
        for (int i = nbBlankLines; i < aktualneTetromino.cells.length; i++) {
            for (int j = 0; j < aktualneTetromino.cells[i].length; j++) {
                if (aktualneTetromino.cells[i][j] == 1) {
                    canvas.drawCell(j + aktualnaPozycja.x - nbBlankCols, i + aktualnaPozycja.y - nbBlankLines, aktualneTetromino.type);
                }
            }
        }
    }

    public void fixTetrimino(Tetrimino tetrimino, Pozycja pos) {
        int nbBlankLines = nbBlankLines(tetrimino);
        int nbBlankCols = nbBlankCols(tetrimino);
        for (int i = nbBlankLines; i < tetrimino.cells.length; i++) {
            for (int j = 0; j < tetrimino.cells[i].length; j++) {
                if (tetrimino.cells[i][j] == 1) {
                    field[ i + pos.y - nbBlankLines][j + pos.x - nbBlankCols].empty = false;
                    field[ i + pos.y - nbBlankLines][j + pos.x - nbBlankCols].type = tetrimino.type;
                }
            }
        }
    }

    public int nbBlankLines(Tetrimino tetrimino) {
        int nbBlankLines = 0;
        int nbBlankCells = 0;

        for (byte[] cell : tetrimino.cells) {
            nbBlankCells = 0;
            for (int j = 0; j < cell.length; j++) {
                if (cell[j] == 0) {
                    nbBlankCells++;
                } else {
                    return nbBlankLines;
                }
            }
            if (nbBlankCells == tetrimino.cells.length) {
                nbBlankLines++;
                nbBlankCells = 0;
            }
            if (nbBlankCells != 0) {
                break;
            }
        }
        return nbBlankLines;
    }

    public int nbBlankCols(Tetrimino tetrimino) {
        int nbBlanckCols = 0;
        int nbBlankCells = 0;

        for (int col = 0; col < tetrimino.cells.length; col++) {
            nbBlankCells = 0;
            for (byte[] cell : tetrimino.cells) {
                if (cell[col] != 0) {
                    return nbBlanckCols;
                } else {
                    nbBlankCells++;
                }
            }
            if (nbBlankCells == tetrimino.cells.length) {
                nbBlanckCols++;
            }
        }
        return nbBlanckCols;
    }

    public boolean jezeliTetrominoMozliweDoWyrysowania(Tetrimino tetrimino, Pozycja pos) {
        int nbBlankLines = nbBlankLines(tetrimino);
        int nbBlankCols = nbBlankCols(tetrimino);
        for (int i = nbBlankLines; i < tetrimino.cells.length; i++) {
            for (int j = 0; j < tetrimino.cells[i].length; j++) {

                if ((pos.x < 0 || pos.y < 0)) {
                    return false;
                }

                if (tetrimino.cells[i][j] == 1 && linie <= pos.y + i - nbBlankLines) {
                    return false;
                }

                if (tetrimino.cells[i][j] == 1 && kolumny <= pos.x + j - nbBlankCols) {
                    return false;
                }

                if (tetrimino.cells[i][j] == 1 && field[pos.y + i - nbBlankLines][pos.x + j - nbBlankCols].empty == false) {
                    return false;
                }

            }
        }
        return true;
    }

    public void clearFullLines() {
        List<Integer> lines = new ArrayList();
        for (int i = field.length - 1; i >= 0; i--) {
            if (pelnaLinia(i)) {
                lines.add(0, i);
            }
        }

        if (lines.size() > 0) {
            updateScore(lines.size());
            totalLines += lines.size();
            updateLevel();
            linieUsuniete += lines.size();
        }


        for (Integer i : lines) {
            for (int line = i; line > 0; line--) {
                for (int j = 0; j < field[i].length; j++) {
                    field[line][j].empty = field[line - 1][j].empty;
                    field[line][j].type = field[line - 1][j].type;
                }
            }
        }
    }

    public void updateScore(int nbRowsCleared) {
        if (nbRowsCleared <= 4) {
            score += SCORE_ONE_ROW_CLEARED * nbRowsCleared;
        } else {
            score = TETRIS;
        }
        notifyObserver();
    }

    public void updateLevel() {
        poziom = totalLines / NB_ROWS_PER_LEVEL + 1;
        notifyObserver();
    }

    public Long getGameSpeed() {
        return ((displayTimePerFrameMillis - (poziom * 50)-(long)pace) * 1000000);
    }

    public void enterGameLoop(long now) {
        if (startTime == 0) {
            startTime = now;
        }
        notifyObserver();

        long elapsed = now - startTime;

        if (!gamePaused && (elapsed > getGameSpeed() || keyTyped)) {
            canvas.clearBackground();
            rysujPole();

            if (!jezeliTetrominoMozliweDoWyrysowania(nastepneTetromino, initialPozycja)) {
                koniecGry = true;
            } else if (jezeliTetrominoMozliweDoWyrysowania(aktualneTetromino, new Pozycja(aktualnaPozycja.x + padding, aktualnaPozycja.y + 1))) {
                aktualnaPozycja.x = aktualnaPozycja.x + padding;
                aktualnaPozycja.y = aktualnaPozycja.y + 1;
                rysujAktualneTetro();
            } else if (jezeliTetrominoMozliweDoWyrysowania(aktualneTetromino, new Pozycja(aktualnaPozycja.x, aktualnaPozycja.y + 1))) {
                aktualnaPozycja.y = aktualnaPozycja.y + 1;
                rysujAktualneTetro();
            } else {
                fixTetrimino(aktualneTetromino, aktualnaPozycja);
                clearFullLines();
                canvas.clearBackground();
                rysujPole();
                DodajNoweTetromino();
            }

            if (padding != 0) {
                padding = 0;
            }

            if (keyTyped) {
                keyTyped = false;
            }

            if (koniecGry) {
                notifyObserver();
            }
            startTime = 0;
        }
    }

    public void HandleAction(Akcje akcje) {
        keyTyped = true;
        if (akcje == Akcje.GO_LEFT) {
            padding = -1;

        } else if (akcje == Akcje.GO_RIGHT) {
            padding = 1;
        } else if (akcje == Akcje.ROTATE) {
            Tetrimino t = aktualneTetromino.clone();
            if (jezeliTetrominoMozliweDoWyrysowania(t.rotateClockWise(),
                    new Pozycja(aktualnaPozycja.x, aktualnaPozycja.y))) {
                aktualneTetromino.rotateClockWise();
            }

        } else if (akcje == Akcje.GO_DOWN) {
            while (jezeliTetrominoMozliweDoWyrysowania(aktualneTetromino,
                    new Pozycja(aktualnaPozycja.x, aktualnaPozycja.y + 1))) {
                aktualnaPozycja.y = aktualnaPozycja.y + 1;
            }
        } else if (akcje == Akcje.PAUSE) {
            gamePaused = !gamePaused;


        } else if (akcje == Akcje.RESET) {
            resetGame();

        }
    }

    public void setPace(double pace) {
        this.pace = pace;
    }

    public StatusGry getCurrentState() {
        StatusGry statusGry = new StatusGry();
        statusGry.tetrimino = nastepneTetromino;
        statusGry.linesCleared = linieUsuniete;
        statusGry.score = (int) score;
        statusGry.level = poziom;
        statusGry.gameOver = koniecGry;
        statusGry.gamePaused = gamePaused;
        return statusGry;
    }

    private void notifyObserver() {
        if (observer != null) {
            observer.update(this, null);
        }
    }

    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    class Cell {
        boolean empty = true;
        int type = -1;
    }
}
