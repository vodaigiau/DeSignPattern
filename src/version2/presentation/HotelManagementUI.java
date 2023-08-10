package version2.presentation;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import version2.domain.model.Invoice;
import version2.domain.model.InvoiceObserver;
import version2.presentation.comand.AddInvoiceCommand;
import version2.presentation.comand.Command;
import version2.presentation.comand.CommandProcessor;
import version2.presentation.comand.EditInvoiceCommand;
import version2.presentation.comand.InvoiceService;
import version2.presentation.comand.RemoveInvoiceCommand;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HotelManagementUI extends JFrame  implements InvoiceObserver{
    private final InvoiceService invoiceService;
    private final DefaultTableModel tableModel;
    private final JTable studentTable;
    private final JLabel idLabel, dateLabel, nameLabel, roomCodeLabel, unitPriceLabel, hoursOrDaysLabel;
    private final JTextField idField, dateField, nameField, roomCodeField, unitPriceField, hoursOrDaysField;
    private final JButton addButton, removeButton, editButton, findButton;
    private final InvoiceController invoiceController;
    private final CommandProcessor commandProcessor;

    private Set<Integer> uniqueInvoiceIds = new HashSet<>();
    
    
    


    public HotelManagementUI(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
        this.invoiceController = new InvoiceController(invoiceService);
        commandProcessor = new CommandProcessor();
        
        //observer
        invoiceService.addObserver(this);


        idLabel = new JLabel("Mã Hoá đơn:");
        dateLabel = new JLabel("Ngày Hoá Đơn (DD-MM-YYYY):");
        nameLabel = new JLabel("Tên Khách Hàng:");
        roomCodeLabel = new JLabel("Số phòng:");
        unitPriceLabel = new JLabel("Tiền Phòng:");
        hoursOrDaysLabel = new JLabel("Số giờ thuê:");

        idField = new JTextField(10);
        dateField = new JTextField(10);
        nameField = new JTextField(20);
        roomCodeField = new JTextField(10);
        unitPriceField = new JTextField(10);
        hoursOrDaysField = new JTextField(5);

        addButton = new JButton("Add");
        removeButton = new JButton("Remove");
        editButton = new JButton("Edit");
        findButton = new JButton("Find");
        // tính thuê phòng và tính trung bình  tháng
        JButton calculateRoomTypeCountsButton = new JButton("Tính số lượng theo loại phòng");
        
        calculateRoomTypeCountsButton.addActionListener(e -> calculateAndDisplayRoomTypeCounts());
        
        JButton calculateAveragePriceButton = new JButton("Tính trung bình thành tiền trong tháng");
        calculateAveragePriceButton.addActionListener(e -> calculateAndDisplayAveragePriceInMonth());
        
        String[] columnNames = {"MÃ HOÁ ĐƠN", "NGÀY HOÁ ĐƠN", "TÊN KHÁCH HÀNG", "SỐ PHÒNG", "TIỀN PHÒNG", "SỐ GIỜ ", "TỔNG TIỀN"};
        tableModel = new DefaultTableModel(columnNames, 0);
        studentTable = new JTable(tableModel);
        

        

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            @Override
            public void setValue(Object value) {
                if (value instanceof LocalDate) {
                    value = ((LocalDate) value).format(formatter);
                }
                super.setValue(value);
            }
        };
        studentTable.getColumnModel().getColumn(1).setCellRenderer(renderer);
        
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        studentTable.setRowSorter(sorter);
        Comparator<Integer> idComparator = Comparator.naturalOrder();
        sorter.setComparator(0, idComparator);
        Comparator<Integer> idReverseComparator = Comparator.reverseOrder();
        sorter.setComparator(0, idReverseComparator);

        refreshInvoiceTable();
        

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int invoiceId = Integer.parseInt(idField.getText());
                    LocalDate invoiceDate = parseInvoiceDate(dateField.getText());
                    String customerName = nameField.getText();
                    String roomCode = roomCodeField.getText();
                    double unitPrice = Double.parseDouble(unitPriceField.getText());
                    int hoursOrDays = Integer.parseInt(hoursOrDaysField.getText());
        
                    invoiceController.addInvoice(invoiceId, invoiceDate, customerName, roomCode, unitPrice, hoursOrDays);
        
                    clearInputFields();
                    refreshInvoiceTable();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(HotelManagementUI.this, "Invalid input for values. Please enter valid numbers.");
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(HotelManagementUI.this, ex.getMessage());
                }
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = studentTable.getSelectedRow();
        
                if (selectedRow != -1) {
                    int invoiceId = (int) studentTable.getValueAt(selectedRow, 0);
                    invoiceController.removeInvoice(invoiceId);
                    uniqueInvoiceIds.remove(invoiceId);
                    refreshInvoiceTable();
                    clearInputFields();
                } else {
                    JOptionPane.showMessageDialog(HotelManagementUI.this, "Please select an invoice to remove.");
                }
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow != -1) {
                    try {
                        int invoiceId = Integer.parseInt(idField.getText());
                        LocalDate invoiceDate = parseInvoiceDate(dateField.getText());
                        String customerName = nameField.getText();
                        String roomCode = roomCodeField.getText();
                        double unitPrice = Double.parseDouble(unitPriceField.getText());
                        int hoursOrDays = Integer.parseInt(hoursOrDaysField.getText());
        
                        invoiceController.editInvoice(invoiceId, invoiceDate, customerName, roomCode, unitPrice, hoursOrDays);
        
                        refreshInvoiceTable();
                        clearInputFields();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(HotelManagementUI.this, "Invalid input for values. Please enter valid numbers.");
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(HotelManagementUI.this, ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(HotelManagementUI.this, "Please select an invoice to edit.");
                }
            }
        });
        

        findButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idStr = JOptionPane.showInputDialog(HotelManagementUI.this, "Enter the invoice ID to find:");
                if (idStr != null && !idStr.isEmpty()) {
                    try {
                        int invoiceId = Integer.parseInt(idStr);
                        Invoice invoice = invoiceController.findInvoice(invoiceId);
                        if (invoice != null) {
                            populateInputFields(invoice);
        
                            // Find the row index of the found invoice in the table
                            int rowIndex = findRowIndexByInvoiceId(invoiceId);
        
                            // Select the corresponding row in the table
                            if (rowIndex != -1) {
                                studentTable.setRowSelectionInterval(rowIndex, rowIndex);
                            }
                        } else {
                            JOptionPane.showMessageDialog(HotelManagementUI.this, "Invoice not found with ID: " + invoiceId);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(HotelManagementUI.this, "Invalid input for ID. Please enter a valid number.");
                    }
                }
            }
        });
        

        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedInvoiceInfo();
            }
        });

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        inputPanel.add(idLabel, gbc);
        gbc.gridx++;
        inputPanel.add(idField, gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        inputPanel.add(dateLabel, gbc);
        gbc.gridx++;
        inputPanel.add(dateField, gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        inputPanel.add(nameLabel, gbc);
        gbc.gridx++;
        inputPanel.add(nameField, gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        inputPanel.add(roomCodeLabel, gbc);
        gbc.gridx++;
        inputPanel.add(roomCodeField, gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        inputPanel.add(unitPriceLabel, gbc);
        gbc.gridx++;
        inputPanel.add(unitPriceField, gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        inputPanel.add(hoursOrDaysLabel, gbc);
        gbc.gridx++;
        inputPanel.add(hoursOrDaysField, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(editButton);
        buttonPanel.add(findButton);
        buttonPanel.add(calculateRoomTypeCountsButton);
        buttonPanel.add(calculateAveragePriceButton);
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new JScrollPane(studentTable), BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        

        this.setTitle("Invoice Management System");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 400);
        this.add(mainPanel);
        this.setVisible(true);
    }

    public void addInvoice(int invoiceId, LocalDate invoiceDate, String customerName, String roomCode, double unitPrice, int hoursOrDays) {
        if (hoursOrDays > 30) {
            throw new IllegalArgumentException("Invalid hours/days value.");
            
        }
        
        Command addCommand = new AddInvoiceCommand(invoiceController, invoiceId, invoiceDate, customerName, roomCode, unitPrice, hoursOrDays);
        commandProcessor.executeCommand(addCommand);
    }

    private LocalDate parseInvoiceDate(String dateString) {
    try {
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    } catch (DateTimeParseException ex) {
        throw new IllegalArgumentException("Invalid date format. Please enter a date in the format DD-MM-YYYY.");
    }
    
    
}

    public void removeInvoice(int invoiceId) {
        Command removeCommand = new RemoveInvoiceCommand(invoiceController, invoiceId);
        commandProcessor.executeCommand(removeCommand);
    }
      
    public void editInvoice(int invoiceId, LocalDate invoiceDate, String customerName, String roomCode, double unitPrice, int hoursOrDays) {
        Invoice invoice = invoiceService.findInvoice(invoiceId);
        if (invoice != null) {
            if (hoursOrDays > 30) {
                throw new IllegalArgumentException("Invalid hours/days value.");
            }

            invoice.setInvoiceDate(invoiceDate);
            invoice.setCustomerName(customerName);
            invoice.setRoomCode(roomCode);
            invoice.setUnitPrice(unitPrice);
            invoice.setHoursOrDays(hoursOrDays);

            Command editCommand = new EditInvoiceCommand(invoiceController, invoiceId, invoiceDate, customerName, roomCode, unitPrice, hoursOrDays);
            commandProcessor.executeCommand(editCommand);
        }
    }
    

    public Invoice findInvoice(int invoiceId) {
        return invoiceService.findInvoice(invoiceId);
    }

    public List<Invoice> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }

   private int findRowIndexByInvoiceId(int invoiceId) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int rowInvoiceId = (int) tableModel.getValueAt(i, 0);
            if (rowInvoiceId == invoiceId) {
                return i;
            }
        }
        return -1;
    }

    private void showSelectedInvoiceInfo() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow != -1) {
            int invoiceId = (int) studentTable.getValueAt(selectedRow, 0);
            Invoice invoice = invoiceService.findInvoice(invoiceId);
            if (invoice != null) {
                populateInputFields(invoice);
            }
        }
    }

    private void populateInputFields(Invoice invoice) {
        idField.setText(String.valueOf(invoice.getInvoiceId()));
        dateField.setText(invoice.getInvoiceDate().toString()); 
        nameField.setText(invoice.getCustomerName());
        roomCodeField.setText(invoice.getRoomCode());
        unitPriceField.setText(String.valueOf(invoice.getUnitPrice()));
        hoursOrDaysField.setText(String.valueOf(invoice.getHoursOrDays()));
 
    }
    

    private void clearInputFields() {
        idField.setText("");
        dateField.setText("");
        nameField.setText("");
        roomCodeField.setText("");
        unitPriceField.setText("");
        hoursOrDaysField.setText("");
    }

    private void refreshInvoiceTable() {
        tableModel.setRowCount(0);
    
        List<Invoice> invoices = invoiceService.getAllInvoices();
        for (Invoice invoice : invoices) {
            double totalMoney = calculateTotalMoney(invoice);
            Object[] rowData = {invoice.getInvoiceId(), invoice.getInvoiceDate(), invoice.getCustomerName(),
                    invoice.getRoomCode(), invoice.getUnitPrice(), invoice.getHoursOrDays(), totalMoney};
            tableModel.addRow(rowData);
        }
    }

    private double calculateTotalMoney(Invoice invoice) {
        int hoursOrDays = invoice.getHoursOrDays();
        double unitPrice = invoice.getUnitPrice();
    
        if (hoursOrDays > 30 && hoursOrDays < 168) {
            return unitPrice * hoursOrDays;
        } else if (hoursOrDays >= 168) {
            return unitPrice * hoursOrDays * 0.8; // Giảm 20% total money nếu số hoursordaily >= 168
        } else if (hoursOrDays > 24 && hoursOrDays <30) {
            return unitPrice * 24;
        } else {
            return unitPrice * hoursOrDays;
        }
    }
    private void calculateAndDisplayRoomTypeCounts() {
        int hourBasedCount = 0;
        int dayBasedCount = 0;
    
        List<Invoice> invoices = invoiceService.getAllInvoices();
        for (Invoice invoice : invoices) {
            if (invoice.getHoursOrDays() > 30) {
                dayBasedCount++;
            } else {
                hourBasedCount++;
            }
        }
    
        JOptionPane.showMessageDialog(this,
                "Số lượng hóa đơn thuê theo giờ: " + hourBasedCount + "\n" +
                "Số lượng hóa đơn thuê theo ngày: " + dayBasedCount);
    }
    //observer
    @Override
    public void updateInvoices(List<Invoice> updatedInvoices) {
        // Cập nhật giao diện UI của bạn với danh sách hóa đơn mới
        // Ví dụ: bạn có thể làm mới bảng bằng dữ liệu mới
        refreshInvoiceTable();
    }
    
    private void calculateAndDisplayAveragePriceInMonth() {
        String monthStr = JOptionPane.showInputDialog(this, "Nhập tháng (MM) để tính trung bình tổng tiền:");
        if (monthStr != null && !monthStr.isEmpty()) {
            try {
                double totalSum = 0;
                int count = 0;
    
                List<Invoice> invoices = invoiceService.getAllInvoices();
                for (Invoice invoice : invoices) {
                    LocalDate invoiceDate = invoice.getInvoiceDate();
                    if (invoiceDate.getMonthValue() == Integer.parseInt(monthStr)) {
                        totalSum += calculateTotalMoney(invoice);
                        count++;
                    }
                }
    
                if (count > 0) {
                    double averagePrice = totalSum / count;
                    JOptionPane.showMessageDialog(this, "Trung bình tổng tiền trong tháng " + monthStr + ": " + averagePrice);
                } else {
                    JOptionPane.showMessageDialog(this, "Không có hóa đơn trong tháng " + monthStr);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Nhập tháng không hợp lệ. Vui lòng nhập một số từ 1 đến 12.");
            }
        }
    }
    
    

}
