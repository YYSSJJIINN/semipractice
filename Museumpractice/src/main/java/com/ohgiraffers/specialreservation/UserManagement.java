package com.ohgiraffers.specialreservation;

import java.sql.*;
import java.util.Scanner;

public class UserManagement {

    public class UserManager {
        private static final String URL = "jdbc:mysql://localhost:3306/cheetahlee_cake_db";
        private static final String USER = "root";
        private static final String PASSWORD = "your_password";

        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("1. 회원가입\n2. 기존회원 조회\n3. 아이디 찾기\n4. 비밀번호 찾기");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    registerUser(scanner);
                    break;
                case 2:
                    getAllUsers();
                    break;
                case 3:
                    findUsername(scanner);
                    break;
                case 4:
                    findPassword(scanner);
                    break;
                default:
                    System.out.println("잘못된 입력입니다.");
            }
            scanner.close();
        }

        private static void registerUser(Scanner scanner) {
            System.out.print("아이디: ");
            String username = scanner.nextLine();
            System.out.print("비밀번호: ");
            String password = scanner.nextLine();
            System.out.print("이메일: ");
            String email = scanner.nextLine();
            System.out.print("전화번호: ");
            String phone = scanner.nextLine();

            String sql = "INSERT INTO users (username, password, email, phone) VALUES (?, ?, ?, ?)";

            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.setString(3, email);
                pstmt.setString(4, phone);
                pstmt.executeUpdate();
                System.out.println("회원가입 성공!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private static void getAllUsers() {
            String sql = "SELECT * FROM users";
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("id") + ", 아이디: " + rs.getString("username") + ", 이메일: " + rs.getString("email") + ", 전화번호: " + rs.getString("phone"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private static void findUsername(Scanner scanner) {
            System.out.print("이메일 입력: ");
            String email = scanner.nextLine();
            String sql = "SELECT username FROM users WHERE email = ?";

            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, email);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    System.out.println("아이디: " + rs.getString("username"));
                } else {
                    System.out.println("이메일에 해당하는 아이디가 없습니다.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private static void findPassword(Scanner scanner) {
            System.out.print("아이디 입력: ");
            String username = scanner.nextLine();
            System.out.print("이메일 입력: ");
            String email = scanner.nextLine();
            String sql = "SELECT password FROM users WHERE username = ? AND email = ?";

            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                pstmt.setString(2, email);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    System.out.println("비밀번호: " + rs.getString("password"));
                } else {
                    System.out.println("정보가 일치하는 계정을 찾을 수 없습니다.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
