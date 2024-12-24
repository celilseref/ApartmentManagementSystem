package com.apartment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.ZoneId;

public class PaymentsPanel extends JPanel {
    private final ApartmentManager manager;
    private JTable paymentsTable;
    private DefaultTableModel tableModel;
    private JTextField idField;
    private JTextField residentIdField;
    private JTextField amountField;
    private JComboBox<PaymentType> typeComboBox;
    private JButton deleteButton;

    public PaymentsPanel() {
        manager = ApartmentManager.getInstance();
        initComponents();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form Panel - Üst kısım
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.NORTH);

        // Tablo Panel - Alt kısım
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);

        refreshTable();
    }

    private void initComponents() {
        idField = new JTextField(20);
        residentIdField = new JTextField(20);
        amountField = new JTextField(20);
        typeComboBox = new JComboBox<>(PaymentType.values());

        // Tablo modeli
        String[] columnNames = {"Ödeme ID", "Sakin ID", "Sakin Adı", "Miktar (TL)", "Ödeme Tipi", "Tarih"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        paymentsTable = new JTable(tableModel);
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Ödeme Bilgileri"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Form alanları
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // ID
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
        fieldsPanel.add(new JLabel("Ödeme ID:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        fieldsPanel.add(idField, gbc);

        // Sakin ID
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        fieldsPanel.add(new JLabel("Sakin ID:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        fieldsPanel.add(residentIdField, gbc);

        // Miktar
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
        fieldsPanel.add(new JLabel("Miktar (TL):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        fieldsPanel.add(amountField, gbc);

        // Ödeme Tipi
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0;
        fieldsPanel.add(new JLabel("Ödeme Tipi:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        fieldsPanel.add(typeComboBox, gbc);

        formPanel.add(fieldsPanel, BorderLayout.CENTER);

        // Butonlar Paneli
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton addButton = new JButton("Ödeme Ekle");
        JButton updateButton = new JButton("Güncelle");
        deleteButton = new JButton("Sil");
        JButton clearButton = new JButton("Temizle");

        // Butonlara stil ekle
        Dimension buttonSize = new Dimension(120, 30);
        addButton.setPreferredSize(buttonSize);
        updateButton.setPreferredSize(buttonSize);
        deleteButton.setPreferredSize(buttonSize);
        clearButton.setPreferredSize(buttonSize);

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        formPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Button listeners
        addButton.addActionListener(e -> addPayment());
        updateButton.addActionListener(e -> updatePayment());
        deleteButton.addActionListener(e -> deletePayment());
        clearButton.addActionListener(e -> clearFields());

        // Initially disable delete button
        deleteButton.setEnabled(false);

        return formPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Ödemeler Listesi"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        paymentsTable.setRowHeight(25);
        paymentsTable.getTableHeader().setReorderingAllowed(false);
        paymentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Tablo seçim listener'ı
        paymentsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = paymentsTable.getSelectedRow();
                deleteButton.setEnabled(selectedRow != -1);
                if (selectedRow != -1) {
                    loadPaymentToForm(selectedRow);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(paymentsTable);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private void addPayment() {
        try {
            validateFields();

            int id = Integer.parseInt(idField.getText());
            int residentId = Integer.parseInt(residentIdField.getText());
            double amount = Double.parseDouble(amountField.getText());
            PaymentType type = (PaymentType) typeComboBox.getSelectedItem();

            // Sakin kontrolü
            Resident resident = manager.getResident(residentId);
            if (resident == null) {
                JOptionPane.showMessageDialog(this,
                        "Belirtilen ID'ye sahip sakin bulunamadı!",
                        "Hata",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Payment payment = new Payment(id, residentId, amount, type);
            manager.addPayment(payment);
            refreshTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "Ödeme başarıyla eklendi.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Lütfen sayısal değerleri doğru formatta girin!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePayment() {
        int selectedRow = paymentsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Lütfen güncellenecek ödemeyi seçin!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            validateFields();

            int id = Integer.parseInt(idField.getText());
            int residentId = Integer.parseInt(residentIdField.getText());
            double amount = Double.parseDouble(amountField.getText());
            PaymentType type = (PaymentType) typeComboBox.getSelectedItem();

            Payment payment = new Payment(id, residentId, amount, type);
            // Update payment in manager
            manager.updatePayment(payment);

            refreshTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "Ödeme başarıyla güncellendi.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Lütfen sayısal değerleri doğru formatta girin!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletePayment() {
        int selectedRow = paymentsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Lütfen silinecek ödemeyi seçin!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int response = JOptionPane.showConfirmDialog(this,
                "Ödemeyi silmek istediğinizden emin misiniz?",
                "Ödeme Silme",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            int paymentId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            manager.deletePayment(paymentId);
            refreshTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "Ödeme başarıyla silindi.");
        }
    }

    private void validateFields() {
        if (idField.getText().trim().isEmpty() ||
                residentIdField.getText().trim().isEmpty() ||
                amountField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Tüm alanları doldurun!");
        }
    }

    private void clearFields() {
        idField.setText("");
        residentIdField.setText("");
        amountField.setText("");
        typeComboBox.setSelectedIndex(0);
        paymentsTable.clearSelection();
        deleteButton.setEnabled(false);
    }

    private void loadPaymentToForm(int row) {
        idField.setText(tableModel.getValueAt(row, 0).toString());
        residentIdField.setText(tableModel.getValueAt(row, 1).toString());
        amountField.setText(tableModel.getValueAt(row, 3).toString().replace(" TL", ""));
        typeComboBox.setSelectedItem(PaymentType.valueOf(tableModel.getValueAt(row, 4).toString()));
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (Payment payment : manager.getAllPayments()) {
            Resident resident = manager.getResident(payment.getResidentId());
            String residentName = resident != null ? resident.getName() : "Bilinmeyen";

            // LocalDateTime'ı Date'e çevirme
            Date date = Date.from(payment.getDate().atZone(ZoneId.systemDefault()).toInstant());

            Object[] row = {
                    payment.getId(),
                    payment.getResidentId(),
                    residentName,
                    String.format("%.2f TL", payment.getAmount()),
                    payment.getType(),
                    dateFormat.format(date)
            };
            tableModel.addRow(row);
        }
    }
}