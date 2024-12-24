package com.apartment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DuesPanel extends JPanel {
    private final ApartmentManager manager;
    private JTable duesTable;
    private DefaultTableModel tableModel;
    private JTextField idField;
    private JTextField apartmentNoField;
    private JTextField residentNameField;
    private JTextField amountField;
    private JTextField dueDateField;
    private JCheckBox isPaidCheckBox;

    public DuesPanel() {
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
        apartmentNoField = new JTextField(20);
        residentNameField = new JTextField(20);
        amountField = new JTextField(20);
        dueDateField = new JTextField(20);
        isPaidCheckBox = new JCheckBox("Ödendi");

        // Tablo modeli
        String[] columnNames = {"ID", "Daire No", "Sakin Adı", "Tutar (TL)", "Son Ödeme Tarihi", "Durum"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        duesTable = new JTable(tableModel);
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Aidat Bilgileri"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Form alanları
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // ID
        gbc.gridx = 0; gbc.gridy = 0;
        fieldsPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        fieldsPanel.add(idField, gbc);

        // Daire No
        gbc.gridx = 0; gbc.gridy = 1;
        fieldsPanel.add(new JLabel("Daire No:"), gbc);
        gbc.gridx = 1;
        fieldsPanel.add(apartmentNoField, gbc);

        // Sakin Adı
        gbc.gridx = 0; gbc.gridy = 2;
        fieldsPanel.add(new JLabel("Sakin Adı:"), gbc);
        gbc.gridx = 1;
        fieldsPanel.add(residentNameField, gbc);

        // Tutar
        gbc.gridx = 0; gbc.gridy = 3;
        fieldsPanel.add(new JLabel("Tutar (TL):"), gbc);
        gbc.gridx = 1;
        fieldsPanel.add(amountField, gbc);

        // Son Ödeme Tarihi
        gbc.gridx = 0; gbc.gridy = 4;
        fieldsPanel.add(new JLabel("Son Ödeme Tarihi (GG.AA.YYYY):"), gbc);
        gbc.gridx = 1;
        fieldsPanel.add(dueDateField, gbc);

        // Ödeme Durumu
        gbc.gridx = 0; gbc.gridy = 5;
        fieldsPanel.add(new JLabel("Ödeme Durumu:"), gbc);
        gbc.gridx = 1;
        fieldsPanel.add(isPaidCheckBox, gbc);

        formPanel.add(fieldsPanel, BorderLayout.CENTER);

        // Butonlar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton addButton = new JButton("Ekle");
        JButton updateButton = new JButton("Güncelle");
        JButton deleteButton = new JButton("Sil");
        JButton clearButton = new JButton("Temizle");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        addButton.addActionListener(e -> addDues());
        updateButton.addActionListener(e -> updateDues());
        deleteButton.addActionListener(e -> deleteDues());
        clearButton.addActionListener(e -> clearFields());

        formPanel.add(buttonPanel, BorderLayout.SOUTH);
        return formPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Aidatlar Listesi"));

        duesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        duesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = duesTable.getSelectedRow();
                if (selectedRow >= 0) {
                    loadDuesToForm(selectedRow);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(duesTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private void addDues() {
        try {
            validateFields();
            int id = Integer.parseInt(idField.getText());
            int apartmentNo = Integer.parseInt(apartmentNoField.getText());
            String residentName = residentNameField.getText();
            double amount = Double.parseDouble(amountField.getText());
            LocalDate dueDate = LocalDate.parse(dueDateField.getText(),
                    DateTimeFormatter.ofPattern("dd.MM.yyyy"));

            Dues dues = new Dues(id, apartmentNo, residentName, amount, dueDate);
            dues.setPaid(isPaidCheckBox.isSelected());

            manager.addDues(dues);
            refreshTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "Aidat başarıyla eklendi.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lütfen tüm alanları doğru formatta doldurun!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateDues() {
        int selectedRow = duesTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Lütfen güncellenecek aidatı seçin!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            validateFields();
            int id = Integer.parseInt(idField.getText());
            int apartmentNo = Integer.parseInt(apartmentNoField.getText());
            String residentName = residentNameField.getText();
            double amount = Double.parseDouble(amountField.getText());
            LocalDate dueDate = LocalDate.parse(dueDateField.getText(),
                    DateTimeFormatter.ofPattern("dd.MM.yyyy"));

            Dues dues = new Dues(id, apartmentNo, residentName, amount, dueDate);
            dues.setPaid(isPaidCheckBox.isSelected());

            manager.updateDues(dues);
            refreshTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "Aidat başarıyla güncellendi.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lütfen tüm alanları doğru formatta doldurun!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteDues() {
        int selectedRow = duesTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Lütfen silinecek aidatı seçin!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Seçili aidatı silmek istediğinizden emin misiniz?",
                "Aidat Silme",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            manager.deleteDues(id);
            refreshTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "Aidat başarıyla silindi.");
        }
    }

    private void validateFields() {
        if (idField.getText().isEmpty() ||
                apartmentNoField.getText().isEmpty() ||
                residentNameField.getText().isEmpty() ||
                amountField.getText().isEmpty() ||
                dueDateField.getText().isEmpty()) {
            throw new IllegalArgumentException("Tüm alanları doldurun!");
        }
    }

    private void clearFields() {
        idField.setText("");
        apartmentNoField.setText("");
        residentNameField.setText("");
        amountField.setText("");
        dueDateField.setText("");
        isPaidCheckBox.setSelected(false);
        duesTable.clearSelection();
    }

    private void loadDuesToForm(int row) {
        idField.setText(tableModel.getValueAt(row, 0).toString());
        apartmentNoField.setText(tableModel.getValueAt(row, 1).toString());
        residentNameField.setText(tableModel.getValueAt(row, 2).toString());
        amountField.setText(tableModel.getValueAt(row, 3).toString().replace(" TL", ""));
        dueDateField.setText(tableModel.getValueAt(row, 4).toString());
        isPaidCheckBox.setSelected(tableModel.getValueAt(row, 5).toString().equals("Ödendi"));
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        for (Dues dues : manager.getAllDues()) {
            Object[] row = {
                    dues.getId(),
                    dues.getApartmentNo(),
                    dues.getResidentName(),
                    String.format("%.2f TL", dues.getAmount()),
                    dues.getDueDate().format(formatter),
                    dues.isPaid() ? "Ödendi" : "Ödenmedi"
            };
            tableModel.addRow(row);
        }
    }
}