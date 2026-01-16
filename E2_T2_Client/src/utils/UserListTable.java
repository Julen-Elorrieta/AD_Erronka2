package utils;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Componente reutilizable para mostrar listas de usuarios (alumnos o profesores)
 * Permite selección de usuarios y proporciona funcionalidad de scroll para listas largas
 */
public class UserListTable extends JPanel {
    
    private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<UserSelectionListener> selectionListeners;
    private int selectedUserId = -1;
    private String userType = "Usuarios"; // "Alumnos", "Profesores", o "Usuarios"
    
    // Colores predefinidos para diferentes tipos de usuarios
    public static final Color STUDENT_COLOR = new Color(70, 130, 180);        // Azul - Alumno
    public static final Color TEACHER_COLOR = new Color(34, 139, 34);         // Verde - Profesor
    public static final Color SELECTED_ROW = new Color(255, 215, 0);          // Dorado - Fila seleccionada
    public static final Color ALTERNATE_ROW = new Color(248, 248, 248);       // Gris muy claro - Filas alternas
    
    private String[] columnNames = {"ID", "Nombre", "Apellidos", "Email", "Tipo"};
    
    /**
     * Constructor por defecto
     */
    public UserListTable() {
        this("Usuarios");
    }
    
    /**
     * Constructor con tipo de usuario específico
     * @param userType Tipo de usuarios a mostrar ("Alumnos", "Profesores", "Usuarios")
     */
    public UserListTable(String userType) {
        this.userType = userType;
        this.selectionListeners = new ArrayList<>();
        initializeComponents();
        setupTable();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        
        // Crear modelo de tabla
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabla no editable
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Integer.class; // ID como entero para ordenación correcta
                }
                return String.class;
            }
        };
        
        table = new JTable(tableModel);
    }
    
    private void setupTable() {
        // Configuración básica de la tabla
        table.setRowHeight(35);
        table.setFont(new Font("Tahoma", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(51, 102, 153));
        table.getTableHeader().setForeground(Color.WHITE);
        
        // Configurar ancho de columnas
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);   // ID
        columnModel.getColumn(1).setPreferredWidth(120);  // Nombre
        columnModel.getColumn(2).setPreferredWidth(150);  // Apellidos
        columnModel.getColumn(3).setPreferredWidth(200);  // Email
        columnModel.getColumn(4).setPreferredWidth(80);   // Tipo
        
        // Configurar selección
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        
        // Renderer personalizado para colores de filas
        table.setDefaultRenderer(Object.class, new UserCellRenderer());
        table.setDefaultRenderer(Integer.class, new UserCellRenderer());
        
        // Listener para selección de filas
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow >= 0) {
                        selectedUserId = (Integer) tableModel.getValueAt(selectedRow, 0);
                        String userName = (String) tableModel.getValueAt(selectedRow, 1) + " " + 
                                         (String) tableModel.getValueAt(selectedRow, 2);
                        String userTypeSelected = (String) tableModel.getValueAt(selectedRow, 4);
                        
                        // Notificar a los listeners
                        notifyUserSelected(selectedUserId, userName, userTypeSelected);
                        table.repaint();
                    }
                }
            }
        });
        
        // Permitir ordenación por columnas
        table.setAutoCreateRowSorter(true);
        
        // Añadir la tabla con scroll
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(700, 400));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
        
        // Título de la tabla
        JLabel titleLabel = new JLabel("Lista de " + userType, JLabel.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        titleLabel.setForeground(new Color(51, 102, 153));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Panel de información
        add(createInfoPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel(new FlowLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder("Información"));
        
        infoPanel.add(createInfoItem("Estudiante", STUDENT_COLOR));
        infoPanel.add(createInfoItem("Profesor", TEACHER_COLOR));
        
        JLabel instructionLabel = new JLabel("Haga clic en una fila para seleccionar un usuario");
        instructionLabel.setFont(new Font("Tahoma", Font.ITALIC, 11));
        instructionLabel.setForeground(Color.GRAY);
        infoPanel.add(instructionLabel);
        
        return infoPanel;
    }
    
    private JPanel createInfoItem(String text, Color color) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        
        JPanel colorBox = new JPanel();
        colorBox.setBackground(color);
        colorBox.setPreferredSize(new Dimension(15, 15));
        colorBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        JLabel label = new JLabel(text);
        label.setFont(new Font("Tahoma", Font.PLAIN, 11));
        
        item.add(colorBox);
        item.add(label);
        
        return item;
    }
    
    /**
     * Añade un usuario a la tabla
     * @param id ID del usuario
     * @param nombre Nombre del usuario
     * @param apellidos Apellidos del usuario
     * @param email Email del usuario
     * @param tipo Tipo de usuario ("Alumno" o "Profesor")
     */
    public void addUser(int id, String nombre, String apellidos, String email, String tipo) {
        Object[] row = {id, nombre, apellidos, email, tipo};
        tableModel.addRow(row);
    }
    
    /**
     * Limpia todos los usuarios de la tabla
     */
    public void clearUsers() {
        tableModel.setRowCount(0);
        selectedUserId = -1;
    }
    
    /**
     * Carga una lista de usuarios en la tabla
     * @param users Lista de arrays de objetos con datos de usuarios
     */
    public void loadUsers(List<Object[]> users) {
        clearUsers();
        for (Object[] user : users) {
            tableModel.addRow(user);
        }
    }
    
    /**
     * Obtiene el ID del usuario seleccionado
     * @return ID del usuario seleccionado, -1 si no hay selección
     */
    public int getSelectedUserId() {
        return selectedUserId;
    }
    
    /**
     * Obtiene la información completa del usuario seleccionado
     * @return Array con [ID, Nombre, Apellidos, Email, Tipo] o null si no hay selección
     */
    public Object[] getSelectedUserInfo() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            Object[] userInfo = new Object[5];
            for (int i = 0; i < 5; i++) {
                userInfo[i] = tableModel.getValueAt(selectedRow, i);
            }
            return userInfo;
        }
        return null;
    }
    
    /**
     * Establece el tipo de usuarios mostrados en la tabla
     * @param userType Tipo de usuarios ("Alumnos", "Profesores", "Usuarios")
     */
    public void setUserType(String userType) {
        this.userType = userType;
        // Actualizar el título
        Component[] components = getComponents();
        for (Component comp : components) {
            if (comp instanceof JLabel) {
                ((JLabel) comp).setText("Lista de " + userType);
                break;
            }
        }
    }
    
    /**
     * Añade un listener para eventos de selección de usuario
     * @param listener Listener a añadir
     */
    public void addUserSelectionListener(UserSelectionListener listener) {
        selectionListeners.add(listener);
    }
    
    /**
     * Remueve un listener de eventos de selección de usuario
     * @param listener Listener a remover
     */
    public void removeUserSelectionListener(UserSelectionListener listener) {
        selectionListeners.remove(listener);
    }
    
    /**
     * Notifica a todos los listeners sobre la selección de un usuario
     */
    private void notifyUserSelected(int userId, String userName, String userType) {
        for (UserSelectionListener listener : selectionListeners) {
            listener.onUserSelected(userId, userName, userType);
        }
    }
    
    /**
     * Obtiene el número total de usuarios en la tabla
     * @return Número de usuarios
     */
    public int getUserCount() {
        return tableModel.getRowCount();
    }
    
    /**
     * Busca usuarios por nombre o apellido
     * @param searchTerm Término de búsqueda
     */
    public void filterUsers(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            table.setRowSorter(new TableRowSorter<>(tableModel));
            return;
        }
        
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchTerm, 1, 2, 3)); // Buscar en nombre, apellidos y email
        table.setRowSorter(sorter);
    }
    
    /**
     * Renderer personalizado para colorear las filas según el tipo de usuario
     */
    private class UserCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            // Obtener el tipo de usuario de la fila
            String userType = (String) table.getModel().getValueAt(row, 4);
            int userId = (Integer) table.getModel().getValueAt(row, 0);
            
            if (userId == selectedUserId) {
                // Usuario seleccionado
                component.setBackground(SELECTED_ROW);
                component.setForeground(Color.BLACK);
            } else if (isSelected) {
                // Fila resaltada por hover
                component.setBackground(table.getSelectionBackground());
                component.setForeground(table.getSelectionForeground());
            } else {
                // Color según tipo de usuario
                if ("Profesor".equalsIgnoreCase(userType)) {
                    component.setBackground(TEACHER_COLOR);
                    component.setForeground(Color.WHITE);
                } else if ("Alumno".equalsIgnoreCase(userType)) {
                    component.setBackground(STUDENT_COLOR);
                    component.setForeground(Color.WHITE);
                } else {
                    // Filas alternas para mejor legibilidad
                    if (row % 2 == 0) {
                        component.setBackground(Color.WHITE);
                    } else {
                        component.setBackground(ALTERNATE_ROW);
                    }
                    component.setForeground(Color.BLACK);
                }
            }
            
            // Centrar el ID
            if (column == 0) {
                setHorizontalAlignment(JLabel.CENTER);
            } else {
                setHorizontalAlignment(JLabel.LEFT);
            }
            
            return component;
        }
    }
    
    /**
     * Interface para listeners de selección de usuario
     */
    public interface UserSelectionListener {
        /**
         * Se llama cuando se selecciona un usuario
         * @param userId ID del usuario seleccionado
         * @param userName Nombre completo del usuario
         * @param userType Tipo de usuario ("Alumno" o "Profesor")
         */
        void onUserSelected(int userId, String userName, String userType);
    }
}