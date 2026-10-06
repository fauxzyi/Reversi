package reversi;

import javax.swing.Icon;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class CWLabel extends JLabel
{
	public CWLabel()
	{
		super();
	}

	public CWLabel(Icon image, int horizontalAlignment)
	{
		super(image, horizontalAlignment);
	}

	public CWLabel(Icon image)
	{
		super(image);
	}

	public CWLabel(String text, Icon icon, int horizontalAlignment)
	{
		super(text, icon, horizontalAlignment);
	}

	public CWLabel(String text, int horizontalAlignment)
	{
		super(text, horizontalAlignment);
	}

	public CWLabel(String text)
	{
		super(text);
	}
}
