package chapter1.challenge_1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class BinarFudApp {
    static boolean exit = false;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Map untuk simpan menu
        Map<Integer, String> menu = new HashMap<>();
        menu.put(1, "Nasi Goreng ");
        menu.put(2, "Mie Goreng  ");
        menu.put(3, "Nasi + Ayam ");
        menu.put(4, "Es Teh Manis");
        menu.put(5, "Es Jeruk    ");

        // Map untuk simpan harga
        Map<String, Integer> prices = new HashMap<>();
        prices.put("Nasi Goreng ", 15000);
        prices.put("Mie Goreng  ", 13000);
        prices.put("Nasi + Ayam ", 18000);
        prices.put("Es Teh Manis", 15000);
        prices.put("Es Jeruk    ", 15000);

        //Map untuk simpan riwayat pesanan
        Map<String, Integer> ordersHistory = new LinkedHashMap<>();

        while (!exit) {
            System.out.println("------------------------");
            System.out.println("Selamat datang di BinarFud");
            System.out.println("------------------------");
            System.out.println("Silahkan pilih makanan:");
            //looping untuk menampilkan menu dan harga dari map
            for (int key : menu.keySet()) {
                System.out.println(key + ". " + menu.get(key) + " | " + prices.get(menu.get(key)));
            }
            //jika riwayat pesanan kosong, menu pesan dan bayar tidak dapat diakses
            if(!ordersHistory.isEmpty()){
                System.out.println("99. Pesan dan Bayar");
            }
            System.out.println("0. Keluar aplikasi");
            //untuk menampilkan pesanan yang telah dipilih pada menu utama
            if (!ordersHistory.isEmpty()){
                System.out.println();
                System.out.println("---- PESANAN ANDA ----");
                for (String orderedItem: ordersHistory.keySet()){
                    int qty = ordersHistory.get(orderedItem);
                    System.out.println(orderedItem + "   " + qty);
                }
            }
            System.out.print("=> ");
            try {
                int choice = scanner.nextInt();
                if (choice == 0) {
                    exit = true;
                } else if (choice == 99) {
                    // Panggil method confirmAndPay untuk pesan dan bayar
                    confirmAndPay(prices, ordersHistory);
                } else if (menu.containsKey(choice)) {
                    String selectedMenu = menu.get(choice);
                    System.out.println("------------------------");
                    System.out.println("Berapa Pesanan Anda");
                    System.out.println("------------------------");
                    System.out.println(selectedMenu + " | " + prices.get(selectedMenu));
                    System.out.println("(input 0 untuk kembali)");
                    System.out.print("qty => ");
                    //user input jumlah pesanan
                    int qty = scanner.nextInt();
                    if (qty > 0) {
                        addOrder(selectedMenu, qty, ordersHistory);
                    }else if (qty < 0){
                        System.out.println("Pesanan harus lebih dari 0");
                    }else{
                        continue;
                    }
                } else {
                    System.out.println("Tidak valid, pilih menu yang tersedia");
                }
            } catch (java.util.InputMismatchException e){
                System.out.println("Pilihan tidak valid (masukkan angka)");
                scanner.nextLine();
            }

        }
        scanner.close();
    }

    private static void confirmAndPay(Map<String, Integer> prices, Map<String, Integer> ordersHistory) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("------------------------");
        System.out.println("Konfirmasi & Pembayaran");
        System.out.println("------------------------");
        int total = 0;
        //looping isi map ordersHistory dan map prices untuk dapat seluruh data pesanan + harga
        for (String orderedItem : ordersHistory.keySet()) {
            int qty = ordersHistory.get(orderedItem);
            int price = prices.get(orderedItem);
            int subtotal = qty * price;
            System.out.println(orderedItem + "   " + qty + "   " + subtotal);
            total += subtotal;
        }
        totalSeparator();
        System.out.println("Total  \t\t\t " + total);
        System.out.println("1. Konfirmasi dan Bayar");
        System.out.println("2. Kembali ke menu utama");
        System.out.println("0. Keluar aplikasi");
        System.out.print("=> ");
        int choice = scanner.nextInt();
        if (choice == 1) {
            // Panggil method generateReceipt untuk generate struk pemesanan
            generateReceipt(prices, ordersHistory, total);
            ordersHistory.clear();
        }else if(choice == 0){
            exit = true;
        }else if(choice == 2){
            //Logic agar user bisa memilih lanjutkan pesanan atau mulai dari awal
            System.out.println("Apakah ingin lanjutkan pesanan?");
            System.out.println("1. Ya, lanjutkan pesanan saya");
            System.out.println("2. Tidak, hapus pesanan saya dan mulai dari awal");
            System.out.println("0. Keluar aplikasi");
            System.out.print("=> ");
            int anotherChoice = scanner.nextInt();
            if (anotherChoice == 2){
                ordersHistory.clear();
                System.out.println("Silahkan pesan kembali");
            }else if(anotherChoice == 1){
                System.out.println("Silahkan lanjutkan pesanan");
            }else if(anotherChoice == 0){
                exit = true;
            }else{
                System.out.println("Pilihan tidak valid");
            }
        }else{
            System.out.println("Pilihan tidak valid");
        }

    }

    private static void addOrder(String menuItem, int qty, Map<String, Integer> ordersHistory) {
        // Logic untuk input pesanan + qty ke map OrderHistory
        if (ordersHistory.containsKey(menuItem)) {
            int currentQty = ordersHistory.get(menuItem);
            ordersHistory.put(menuItem, currentQty + qty);
        } else {
            ordersHistory.put(menuItem, qty);
        }
    }

    private static void generateReceipt(Map<String, Integer> prices, Map<String, Integer> ordersHistory, int total) {
        // Logic untuk generate struk ke file receipt.txt
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("receipt.txt"))) {
            writer.write("------------------------");
            writer.newLine();
            writer.write("BinarFud");
            writer.newLine();
            writer.write("------------------------");
            writer.newLine();
            writer.newLine();
            writer.write("Terimakasih sudah memesan");
            writer.newLine();
            writer.write("di BinarFud");
            writer.newLine();
            writer.newLine();

            // looping untuk tampilkan seluruh data pesanan dari map orderHistory beserta harga totalnya
            for (String orderedItem : ordersHistory.keySet()) {
                int qty = ordersHistory.get(orderedItem);
                int price = prices.get(orderedItem);
                int subtotal = qty * price;
                writer.write(orderedItem + "   " + qty + "   " + subtotal);
                writer.newLine();
            }
            writer.write("------------------------+");
            writer.newLine();
            writer.write("Total  \t\t\t" + total);
            writer.newLine();
            writer.newLine();
            writer.write("Pembayaran : BinarCash");
            writer.newLine();
            writer.newLine();
            writer.write("------------------------");
            writer.newLine();
            writer.write("Simpan struk ini sebagai");
            writer.newLine();
            writer.write("bukti pembayaran");
            writer.newLine();
            writer.write("------------------------");

            // Logic untuk menampilkan struk di terminal (selain sudah di write ke receipt.txt)
            System.out.println("Struk pembayaran telah disimpan di 'receipt.txt'.");
            System.out.println("Terimakasih sudah memesan di BinarFud");
            System.out.println();
            System.out.println("Berikut pesanan anda");
            System.out.println("------------------------");
            for (String orderedItem : ordersHistory.keySet()) {
                int qty = ordersHistory.get(orderedItem);
                int price = prices.get(orderedItem);
                int subtotal = qty * price;
                System.out.print(orderedItem + "   " + qty + "   " + subtotal);
                System.out.println();
            }
            totalSeparator();
            System.out.println("Total  \t\t\t" + total);
            System.out.println();
            System.out.println("Pembayaran menggunakan Binar Cash");
            exit = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void totalSeparator() {
        // Dibuat biar tidak hardcode setiap penjumlahan menu
        System.out.println("-----------------------+");
    }
}
