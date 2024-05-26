import java.text.DecimalFormat;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.geom.RoundRectangle2D;

public class InfoFrame extends JFrame {
    private JPanel outPanel,backPanel;
    private JButton backButton,addLog;
    private int d,m,y;
    DatabaseConnection db = new DatabaseConnection();
    public InfoFrame(int d,int m,int y) {
        this.d = d;
        this.m = m;
        this.y = y;
        db.initConnection();
        setTitle(d + "/" + m + "/" + y);
        setupUI();
    }
    private void setupUI(){
        backPanel = new JPanel();
        backButton = createBorderlessButton(" < Budget Bloom");
        backPanel.setLayout(new BorderLayout());
        backPanel.add(backButton,BorderLayout.WEST);
        setFontForPanel(backPanel, 16);

        addLog = createRoundedButton("Add a new log",Color.decode("#EFCF2C"),getWidth()-20,50,20,20);
        JPanel btn = new JPanel();
        btn.setLayout(new BorderLayout());
        btn.add(addLog,BorderLayout.CENTER);
        btn.setPreferredSize(new Dimension(0,50));
        setFontForPanel(btn, 20);
        addLineSpacingToPanel(btn,10,0,0,0);

        JPanel btnGroup = new JPanel();
        btnGroup.setLayout(new BoxLayout(btnGroup,BoxLayout.Y_AXIS));
        btnGroup.add(backPanel);
        btnGroup.add(btn);
        addLineSpacingToPanel(btnGroup,0,10,5,10);

        outPanel = new JPanel();
        outPanel.setLayout(new GridBagLayout());
        showLog();

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));
        container.add(btnGroup);
        container.add(outPanel);

        JPanel empty = new JPanel();
        empty.setPreferredSize(new Dimension(0,10));
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(container,BorderLayout.NORTH);
        mainPanel.add(empty,BorderLayout.SOUTH);
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(13);
        scrollPane.setBorder(null);

        setLayout(new BorderLayout());
        add(scrollPane);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                HomeFrame homeFrame = new HomeFrame();
                homeFrame.setSize(getSize());
                homeFrame.setLocation(getLocation());
                homeFrame.setVisible(true);
                dispose();
            }
        });
        addLog.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LogFrame logFrame = new LogFrame();
                logFrame.dailyLog(d,m-1,y);
                logFrame.setSize(getSize());
                logFrame.setLocation(getLocation());
                logFrame.setVisible(true);
                dispose();
            }
        });
    }
    private JButton createBorderlessButton(String text) {
        JButton button = new JButton(text);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setFocusPainted(false);
        return button;
    }
    private JLabel createColoredLabel(String text, Color color,int width,int height) {
        JLabel label = new JLabel(text){
            public Dimension getPreferredSize() {
                return new Dimension(width, height);
            }
        };
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(color);
        label.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        return label;
    }
    private void setFontForPanel(JPanel panel, int fontSize) {
        Font font = new Font("Britannic Bold", Font.PLAIN, fontSize);
        setFontRecursively(panel, font);
    }
    private void setFontRecursively(Component component, Font font) {
        component.setFont(font);
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                setFontRecursively(child, font);
            }
        }
    }
    private JButton createRoundedButton(String text,Color color,int width,int height,int arcw,int arch) {
        JButton button = new JButton(text) {
            protected void paintComponent(Graphics g) {
                g.setColor(color);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, arcw, arch));
                super.paintComponent(g);
            }
            public Dimension getPreferredSize() {
                return new Dimension(width, height);
            }
        };
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setBackground(color);
        return button;
    }
    private void addLineSpacingToPanel(JPanel panel,int t,int l,int b,int r) {
        panel.setBorder(BorderFactory.createEmptyBorder(t, l, b, r));
    }
    private void showLog(){
        ArrayList<Log> list = db.individualQuery(d,m,y);
        outPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(5,10,5,10);
        int yPos = 0;

        for (Log log : list) {
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            JLabel ty = new JLabel();
            if(log.getType().equals("Expense")) {
                ty = createColoredLabel(log.getType(), Color.decode("#D82626"),85,0);
            }else {
                ty = createColoredLabel(log.getType(), Color.decode("#5BDA15"),85,0);
            }
            JLabel date = new JLabel(String.valueOf(log.getMyDate()));
            JLabel cat = new JLabel("Category: "+log.getCategory());
            JLabel note = new JLabel("Note: "+log.getNote());
            DecimalFormat df = new DecimalFormat("#,###.##");
            JLabel am = new JLabel(log.getCurrency()+" "+df.format(log.getAmount()));

            JPanel container = new JPanel();
            container.setLayout(new GridLayout(3,1));
            container.add(date);
            container.add(cat);
            container.add(note);
            JPanel info = new JPanel();
            info.setLayout(new BorderLayout());
            info.add(container,BorderLayout.CENTER);
            info.add(am,BorderLayout.EAST);

            JPanel btn = new JPanel(new GridBagLayout());
            GridBagConstraints btnGbc = new GridBagConstraints();
            JButton edit = createRoundedButton("Edit", Color.decode("#54B3F6"), 65, 50,20,20);
            btnGbc.gridx = 0;
            btn.add(edit, btnGbc);
            JButton delete = createRoundedButton("Delete", Color.decode("#E73071"), 85, 50,20,20);
            btnGbc.gridx = 1;
            btn.add(delete, btnGbc);

            addLineSpacingToPanel(info,0,5,0,10);
            panel.add(ty, BorderLayout.WEST);
            panel.add(info, BorderLayout.CENTER);
            panel.add(btn,BorderLayout.EAST);
            gbc.gridy = yPos++;
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            outPanel.add(panel, gbc);
            setFontForPanel(outPanel, 18);

            edit.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    LogFrame logFrame = new LogFrame();
                    logFrame.editLog(log);
                    logFrame.setSize(getSize());
                    logFrame.setLocation(getLocation());
                    logFrame.setVisible(true);
                    dispose();
                }
            });

            delete.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this log?", "Delete Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        db.deleteLog(db.getID(log.getType(),log.getMyDate(),log.getAmount(),log.getCurrency(),log.getCategory(), log.getNote()));
                        JOptionPane.showMessageDialog(InfoFrame.this, "The record has been removed!");
                        outPanel.remove(panel);
                        outPanel.revalidate();
                        outPanel.repaint();
                    }
                }
            });
        }
        revalidate();
        repaint();
    }
}
