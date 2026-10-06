package reversi;

import java.awt.Point;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class CWButton extends JButton
{
	public CWButton()
	{
		super();
	}

	public CWButton(Action a)
	{
		super(a);
	}

	public CWButton(Icon icon)
	{
		super(icon);
	}

	public CWButton(String text, Icon icon)
	{
		super(text, icon);
	}

	public CWButton(String text)
	{
		super(text);
	}
}
