package WhereToWalk;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame
{
    private float mAspectRatio = 16.0f / 9.0f;
    private int mWidth = 400;
    private int mHeight;
    private JPanel mPanel;
    // private JFrame frame;
    Window()
    {
        mHeight = (int)(mWidth * mAspectRatio);

        mPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));


        JButton b = new JButton("Whomp");
        b.setBounds(40, 40, 100, 100);

        mPanel.add(b);

        setLayout(new FlowLayout());
        add(mPanel);
        setSize(mWidth, mHeight);
        setVisible(true);
    }
}
