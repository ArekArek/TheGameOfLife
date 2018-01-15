package main;

public class LifeBoard implements Runnable {
    private static int THREADS_COUNT = -1;
    private static int THREADS_NUMBER = 0;
    private static int SIZE;
    public static boolean lifeBoard[][][];
    private static boolean T;

    private int id;

    public static void init(int threadsNumber, int size) {
        THREADS_COUNT = threadsNumber;
        SIZE = size;
        lifeBoard = new boolean[2][SIZE][SIZE];
    }

    public LifeBoard() {
        id = THREADS_NUMBER;
        THREADS_NUMBER++;
    }

    @Override
    public void run() {
        if (THREADS_COUNT < 0)
            return;
        for (int i = id; i < SIZE; i = i + THREADS_COUNT) {
            for (int j = 0; j < SIZE; j++) {
                if (T)
                    lifeBoard[0][i][j] = calcState(i, j);
                else
                    lifeBoard[1][i][j] = calcState(i, j);
            }
        }
    }

    public static void setState(int x, int y, boolean value) {
        if (T)
            lifeBoard[1][x][y] = value;
        else
            lifeBoard[0][x][y] = value;
    }

    public static boolean getState(int x, int y) {
        if (T)
            return lifeBoard[1][x][y];
        else
            return lifeBoard[0][x][y];
    }

    public static int getSize() {
        return SIZE;
    }

    private boolean calcState(int x, int y) {
        boolean prevState;
        if (T)
            prevState = lifeBoard[1][x][y];
        else
            prevState = lifeBoard[0][x][y];

        int neighbourSum = calcNeighbourSum(x, y);

        if (!prevState && neighbourSum == 3) {
            return true;
        } else if (prevState && (neighbourSum == 2 || neighbourSum == 3)) {
            return true;
        } else {
            return false;
        }
    }


    private int calcNeighbourSum(int x, int y) {
        int sum = 0;
        int tmpX;
        int tmpY;
        for (int i = -1; i <= 1; i++)
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0)
                    continue;
                tmpX = x + i;
                tmpY = y + j;
                if (isPointInArray(tmpX, tmpY)) {
                    if (T) {
                        if (lifeBoard[1][tmpX][tmpY])
                            sum++;
                    } else {
                        if (lifeBoard[0][tmpX][tmpY])
                            sum++;
                    }
                }

            }
        return sum;
    }

    private boolean isPointInArray(int x, int y) {
        if (x < 0 || x >= SIZE)
            return false;
        else if (y < 0 || y >= SIZE)
            return false;
        else return true;
    }

    public static void changeBoard() {
        T = !T;
    }


    public static void printBoard() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                char tmp;
                if (T)
                    tmp = lifeBoard[0][i][j] ? 'O' : '.';
                else
                    tmp = lifeBoard[1][i][j] ? 'O' : '.';
                sb.append(tmp);
            }
            sb.append("\n");
        }

        System.out.println(sb.toString());
    }

    public static void threadsFinished() {
        THREADS_NUMBER = 0;

//        LifeBoard.printBoard();
        LifeBoard.changeBoard();
    }


}
