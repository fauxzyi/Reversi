package reversi;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class CWFrame extends JFrame
{
	public CWFrame() throws HeadlessException
	{
		super();
	}

	public CWFrame(GraphicsConfiguration gc)
	{
		super(gc);
	}

	public CWFrame(String title, GraphicsConfiguration gc)
	{
		super(title, gc);
	}

	public CWFrame(String title) throws HeadlessException
	{
		super(title);
	}
	
}
