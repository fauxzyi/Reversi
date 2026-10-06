package reversi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIView implements IView
{
    IModel model;
    IController controller;

    CWFrame whiteFrame;
    CWFrame blackFrame;

    CWLabel whiteLabel;
    CWLabel blackLabel;

    BoardSquareButton[][] whiteButtons;
    BoardSquareButton[][] blackButtons;

    class BoardSquareButton extends CWButton implements ActionListener
    {
        int modelX;
        int modelY;
        int playerOwner;

        BoardSquareButton(int modelX, int modelY, int playerOwner)
        {
            super();
            this.modelX = modelX;
            this.modelY = modelY;
            this.playerOwner = playerOwner;
            setPreferredSize(new Dimension(50, 50));
            setBackground(Color.GREEN);
            setOpaque(true);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            addActionListener(this);
        }

        @Override
        protected void paintComponent(Graphics g)
        {
            g.setColor(Color.GREEN);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.BLACK);
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

            int contents = GUIView.this.model.getBoardContents(modelX, modelY);
            if (contents == IModel.PLAYER_WHITE || contents == IModel.PLAYER_BLACK)
            {
                Color pieceColor = (contents == IModel.PLAYER_WHITE) ? Color.WHITE : Color.BLACK;
                Color borderColor = (contents == IModel.PLAYER_WHITE) ? Color.BLACK : Color.WHITE;

                g.setColor(pieceColor);
                g.fillOval(2, 2, 46, 46);

                g.setColor(borderColor);
                g.drawOval(2, 2, 46, 46);
            }
        }

        @Override
        protected void paintBorder(Graphics g)
        {
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            GUIView.this.controller.squareSelected(playerOwner, modelX, modelY);
        }
    }

    @Override
    public void initialise(IModel model, IController controller)
    {
        this.model = model;
        this.controller = controller;

        int width = model.getBoardWidth();
        int height = model.getBoardHeight();

        whiteButtons = new BoardSquareButton[width][height];
        blackButtons = new BoardSquareButton[width][height];

        whiteFrame = new CWFrame("Reversi - white player");
        whiteFrame.setDefaultCloseOperation(CWFrame.EXIT_ON_CLOSE);
        whiteFrame.getContentPane().setLayout(new BorderLayout());

        whiteLabel = new CWLabel(" ");
        whiteLabel.setFont(new Font("Arial", Font.BOLD, 14));
        whiteFrame.getContentPane().add(whiteLabel, BorderLayout.NORTH);

        CWPanel whiteBoardPanel = new CWPanel(new GridLayout(height, width));
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                BoardSquareButton btn = new BoardSquareButton(x, y, IModel.PLAYER_WHITE);
                whiteButtons[x][y] = btn;
                whiteBoardPanel.add(btn);
            }
        }
        whiteFrame.getContentPane().add(whiteBoardPanel, BorderLayout.CENTER);

        CWPanel whiteSouthPanel = new CWPanel(new GridLayout(2, 1));
        CWButton whiteAIButton = new CWButton("Greedy AI (play white)");
        whiteAIButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.doAutomatedMove(IModel.PLAYER_WHITE);
            }
        });
        CWButton whiteRestartButton = new CWButton("Restart");
        whiteRestartButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.startup();
            }
        });
        whiteSouthPanel.add(whiteAIButton);
        whiteSouthPanel.add(whiteRestartButton);
        whiteFrame.getContentPane().add(whiteSouthPanel, BorderLayout.SOUTH);

        whiteFrame.pack();
        whiteFrame.setLocationRelativeTo(null);
        whiteFrame.setVisible(true);

        blackFrame = new CWFrame("Reversi - black player");
        blackFrame.setDefaultCloseOperation(CWFrame.EXIT_ON_CLOSE);
        blackFrame.getContentPane().setLayout(new BorderLayout());

        blackLabel = new CWLabel(" ");
        blackLabel.setFont(new Font("Arial", Font.BOLD, 14));
        blackFrame.getContentPane().add(blackLabel, BorderLayout.NORTH);

        CWPanel blackBoardPanel = new CWPanel(new GridLayout(height, width));
        for (int y = height - 1; y >= 0; y--)
        {
            for (int x = width - 1; x >= 0; x--)
            {
                BoardSquareButton btn = new BoardSquareButton(x, y, IModel.PLAYER_BLACK);
                blackButtons[x][y] = btn;
                blackBoardPanel.add(btn);
            }
        }
        blackFrame.getContentPane().add(blackBoardPanel, BorderLayout.CENTER);

        CWPanel blackSouthPanel = new CWPanel(new GridLayout(2, 1));
        CWButton blackAIButton = new CWButton("Greedy AI (play black)");
        blackAIButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.doAutomatedMove(IModel.PLAYER_BLACK);
            }
        });
        CWButton blackRestartButton = new CWButton("Restart");
        blackRestartButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.startup();
            }
        });
        blackSouthPanel.add(blackAIButton);
        blackSouthPanel.add(blackRestartButton);
        blackFrame.getContentPane().add(blackSouthPanel, BorderLayout.SOUTH);

        blackFrame.pack();
        blackFrame.setLocation(whiteFrame.getX() + whiteFrame.getWidth() + 10, whiteFrame.getY());
        blackFrame.setVisible(true);
    }

    @Override
    public void refreshView()
    {
        int width = model.getBoardWidth();
        int height = model.getBoardHeight();

        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                whiteButtons[x][y].repaint();
                blackButtons[x][y].repaint();
            }
        }

        whiteFrame.repaint();
        blackFrame.repaint();
    }

    @Override
    public void feedbackToUser(int player, String message)
    {
        if (player == IModel.PLAYER_WHITE)
            whiteLabel.setText(message);
        else if (player == IModel.PLAYER_BLACK)
            blackLabel.setText(message);
    }
}
