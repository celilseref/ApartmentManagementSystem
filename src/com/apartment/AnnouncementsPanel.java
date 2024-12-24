package com.apartment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class AnnouncementsPanel extends JPanel {
    private final ApartmentManager manager;
    private JTable announcementsTable;
    private DefaultTableModel tableModel;
    private JTextField idField;
    private JTextField titleField;
    private JTextArea contentArea;
    private JButton deleteButton;

    public AnnouncementsPanel() {
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
        titleField = new JTextField(20);
        contentArea = new JTextArea(5, 20);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);

        // Tablo modeli
        String[] columnNames = {"Duyuru ID", "Başlık", "İçerik", "Tarih"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        announcementsTable = new JTable(tableModel);
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Duyuru Bilgileri"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Form alanları
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // ID
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
        fieldsPanel.add(new JLabel("Duyuru ID:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        fieldsPanel.add(idField, gbc);

        // Başlık
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        fieldsPanel.add(new JLabel("Başlık:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        fieldsPanel.add(titleField, gbc);

        // İçerik
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
        fieldsPanel.add(new JLabel("İçerik:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        JScrollPane contentScroll = new JScrollPane(contentArea);
        contentScroll.setPreferredSize(new Dimension(300, 100));
        fieldsPanel.add(contentScroll, gbc);

        formPanel.add(fieldsPanel, BorderLayout.CENTER);

        // Butonlar Paneli
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton addButton = new JButton("Duyuru Ekle");
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
        addButton.addActionListener(e -> addAnnouncement());
        updateButton.addActionListener(e -> updateAnnouncement());
        deleteButton.addActionListener(e -> deleteAnnouncement());
        clearButton.addActionListener(e -> clearFields());

        // Initially disable delete button
        deleteButton.setEnabled(false);

        return formPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Duyurular Listesi"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        announcementsTable.setRowHeight(25);
        announcementsTable.getTableHeader().setReorderingAllowed(false);
        announcementsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Tablo seçim listener'ı
        announcementsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = announcementsTable.getSelectedRow();
                deleteButton.setEnabled(selectedRow != -1);
                if (selectedRow != -1) {
                    loadAnnouncementToForm(selectedRow);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(announcementsTable);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private void addAnnouncement() {
        try {
            validateFields();

            int id = Integer.parseInt(idField.getText());
            String title = titleField.getText();
            String content = contentArea.getText();

            Announcement announcement = new Announcement(id, title, content);
            manager.addAnnouncement(announcement);
            refreshTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "Duyuru başarıyla eklendi.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Lütfen ID'yi sayısal değer olarak girin!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateAnnouncement() {
        int selectedRow = announcementsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Lütfen güncellenecek duyuruyu seçin!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            validateFields();

            int id = Integer.parseInt(idField.getText());
            String title = titleField.getText();
            String content = contentArea.getText();

            Announcement announcement = new Announcement(id, title, content);
            manager.updateAnnouncement(announcement);

            refreshTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "Duyuru başarıyla güncellendi.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Lütfen ID'yi sayısal değer olarak girin!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteAnnouncement() {
        int selectedRow = announcementsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Lütfen silinecek duyuruyu seçin!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int response = JOptionPane.showConfirmDialog(this,
                "Duyuruyu silmek istediğinizden emin misiniz?",
                "Duyuru Silme",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            int announcementId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            manager.deleteAnnouncement(announcementId);
            refreshTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "Duyuru başarıyla silindi.");
        }
    }

    private void validateFields() {
        if (idField.getText().trim().isEmpty() ||
                titleField.getText().trim().isEmpty() ||
                contentArea.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Tüm alanları doldurun!");
        }
    }

    private void clearFields() {
        idField.setText("");
        titleField.setText("");
        contentArea.setText("");
        announcementsTable.clearSelection();
        deleteButton.setEnabled(false);
    }

    private void loadAnnouncementToForm(int row) {
        idField.setText(tableModel.getValueAt(row, 0).toString());
        titleField.setText(tableModel.getValueAt(row, 1).toString());
        contentArea.setText(tableModel.getValueAt(row, 2).toString());
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Announcement announcement : manager.getAllAnnouncements()) {
            Object[] row = {
                    announcement.getId(),
                    announcement.getTitle(),
                    announcement.getContent(),
                    announcement.getDate().format(formatter)
            };
            tableModel.addRow(row);
        }
    }
}