import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MainFrame extends JFrame {
    final private Font mainFont = new Font("Arial", Font.BOLD, 18);
    JTextField tfBarang, tfPenitip, tfHari;
    JLabel lbKonfirmasi;
    DefaultTableModel tableModel;
    
    public static void main(String[] args) {
        MainFrame test = new MainFrame();
        test.Tampil();
    
    }
    public void Tampil() {

        // Form Penambahan Barang
        JLabel lbBarang = new JLabel("Barang");
        lbBarang.setFont(mainFont);

        tfBarang = new JTextField();
        tfBarang.setFont(mainFont);

        JLabel lbPenitip = new JLabel("Penitip");
        lbPenitip.setFont(mainFont);

        tfPenitip = new JTextField();
        tfPenitip.setFont(mainFont);

        JLabel lbHari = new JLabel("Berapa Hari");
        lbHari.setFont(mainFont);

        tfHari = new JTextField();
        tfHari.setFont(mainFont);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 1, 3, 3));
        formPanel.setOpaque(false);
        formPanel.add(lbBarang);
        formPanel.add(tfBarang);
        formPanel.add(lbPenitip);
        formPanel.add(tfPenitip);
        formPanel.add(lbHari);
        formPanel.add(tfHari);

        // konfirmasi box
        lbKonfirmasi = new JLabel();
        lbKonfirmasi.setFont(mainFont);

        // Button
        JButton btnSimpan = new JButton("Simpan");
        btnSimpan.setFont(mainFont);
        btnSimpan.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                String Barang = tfBarang.getText();
                String Penitip = tfPenitip.getText();
                String Hari = tfHari.getText();
                // lbKonfirmasi.setText(Penitip + " menitipkan barang " + Barang + " selama " + Hari + " hari.");

                try {
                    Query("INSERT INTO penitipan.daftar (barang,penitip,hari) VALUES ('" + Barang + "','"
                            + Penitip + "'," + Hari + ")");
                    tampilkanData();
                } catch (Exception ex) {
                    // TODO: handle exception
                }

            }

        });
        
        JButton btnHapus = new JButton("Hapus");
        btnHapus.setFont(mainFont);
        btnHapus.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                try {
                    Query("DELETE FROM penitipan.daftar");
                } catch (Exception ev) {
                    // TODO: handle exception
                }
            }
            
        });

        JButton btnKosong = new JButton("Baru");
        btnKosong.setFont(mainFont);
        btnKosong.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                tfBarang.setText("");
                tfPenitip.setText("");
                tfHari.setText("");
                lbKonfirmasi.setText("");
            }

        });

        // Table
        tableModel = new DefaultTableModel(new String[] {"Barang", "Penitip", "Hari" }, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        JTable tabel = new JTable(tableModel);
        tabel.setFont(mainFont);
        tableModel.addRow(new Object[]{"Barang","Penitip","Hari"});
        JScrollPane scrollPane = new JScrollPane(tabel);
        scrollPane.getVerticalScrollBar();

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new GridLayout(1, 2, 3, 3));
        btnPanel.add(btnSimpan);
        btnPanel.add(btnKosong);
        btnPanel.add(btnHapus);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(255, 255, 255));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(lbKonfirmasi, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        mainPanel.add(tabel, BorderLayout.CENTER);

        add(mainPanel);

        setTitle("Daftar Barang");
        setSize(500, 400);
        setMinimumSize(new Dimension(400, 300));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }


    public void Query(String query) {
        Connection conn = null;
        Statement stm = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/penitipan", "root", "1234");
            stm = conn.createStatement();
            stm.executeUpdate(query);
            JOptionPane.showMessageDialog(null, "Data diupdate");
        } catch (Exception e) {
            // TODO: handle exception
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void tampilkanData() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/penitipan", "root", "1234");
                Statement stm = conn.createStatement();
                ResultSet rs = stm.executeQuery("SELECT * FROM daftar")) {

            tableModel.setRowCount(1);
            while (rs.next()) {
                String barang = rs.getString("barang");
                String penitip = rs.getString("penitip");
                int hari = rs.getInt("hari");
                tableModel.addRow(new Object[] { barang, penitip, hari });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
