import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Connection myConn = null;
        Scanner scanner = new Scanner(System.in);

        try {
            myConn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/project",
                    "root",
                    ""
            );
            System.out.println("Genial, nos conectamos");

            int option;
            do {
                System.out.println("\nMenú:");
                System.out.println("1. Insertar empleado");
                System.out.println("2. Consultar empleados");
                System.out.println("3. Actualizar empleado");
                System.out.println("4. Eliminar empleado");
                System.out.println("5. Salir");
                System.out.print("Elige una opción: ");
                option = scanner.nextInt();
                scanner.nextLine(); // Limpiar el buffer

                switch (option) {
                    case 1:
                        System.out.print("Nombre: ");
                        String firstName = scanner.nextLine();
                        System.out.print(" Primer Apellido: ");
                        String paSurname = scanner.nextLine();
                        System.out.print(" Segundo Apellido : ");
                        String maSurname = scanner.nextLine();
                        System.out.print("Email: ");
                        String email = scanner.nextLine();
                        System.out.print("Salario: ");
                        double salary = scanner.nextDouble();
                        insertarEmpleado(myConn, firstName, paSurname, maSurname, email, salary);
                        break;
                    case 2:
                        consultarEmpleados(myConn);
                        break;
                    case 3:
                        System.out.print("ID del empleado a actualizar: ");
                        int idToUpdate = scanner.nextInt();
                        scanner.nextLine(); // Limpiar el buffer
                        System.out.print("Nuevo Nombre: ");
                        String newFirstName = scanner.nextLine();
                        System.out.print("Nuevo Segundo Apellido: ");
                        String newPaSurname = scanner.nextLine();
                        System.out.print("Nuevo Segundo Apellido: ");
                        String newMaSurname = scanner.nextLine();
                        System.out.print("Nuevo Email: ");
                        String newEmail = scanner.nextLine();
                        System.out.print("Nuevo Salario: ");
                        double newSalary = scanner.nextDouble();
                        actualizarEmpleado(myConn, idToUpdate, newFirstName, newPaSurname, newMaSurname, newEmail, newSalary);
                        break;
                    case 4:
                        System.out.print("ID del empleado a eliminar: ");
                        int idToDelete = scanner.nextInt();
                        eliminarEmpleado(myConn, idToDelete);
                        break;
                    case 5:
                        System.out.println("Saliendo...");
                        break;
                    default:
                        System.out.println("Opción no válida, intenta de nuevo.");
                }
            } while (option != 5);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Algo salió mal :(");
        } finally {
            try {
                if (myConn != null) myConn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para insertar un empleado
    private static void insertarEmpleado(Connection conexion, String firstName, String paSurname, String maSurname, String email, double salary)
            throws SQLException {
        String sql = "INSERT INTO employees (first_name, pa_surname, ma_surname, email, salary) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, paSurname);
            pstmt.setString(3, maSurname);
            pstmt.setString(4, email);
            pstmt.setDouble(5, salary);
            pstmt.executeUpdate();
            System.out.println("Empleado insertado correctamente!");
        }
    }

    // Método para consultar empleados
    private static void consultarEmpleados(Connection conexion) throws SQLException {
        String sql = "SELECT * FROM employees";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.printf("ID: %d, Nombre: %s %s %s, Email: %s, Salary: %.2f%n",
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("pa_surname"),
                        rs.getString("ma_surname"),
                        rs.getString("email"),
                        rs.getDouble("salary"));
            }
        }
    }

    // Método para actualizar un empleado
    private static void actualizarEmpleado(Connection conexion, int id, String firstName, String paSurname, String maSurname, String email, double salary)
            throws SQLException {
        String sql = "UPDATE employees SET first_name = ?, pa_surname = ?, ma_surname = ?, email = ?, salary = ? WHERE id = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, paSurname);
            pstmt.setString(3, maSurname);
            pstmt.setString(4, email);
            pstmt.setDouble(5, salary);
            pstmt.setInt(6, id);
            pstmt.executeUpdate();
            System.out.println("Empleado actualizado correctamente!");
        }
    }

    // Método para eliminar un empleado
    private static void eliminarEmpleado(Connection conexion, int id) throws SQLException {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Empleado eliminado correctamente!");
        }
    }
}
