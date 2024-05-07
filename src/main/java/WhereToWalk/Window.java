package WhereToWalk;

import javax.swing.*;

public class Window extends JFrame
{
    private float mAspectRatio = 17.0f / 9.0f;
    private int mWidth = 200;
    private int mHeight;
    // private JFrame frame;
    Window()
    {
        mHeight = (int)(mWidth * mAspectRatio);
        setSize(mWidth, mHeight);
        setVisible(true);

        JButton b = new JButton("Whomp");
        b.setBounds(20, 10, 10, 10);

        add(b);
    }
}
