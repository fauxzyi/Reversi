package reversi;

import java.util.Random;

public class ReversiController implements IController
{
    IModel model;
    IView view;

    Random rand = new Random();

    @Override
    public void initialise(IModel model, IView view)
    {
        this.model = model;
        this.view = view;
    }

    @Override
    public void startup()
    {
        int width = model.getBoardWidth();
        int height = model.getBoardHeight();

        model.clear(IModel.PLAYER_NONE);

        int cx = width / 2;
        int cy = height / 2;
        model.setBoardContents(cx - 1, cy - 1, IModel.PLAYER_WHITE);
        model.setBoardContents(cx,     cy - 1, IModel.PLAYER_BLACK);
        model.setBoardContents(cx - 1, cy,     IModel.PLAYER_BLACK);
        model.setBoardContents(cx,     cy,     IModel.PLAYER_WHITE);

        model.setPlayer(IModel.PLAYER_WHITE);
        model.setFinished(false);

        view.refreshView();
        update();
    }

    @Override
    public void update()
    {
        boolean whiteCanMove = canPlayerMove(IModel.PLAYER_WHITE);
        boolean blackCanMove = canPlayerMove(IModel.PLAYER_BLACK);

        int currentPlayer = model.getPlayer();

        if (!whiteCanMove && !blackCanMove)
        {
            endGame();
            return;
        }

        if (currentPlayer == IModel.PLAYER_WHITE && !whiteCanMove)
        {
            model.setPlayer(IModel.PLAYER_BLACK);
            currentPlayer = IModel.PLAYER_BLACK;
        }
        else if (currentPlayer == IModel.PLAYER_BLACK && !blackCanMove)
        {
            model.setPlayer(IModel.PLAYER_WHITE);
            currentPlayer = IModel.PLAYER_WHITE;
        }

        model.setFinished(false);

        if (currentPlayer == IModel.PLAYER_WHITE)
        {
            view.feedbackToUser(IModel.PLAYER_WHITE, "White player - choose where to put your piece");
            view.feedbackToUser(IModel.PLAYER_BLACK, "Black player - not your turn");
        }
        else
        {
            view.feedbackToUser(IModel.PLAYER_WHITE, "White player - not your turn");
            view.feedbackToUser(IModel.PLAYER_BLACK, "Black player - choose where to put your piece");
        }

        view.refreshView();
    }

    @Override
    public void squareSelected(int player, int x, int y)
    {
        if (model.hasFinished())
        {
            view.feedbackToUser(player, "The game has finished. Press Restart to play again.");
            return;
        }

        if (model.getPlayer() != player)
        {
            view.feedbackToUser(player, "It is not your turn!");
            return;
        }

        if (model.getBoardContents(x, y) != IModel.PLAYER_NONE || countCaptures(player, x, y) == 0)
        {
            view.feedbackToUser(player, "Invalid location to play a piece.");
            return;
        }

        model.setBoardContents(x, y, player);

        doCaptures(player, x, y);

        model.setPlayer(opponent(player));

        update();
    }

    @Override
    public void doAutomatedMove(int player)
    {
        if (model.hasFinished())
        {
            view.feedbackToUser(player, "The game has finished. Press Restart to play again.");
            return;
        }

        if (model.getPlayer() != player)
        {
            view.feedbackToUser(player, "It is not your turn!");
            return;
        }

        int bestX = -1;
        int bestY = -1;
        int bestCount = 0;

        int width = model.getBoardWidth();
        int height = model.getBoardHeight();

        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                if (model.getBoardContents(x, y) == IModel.PLAYER_NONE)
                {
                    int count = countCaptures(player, x, y);
                    if (count > bestCount)
                    {
                        bestCount = count;
                        bestX = x;
                        bestY = y;
                    }
                }
            }
        }

        if (bestX == -1)
        {
            update();
            return;
        }

        // Play the best move
        squareSelected(player, bestX, bestY);
    }

    private int countCaptures(int player, int x, int y)
    {
        int total = 0;
        for (int dx = -1; dx <= 1; dx++)
        {
            for (int dy = -1; dy <= 1; dy++)
            {
                if (dx == 0 && dy == 0) continue;
                total += countInDirection(player, x, y, dx, dy);
            }
        }
        return total;
    }
    
    private int countInDirection(int player, int x, int y, int dx, int dy)
    {
        int opp = opponent(player);
        int count = 0;
        int nx = x + dx;
        int ny = y + dy;
        int width = model.getBoardWidth();
        int height = model.getBoardHeight();

        while (nx >= 0 && nx < width && ny >= 0 && ny < height)
        {
            int contents = model.getBoardContents(nx, ny);
            if (contents == opp)
            {
                count++;
            }
            else if (contents == player)
            {
                return count;
            }
            else
            {
                return 0;
            }
            nx += dx;
            ny += dy;
        }
        return 0;
    }

    private void doCaptures(int player, int x, int y)
    {
        for (int dx = -1; dx <= 1; dx++)
        {
            for (int dy = -1; dy <= 1; dy++)
            {
                if (dx == 0 && dy == 0) continue;
                if (countInDirection(player, x, y, dx, dy) > 0)
                {
                    flipInDirection(player, x, y, dx, dy);
                }
            }
        }
    }

    private void flipInDirection(int player, int x, int y, int dx, int dy)
    {
        int opp = opponent(player);
        int nx = x + dx;
        int ny = y + dy;
        int width = model.getBoardWidth();
        int height = model.getBoardHeight();

        while (nx >= 0 && nx < width && ny >= 0 && ny < height)
        {
            int contents = model.getBoardContents(nx, ny);
            if (contents == opp)
            {
                model.setBoardContents(nx, ny, player);
            }
            else
            {
                break;
            }
            nx += dx;
            ny += dy;
        }
    }

    private boolean canPlayerMove(int player)
    {
        int width = model.getBoardWidth();
        int height = model.getBoardHeight();

        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                if (model.getBoardContents(x, y) == IModel.PLAYER_NONE)
                {
                    if (countCaptures(player, x, y) > 0)
                        return true;
                }
            }
        }
        return false;
    }

    private void endGame()
    {
        int whiteCount = 0;
        int blackCount = 0;
        int width = model.getBoardWidth();
        int height = model.getBoardHeight();

        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                int c = model.getBoardContents(x, y);
                if (c == IModel.PLAYER_WHITE) whiteCount++;
                else if (c == IModel.PLAYER_BLACK) blackCount++;
            }
        }

        model.setFinished(true);

        String msg;
        if (whiteCount > blackCount)
        {
            msg = "White won. White " + whiteCount + " to Black " + blackCount + ". Restart to continue.";
        }
        else if (blackCount > whiteCount)
        {
            msg = "Black won. Black " + blackCount + " to White " + whiteCount + ". Restart to continue.";
        }
        else
        {
            msg = "Draw. Both players ended with " + whiteCount + " pieces. Restart to continue.";
        }

        view.feedbackToUser(IModel.PLAYER_WHITE, msg);
        view.feedbackToUser(IModel.PLAYER_BLACK, msg);
        view.refreshView();
    }

    private int opponent(int player)
    {
        return (player == IModel.PLAYER_WHITE) ? IModel.PLAYER_BLACK : IModel.PLAYER_WHITE;
    }
}
