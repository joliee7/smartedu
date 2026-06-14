package View;

import javax.swing.*;
import java.awt.*;

public class DetailPembayaranSiswa extends JFrame {
    public DetailPembayaranSiswa(String namaSiswa, Object[] data) {
        this.setTitle("Detail Pemabayaran " + namaSiswa);
        this.setSize(400, 300);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new GridBagLayout());
        this.getContentPane().setBackground(Color.decode("#D9E8EE"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        String[] labels = {
                "Tanggal Transaksi", "ID Siswa", "Nama Siswa", "Nominal", "Metode Bayar", "Periode Mulai", "Periode Akhir"
        };

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            this.add(new JLabel(labels[i] + " :"), gbc);

            gbc.gridx = 1;
            JTextField field = new JTextField(data[i].toString(), 20);
            field.setEditable(false);
            this.add(field, gbc);
        }
    }
}
