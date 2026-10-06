package reversi;

import java.awt.LayoutManager;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class CWPanel extends JPanel
{
	public CWPanel()
	{
		super();
	}

	public CWPanel(boolean isDoubleBuffered)
	{
		super(isDoubleBuffered);
	}

	public CWPanel(LayoutManager layout, boolean isDoubleBuffered)
	{
		super(layout, isDoubleBuffered);
	}

	public CWPanel(LayoutManager layout)
	{
		super(layout);
	}
}
