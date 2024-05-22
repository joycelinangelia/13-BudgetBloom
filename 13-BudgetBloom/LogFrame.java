import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.time.LocalDate;

public class LogFrame extends JFrame {
    private ArrayList<String> category;
    private JPanel backPanel, titlePanel, typePanel,datePanel,amountPanel,categoryPanel,notePanel,addPanel,inputDate;
    private JButton backButton, addLog, addCategory;
    private ButtonGroup typeButton;
    private JRadioButton income,expense;
    private JLabel titleLabel, typeLabel,dateLabel, amountLabel, categoryLabel, noteLabel;
    private JTextField inputAmount, inputNote;
    private JComboBox<String> categoryChoices,date,month,year;


    public LogFrame() {
        setTitle("New Record");
        ArrayList<String> category = new ArrayList<String>();
        category.add("");
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
        date.setPreferredSize(new Dimension(50, 50));
        month.setPreferredSize(new Dimension(100, 50));
        year.setPreferredSize(new Dimension(70, 50));

        backPanel = new JPanel();
        backButton = createBorderlessButton("< BudgetBloom");
        titlePanel = new JPanel();
        titleLabel = createColoredLabel("LOG", Color.PINK);
        typePanel = new JPanel();
        typeLabel = new JLabel("Type        ");
        typeButton = new ButtonGroup();
        income = new JRadioButton("Income");
        expense = new JRadioButton("Expense");
        datePanel = new JPanel();
        dateLabel = new JLabel("Date        ");
        inputDate = new JPanel();
        amountPanel = new JPanel();
        amountLabel = new JLabel("Amount     ");
        inputAmount = createUnderlineTextField(10,amountPanel.getBackground());
        categoryPanel = new JPanel();
        categoryLabel = new JLabel("Category  ");
        categoryChoices = new JComboBox<String>(category.toArray(new String[0]));
        addCategory = new JButton("Add");
        notePanel = new JPanel();
        noteLabel = new JLabel("Note        ");
        inputNote = createUnderlineTextField(10,notePanel.getBackground());
        addPanel = new JPanel();
        addLog = new JButton("Save");

        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        setupPanel(backPanel, backButton, BorderLayout.WEST);
        setupPanel(titlePanel, titleLabel, BorderLayout.CENTER);
        typeButton.add(income);
        typeButton.add(expense);
        setupFlowPanel(typePanel, FlowLayout.LEFT, typeLabel,income,expense);
        setupFlowPanel(inputDate, FlowLayout.LEFT, date,month,year);
        setupPanel(datePanel, dateLabel, inputDate);
        setupPanel(amountPanel, amountLabel, inputAmount);
        setupPanel(categoryPanel, categoryLabel, categoryChoices,addCategory);
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

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx=0;
        gbc.gridy=GridBagConstraints.RELATIVE;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.weightx=1.0;

        addPanelToLayout(backPanel, gbc, 25);
        addPanelToLayout(titlePanel, gbc, 40);
        addPanelToLayout(typePanel, gbc, 20);
        addPanelToLayout(datePanel, gbc, 20);
        addPanelToLayout(amountPanel, gbc, 40);
        addPanelToLayout(categoryPanel, gbc, 40);
        addPanelToLayout(notePanel, gbc, 40);
        addPanelToLayout(addPanel, gbc, 15);

        addLineSpacingToPanel(backPanel,0,10,0,0);
        addLineSpacingToPanel(titlePanel,0,0,0,0);
        addLineSpacingToPanel(typePanel,0,0,0,0);
        addLineSpacingToPanel(datePanel,0,10,0,0);
        addLineSpacingToPanel(amountPanel,0,10,5,10);
        addLineSpacingToPanel(categoryPanel,5,10,5,10);
        addLineSpacingToPanel(notePanel,0,10,5,10);
        addLineSpacingToPanel(addPanel,5,10,10,10);

        addCategory.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newCategory = JOptionPane.showInputDialog(LogFrame.this, "Please input the name of the new category:");
                if (newCategory != null && !newCategory.isEmpty()) {
                    if (category.contains(newCategory)) {
                        JOptionPane.showMessageDialog(LogFrame.this, "Category already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        category.add(newCategory);
                        categoryChoices.addItem(newCategory);
                        JOptionPane.showMessageDialog(LogFrame.this, "New category added!");
                        categoryChoices.setSelectedItem(newCategory);
                    }
                }
            }
        });

        addLog.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveLog();
            }
        });

    }
    private JLabel createColoredLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setOpaque(true); // Make the label opaque
        label.setBackground(color); // Set the background color
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
    private void addPanelToLayout(JPanel panel, GridBagConstraints gbc, int height) {
        gbc.ipady = height;
        add(panel, gbc);
    }
    private void addLineSpacingToPanel(JPanel panel,int t,int l,int b,int r) {
        panel.setBorder(BorderFactory.createEmptyBorder(t, l, b, r));
    }
    private void setFontForPanel(JPanel panel, int fontSize) {
        Font font = new Font("Comic Sans MS", Font.PLAIN, fontSize);
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
    private void reset() {
        typeButton.clearSelection();
        LocalDate currentDate = LocalDate.now();
        date.setSelectedItem(String.valueOf(currentDate.getDayOfMonth()));
        month.setSelectedIndex(currentDate.getMonthValue()-1);
        year.setSelectedItem(String.valueOf(currentDate.getYear()));
        inputAmount.setText("");
        inputNote.setText("");
        categoryChoices.setSelectedIndex(0);
    }
    private void saveLog() {
        try {
            if (!income.isSelected() && !expense.isSelected()) {
                throw new IllegalArgumentException("Please select a type (Income or Expense).");
            }
            String type = income.isSelected() ? "Income" : "Expense";

            Date date = getDateFromComboBoxes();
            if (date == null) {
                throw new IllegalArgumentException("Please select a valid date.");
            }

            String amountStr = inputAmount.getText();
            if (amountStr.isEmpty() || !amountStr.matches("\\d+(\\.\\d{1,2})?")) {
                throw new IllegalArgumentException("Please enter a valid amount.");
            }
            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be a positive number.");
            }

            String category = (String) categoryChoices.getSelectedItem();
            if (category == null || category.isEmpty() || category.equals("Add")) {
                throw new IllegalArgumentException("Please select a category.");
            }
            String note = inputNote.getText();

            Log log = new Log(type, date, amount, category, note);
            JOptionPane.showMessageDialog(this,"New log has been successfully added!");
            reset();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Amount must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}