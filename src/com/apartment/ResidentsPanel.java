package com.apartment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ResidentsPanel extends JPanel {
    private final ApartmentManager manager;
    private final JTable residentsTable;
    private final DefaultTableModel tableModel;
    private final JTextField idField;
    private final JTextField nameField;
    private final JTextField apartmentNoField;
    private final JTextField phoneField;

    public ResidentsPanel() {
        manager = ApartmentManager.getInstance();
        setLayout(new BorderLayout());

        // Tablo modeli oluşturma
        String[] columnNames = {"ID", "Ad Soyad", "Daire No", "Telefon"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tablonun düzenlenebilir olmasını engelle
            }
        };
        residentsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(residentsTable);

        // Form paneli
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        idField = new JTextField();
        nameField = new JTextField();
        apartmentNoField = new JTextField();
        phoneField = new JTextField();

        formPanel.add(new JLabel("ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Ad Soyad:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Daire No:"));
        formPanel.add(apartmentNoField);
        formPanel.add(new JLabel("Telefon:"));
        formPanel.add(phoneField);

        // Butonlar
        JButton addButton = new JButton("Ekle");
        JButton updateButton = new JButton("Güncelle");
        JButton deleteButton = new JButton("Sil");
        JButton clearButton = new JButton("Temizle");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        formPanel.add(buttonPanel);

        // Button listeners
        addButton.addActionListener(e -> addResident());
        updateButton.addActionListener(e -> updateResident());
        deleteButton.addActionListener(e -> deleteResident());
        clearButton.addActionListener(e -> clearFields());

        // Tablo seçim listener'ı
        residentsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && residentsTable.getSelectedRow() != -1) {
                loadResidentToForm(residentsTable.getSelectedRow());
            }
        });

        // Panel yerleşimi
        JPanel eastPanel = new JPanel(new BorderLayout());
        eastPanel.add(formPanel, BorderLayout.NORTH);

        add(scrollPane, BorderLayout.CENTER);
        add(eastPanel, BorderLayout.EAST);

        // Tabloyu doldur
        refreshTable();
    }

    private void addResident() {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            int apartmentNo = Integer.parseInt(apartmentNoField.getText());
            String phone = phoneField.getText();

            if (name.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Lütfen tüm alanları doldurun!",
                        "Hata",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Resident resident = new Resident(id, name, apartmentNo, phone);
            manager.addResident(resident);
            refreshTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "Sakin başarıyla eklendi.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "ID ve Daire No sayısal değer olmalıdır!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateResident() {
        int selectedRow = residentsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Lütfen güncellenecek sakini seçin!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            int apartmentNo = Integer.parseInt(apartmentNoField.getText());
            String phone = phoneField.getText();

            Resident resident = new Resident(id, name, apartmentNo, phone);
            manager.updateResident(resident);
            refreshTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "Sakin başarıyla güncellendi.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "ID ve Daire No sayısal değer olmalıdır!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteResident() {
        int selectedRow = residentsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Lütfen silinecek sakini seçin!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        Resident resident = manager.getResident(id);

        int choice = JOptionPane.showConfirmDialog(this,
                "Sakini silmek istediğinizden emin misiniz?",
                "Silme Onayı",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            manager.removeResident(resident);
            refreshTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "Sakin başarıyla silindi.");
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        apartmentNoField.setText("");
        phoneField.setText("");
        residentsTable.clearSelection();
    }

    private void loadResidentToForm(int row) {
        idField.setText(tableModel.getValueAt(row, 0).toString());
        nameField.setText(tableModel.getValueAt(row, 1).toString());
        apartmentNoField.setText(tableModel.getValueAt(row, 2).toString());
        phoneField.setText(tableModel.getValueAt(row, 3).toString());
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Resident> residents = manager.getAllResidents();
        for (Resident resident : residents) {
            Object[] row = {
                    resident.getId(),
                    resident.getName(),
                    resident.getApartmentNo(),
                    resident.getPhone()
            };
            tableModel.addRow(row);
        }
    }
}