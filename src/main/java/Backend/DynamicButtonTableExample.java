package Backend;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class DynamicButtonTableExample {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Dynamic Button Table Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Sample data for the table
            Object[][] data = {
                    {"Row 1 Data", "Button 1"},
                    {"Row 2 Data", "Button 2"},
                    {"Row 3 Data", "Button 3"}
            };

            // Column names
            String[] columnNames = {"Data", "Action"};

            // Create the table model
            DefaultTableModel model = new DefaultTableModel(data, columnNames);

            // Create the JTable with the model
            JTable table = new JTable(model);

            // Set the custom renderer and editor for the first column
            table.getColumnModel().getColumn(0).setCellRenderer(new ButtonRenderer());
            table.getColumnModel().getColumn(0).setCellEditor(new ButtonEditor(new JTextField()));

            // Add the table to a scroll pane
            JScrollPane scrollPane = new JScrollPane(table);
            frame.add(scrollPane, BorderLayout.CENTER);

            frame.setSize(400, 300);
            frame.setVisible(true);
        });
    }

    // Custom cell renderer for rendering buttons
    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    // Custom cell editor for handling button clicks
    static class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JTextField textField) {
            super(textField);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(UIManager.getColor("Button.background"));
            }

            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                // Perform action based on the button click
                // For example, you can display row details in a dialog
                JOptionPane.showMessageDialog(button, "Row details: " + label);
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
}
