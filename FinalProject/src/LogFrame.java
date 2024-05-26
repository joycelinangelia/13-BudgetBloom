import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.time.LocalDate;
import java.awt.geom.RoundRectangle2D;

public class LogFrame extends JFrame {
    private ArrayList<String> category;
    private JPanel backPanel, titlePanel, typePanel,datePanel,amountPanel,categoryPanel,notePanel,addPanel,inputDate;
    private JButton backButton, addLog, addCategory, delCategory;
    private ButtonGroup typeButton;
    private JRadioButton income,expense;
    private JLabel titleLabel, typeLabel,dateLabel, amountLabel, categoryLabel, noteLabel;
    private JTextField inputAmount, inputNote;
    private JComboBox<String> categoryChoices,date,month,year,currencyChoices;
    DatabaseConnection db = new DatabaseConnection();

    public LogFrame() {
        db.initConnection();
        setTitle("New Record");
        category = db.getCategory();
        setupUI();
    }
    private void setupUI(){
        String[] days = new String[31];
        for(int i=1;i<=31;++i)days[i-1]=String.valueOf(i);
        date = new JComboBox<>(days);
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        month = new JComboBox<>(months);
        String[] years = new String[100];
        int currentYear = LocalDate.now().getYear();
        for(int i=0;i<100;++i)years[i]=String.valueOf(currentYear-i);
        year = new JComboBox<>(years);

        int currentDay = LocalDate.now().getDayOfMonth();
        int currentMonth = LocalDate.now().getMonthValue();
        date.setSelectedItem(String.valueOf(currentDay));
        month.setSelectedIndex(currentMonth-1);
        year.setSelectedItem(String.valueOf(currentYear));
        date.setPreferredSize(new Dimension(60, 50));
        month.setPreferredSize(new Dimension(140, 50));
        year.setPreferredSize(new Dimension(80, 50));

        backPanel = new JPanel();
        backButton = createBorderlessButton("   < Budget Bloom");
        titlePanel = new JPanel(new BorderLayout());
        titleLabel = createColoredLabel("LOG", Color.decode("#EFCF2C"));
        typePanel = new JPanel();
        typeLabel = createInputLabel("Type",95);
        typeButton = new ButtonGroup();
        income = new JRadioButton("Income");
        expense = new JRadioButton("Expense");
        datePanel = new JPanel();
        dateLabel = createInputLabel("Date",95);
        inputDate = new JPanel();
        amountPanel = new JPanel();
        amountLabel = createInputLabel("Amount",100);
        inputAmount = createUnderlineTextField(10,amountPanel.getBackground());
        CurrencyConverter cc = new CurrencyConverter();
        currencyChoices = new JComboBox<String>(cc.getCurrencyList());
        currencyChoices.setSelectedItem(db.getBaseCurrency());
        currencyChoices.setPreferredSize(new Dimension(85,0));
        categoryPanel = new JPanel();
        categoryLabel = createInputLabel("Category",100);
        categoryChoices = new JComboBox<String>(category.toArray(new String[0]));
        addCategory = createRoundedButton("Add",Color.decode("#5BDA15"),85,30);
        delCategory = createRoundedButton("Del",Color.decode("#D82626"),85,30);
        notePanel = new JPanel();
        noteLabel = createInputLabel("Note",100);
        inputNote = createUnderlineTextField(10,notePanel.getBackground());
        addPanel = new JPanel();
        addLog = createRoundedButton("Save",Color.decode("#54B3F6"),100,30);

        JPanel cat = new JPanel();
        cat.setLayout(new GridLayout(2,1));
        cat.add(addCategory);
        cat.add(delCategory);
        setupPanel(backPanel, backButton, BorderLayout.WEST);
        setupPanel(titlePanel, titleLabel, BorderLayout.CENTER);
        typeButton.add(income);
        typeButton.add(expense);
        setupFlowPanel(typePanel, FlowLayout.LEFT, typeLabel,income,expense);
        setupFlowPanel(inputDate, FlowLayout.LEFT, date,month,year);
        setupFlowPanel(datePanel, FlowLayout.LEFT, dateLabel, inputDate);
        setupPanel(amountPanel, amountLabel, inputAmount,currencyChoices);
        setupPanel(categoryPanel, categoryLabel, categoryChoices,cat);
        setupPanel(notePanel, noteLabel, inputNote);
        setupPanel(addPanel, addLog, BorderLayout.CENTER);

        setFontForPanel(backPanel, 16);
        setFontForPanel(titlePanel, 30);
        setFontForPanel(typePanel, 20);
        setFontForPanel(datePanel, 20);
        setFontForPanel(amountPanel, 20);
        setFontForPanel(categoryPanel, 20);
        setFontForPanel(notePanel, 20);
        setFontForPanel(addPanel, 20);

        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
        containerPanel.add(titlePanel);
        containerPanel.add(typePanel);
        containerPanel.add(datePanel);
        containerPanel.add(amountPanel);
        containerPanel.add(categoryPanel);
        containerPanel.add(notePanel);
        containerPanel.add(addPanel);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1.0;
        add(backButton, gbc);

        gbc.gridy++;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(containerPanel, gbc);

        addLineSpacingToPanel(backPanel,20,10,10,0);
        addLineSpacingToPanel(titlePanel,10,10,0,10);
        addLineSpacingToPanel(typePanel,10,5,0,0);
        addLineSpacingToPanel(datePanel,0,5,0,0);
        addLineSpacingToPanel(amountPanel,0,10,5,10);
        addLineSpacingToPanel(categoryPanel,5,10,5,10);
        addLineSpacingToPanel(notePanel,0,10,5,10);
        addLineSpacingToPanel(addPanel,5,10,10,10);

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

        addCategory.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newCategory = JOptionPane.showInputDialog(LogFrame.this, "Please input the name of the new category:");
                if (newCategory != null && !newCategory.isEmpty()) {
                    if (category.contains(newCategory)) {
                        JOptionPane.showMessageDialog(LogFrame.this, "Category already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        db.insertQuery(newCategory);
                        categoryChoices.addItem(newCategory);
                        JOptionPane.showMessageDialog(LogFrame.this, "New category ["+newCategory+"] added!");
                        categoryChoices.setSelectedItem(newCategory);
                    }
                }
            }
        });
        delCategory.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try{
                    String category = (String) categoryChoices.getSelectedItem();
                    if (category == null || category.isEmpty() || category.equals("Add")) {
                        throw new IllegalArgumentException("Please select a category!");
                    }
                    int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this category?", "Delete Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        db.deleteCategory(db.getID(category),category);
                        categoryChoices.removeItem(category);
                        categoryChoices.setSelectedIndex(0);
                        JOptionPane.showMessageDialog(LogFrame.this, "Category ["+category+"] has been removed!");
                    }
                } catch (IllegalArgumentException e){
                    JOptionPane.showMessageDialog(LogFrame.this, e.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        addLog.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveLog();
                HomeFrame homeFrame = new HomeFrame();
                homeFrame.setSize(getSize());
                homeFrame.setLocation(getLocation());
                homeFrame.setVisible(true);
                dispose();
            }
        });
    }
    private JButton createRoundedButton(String text,Color color, int width, int height) {
        JButton button = new JButton(text) {
            protected void paintComponent(Graphics g) {
                g.setColor(color);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));
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
    private JLabel createInputLabel (String text,int size){
        JLabel label = new JLabel(text);
        label.setPreferredSize(new Dimension(size, 30));
        return label;
    }
    private JLabel createColoredLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(color);
        label.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        return label;
    }
    private JButton createBorderlessButton(String text) {
        JButton button = new JButton(text);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setFocusPainted(false);
        return button;
    }
    private JTextField createUnderlineTextField(int columns, Color backgroundColor) {
        JTextField textField = new JTextField(columns);
        textField.setOpaque(false);
        textField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        textField.setBackground(backgroundColor);
        return textField;
    }
    private void setupPanel(JPanel panel, JComponent component, String layoutPosition) {
        panel.setLayout(new BorderLayout());
        panel.add(component, layoutPosition);
    }
    private void setupFlowPanel(JPanel panel, int alignment, JComponent... components) {
        panel.setLayout(new FlowLayout(alignment));
        for (JComponent component : components) {
            panel.add(component);
        }
    }
    private void setupPanel(JPanel panel, JComponent label, JComponent field) {
        panel.setLayout(new BorderLayout());
        panel.add(label, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        Dimension maxSize = new Dimension(label.getPreferredSize().width, label.getPreferredSize().height);
        label.setPreferredSize(maxSize);
    }
    private void setupPanel(JPanel panel, JComponent label, JComponent field, JComponent button) {
        JPanel tmp = new JPanel();
        tmp.setLayout(new BorderLayout());
        tmp.add(label,BorderLayout.WEST);
        tmp.add(field,BorderLayout.CENTER);
        addLineSpacingToPanel(tmp,0,0,0,10);
        panel.setLayout(new BorderLayout());
        panel.add(tmp, BorderLayout.CENTER);
        panel.add(button, BorderLayout.EAST);
    }
    private void addLineSpacingToPanel(JPanel panel,int t,int l,int b,int r) {
        panel.setBorder(BorderFactory.createEmptyBorder(t, l, b, r));
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
    private Date getDateFromComboBoxes() {
        int d = Integer.parseInt((String) date.getSelectedItem());
        int m = month.getSelectedIndex()+1;
        int y = Integer.parseInt((String) year.getSelectedItem());
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        calendar.set(y, m-1, d);
        try {
            return calendar.getTime();
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    private void saveLog() {
        try {
            if (!income.isSelected() && !expense.isSelected()) {
                throw new IllegalArgumentException("Please select a type (Income or Expense)!");
            }
            String type = income.isSelected() ? "Income" : "Expense";

            Date date = getDateFromComboBoxes();
            if (date == null) {
                throw new IllegalArgumentException("Please select a valid date!");
            }

            String amountStr = inputAmount.getText();
            if (amountStr.isEmpty() || !amountStr.matches("\\d+(\\.\\d{1,2})?")) {
                throw new IllegalArgumentException("Please enter a valid amount!");
            }
            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be a positive number!");
            }

            String currency = (String) currencyChoices.getSelectedItem();
            if (currency == null || currency.isEmpty()) {
                throw new IllegalArgumentException("Please select a currency!");
            }

            String category = (String) categoryChoices.getSelectedItem();
            if (category == null || category.isEmpty() || category.equals("Add")) {
                throw new IllegalArgumentException("Please select a category!");
            }
            String note = inputNote.getText();
            db.insertQuery(type, date, amount, currency, category, note);
            JOptionPane.showMessageDialog(LogFrame.this, "New record has been successfully added!");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(LogFrame.this, e.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void editLog(Log log) {
        if(log.getType().equals("Income")) income.setSelected(true);
        else expense.setSelected(true);
        Date utilDate = log.getMyDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(utilDate);
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int m = calendar.get(Calendar.MONTH);
        int y = calendar.get(Calendar.YEAR);
        date.setSelectedItem(String.valueOf(d));
        month.setSelectedIndex(m);
        year.setSelectedItem(String.valueOf(y));
        inputAmount.setText(String.valueOf(log.getAmount()));
        categoryChoices.setSelectedItem(log.getCategory());
        inputNote.setText(log.getNote());
        backButton.setText("   < Back");
        for(ActionListener al : backButton.getActionListeners()) {
            backButton.removeActionListener(al);
        }
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InfoFrame infoFrame = new InfoFrame(d, m+1, y);
                infoFrame.setSize(getSize());
                infoFrame.setLocation(getLocation());
                infoFrame.setVisible(true);
                dispose();
            }
        });
        for(ActionListener al : addLog.getActionListeners()) {
            addLog.removeActionListener(al);
        }
        addLog.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                int id= db.getID(log.getType(),log.getMyDate(),log.getAmount(), log.getCurrency(), log.getCategory(),log.getNote());
                try {
                    if (!income.isSelected() && !expense.isSelected()) {
                        throw new IllegalArgumentException("Please select a type (Income or Expense)!");
                    }
                    String type = income.isSelected() ? "Income" : "Expense";

                    Date date = getDateFromComboBoxes();
                    if (date == null) {
                        throw new IllegalArgumentException("Please select a valid date!");
                    }

                    String amountStr = inputAmount.getText();
                    if (amountStr.isEmpty() || !amountStr.matches("\\d+(\\.\\d{1,2})?")) {
                        throw new IllegalArgumentException("Please enter a valid amount!");
                    }
                    double amount = Double.parseDouble(amountStr);
                    if (amount <= 0) {
                        throw new IllegalArgumentException("Amount must be a positive number!");
                    }

                    String currency = (String) currencyChoices.getSelectedItem();
                    if (currency == null || currency.isEmpty()) {
                        throw new IllegalArgumentException("Please select a currency!");
                    }

                    String category = (String) categoryChoices.getSelectedItem();
                    if (category == null || category.isEmpty() || category.equals("Add")) {
                        throw new IllegalArgumentException("Please select a category!");
                    }
                    String note = inputNote.getText();
                    db.editQuery(id, type, date, amount, currency, category, note);
                    JOptionPane.showMessageDialog(LogFrame.this, "The record has been successfully updated!");
                    InfoFrame infoFrame = new InfoFrame(d,m+1,y);
                    infoFrame.setSize(getSize());
                    infoFrame.setLocation(getLocation());
                    infoFrame.setVisible(true);
                    dispose();
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(LogFrame.this, e.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    public void dailyLog(int d,int m,int y) {
        date.setSelectedItem(String.valueOf(d));
        month.setSelectedIndex(m);
        year.setSelectedItem(String.valueOf(y));
        backButton.setText("   < Back");
        for(ActionListener al : backButton.getActionListeners()) {
            backButton.removeActionListener(al);
        }
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InfoFrame infoFrame = new InfoFrame(d, m+1, y);
                infoFrame.setSize(getSize());
                infoFrame.setLocation(getLocation());
                infoFrame.setVisible(true);
                dispose();
            }
        });
        for(ActionListener al : addLog.getActionListeners()) {
            addLog.removeActionListener(al);
        }
        addLog.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                saveLog();
                InfoFrame infoFrame = new InfoFrame(d,m+1,y);
                infoFrame.setSize(getSize());
                infoFrame.setLocation(getLocation());
                infoFrame.setVisible(true);
                dispose();
            }
        });
    }
}