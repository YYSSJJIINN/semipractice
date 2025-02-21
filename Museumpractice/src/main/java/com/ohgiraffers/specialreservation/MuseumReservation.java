package com.ohgiraffers.specialreservation;

import java.sql.*;
import java.util.Scanner;

public class MuseumReservation {

        private static final String URL = "jdbc:mysql://localhost:3306/museum_db";
        private static final String USER = "root";
        private static final String PASSWORD = "your_password";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. 회원가입\n2. 특별전 예약\n3. 예약 조회");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                registerUser(scanner);
                break;
            case 2:
                makeReservation(scanner);
                break;
            case 3:
                viewReservations(scanner);
                break;
            default:
                System.out.println("잘못된 입력입니다.");
        }
        scanner.close();
    }

    private static void registerUser(Scanner scanner) {
        System.out.print("아이디: ");
        String username = scanner.nextLine();
        System.out.print("이메일: ");
        String email = scanner.nextLine();
        System.out.print("회원 유형 (LOCAL / FOREIGN): ");
        String role = scanner.nextLine().toUpperCase();

        if (!role.equals("LOCAL") && !role.equals("FOREIGN")) {
            System.out.println("잘못된 회원 유형입니다.");
            return;
        }

        String insertUserSQL = "INSERT INTO users (username, email, role) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(insertUserSQL)) {
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, role);
            pstmt.executeUpdate();
            System.out.println("회원가입 완료!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void makeReservation(Scanner scanner) {
        System.out.print("아이디: ");
        String username = scanner.nextLine();
        System.out.print("예약할 전시회 (South America / Middle East): ");
        String exhibition = scanner.nextLine();

        String getUserIdSQL = "SELECT id FROM users WHERE username = ?";
        String insertReservationSQL = "INSERT INTO reservations (user_id, exhibition) VALUES (?, ?)";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC 드라이버를 찾을 수 없습니다.");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement getUserStmt = conn.prepareStatement(getUserIdSQL);
             PreparedStatement insertStmt = conn.prepareStatement(insertReservationSQL)) {
            getUserStmt.setString(1, username);
            ResultSet rs = getUserStmt.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("id");
                insertStmt.setInt(1, userId);
                insertStmt.setString(2, exhibition);
                insertStmt.executeUpdate();
                System.out.println("예약 완료!");
            } else {
                System.out.println("회원 정보를 찾을 수 없습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewReservations(Scanner scanner) {
        System.out.print("아이디: ");
        String username = scanner.nextLine();

        String checkRoleSQL = "SELECT role FROM users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement checkRoleStmt = conn.prepareStatement(checkRoleSQL)) {
            checkRoleStmt.setString(1, username);
            ResultSet roleRs = checkRoleStmt.executeQuery();

            if (roleRs.next()) {
                String role = roleRs.getString("role");

                if ("ADMIN".equals(role)) {
                    viewAllReservations(conn);
                } else {
                    viewUserReservations(conn, username);
                }
            } else {
                System.out.println("회원 정보를 찾을 수 없습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 일반 회원의 예약 조회
    private static void viewUserReservations(Connection conn, String username) throws SQLException {
        String getReservationsSQL = "SELECT r.exhibition, r.reservation_date FROM reservations r " +
                "JOIN users u ON r.user_id = u.id WHERE u.username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(getReservationsSQL)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            boolean hasReservations = false;
            while (rs.next()) {
                hasReservations = true;
                System.out.println("전시회: " + rs.getString("exhibition") + ", 예약일: " + rs.getTimestamp("reservation_date"));
            }
            if (!hasReservations) {
                System.out.println("예약 내역이 없습니다.");
            }
        }
    }

    // 관리자의 전체 예약 조회
    private static void viewAllReservations(Connection conn) throws SQLException {
        String getAllReservationsSQL = "SELECT u.username, u.role, r.exhibition, r.reservation_date FROM reservations r " +
                "JOIN users u ON r.user_id = u.id";
        try (PreparedStatement pstmt = conn.prepareStatement(getAllReservationsSQL)) {
            ResultSet rs = pstmt.executeQuery();
            System.out.println("===== 모든 예약 내역 =====");
            while (rs.next()) {
                System.out.println("회원: " + rs.getString("username") +
                        " (유형: " + rs.getString("role") + ")" +
                        ", 전시회: " + rs.getString("exhibition") +
                        ", 예약일: " + rs.getTimestamp("reservation_date"));
            }
        }
    }
}
