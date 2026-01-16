package utils;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Componente reutilizable para mostrar horarios semanales (Lunes-Viernes)
 * Permite cambiar colores de celdas para diferentes estados (reuniones, cancelaciones, etc.)
 */
public class WeekScheduleTable extends JPanel {
    
    private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel tableModel;
    private Map<String, Color> cellColors;
    
    // Colores predefinidos para diferentes estados
    public static final Color NORMAL_CLASS = new Color(70, 130, 180);         // Azul - Clase normal
    public static final Color MEETING_SCHEDULED = new Color(34, 139, 34);     // Verde - Reunión programada
    public static final Color MEETING_CANCELLED = new Color(220, 53, 69);     // Rojo - Reunión cancelada
    public static final Color FREE_TIME = new Color(240, 240, 240);           // Gris claro - Tiempo libre
    public static final Color SELECTED_CELL = new Color(255, 215, 0);         // Dorado - Celda seleccionada
    
    private String[] timeSlots = {
        "08:00-09:00", "09:00-10:00", "10:00-11:00", "11:00-11:30", // Recreo
        "11:30-12:30", "12:30-13:30", "13:30-14:30", "14:30-15:30"
    };
    
    private String[] days = {"Hora", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};
    
    public WeekScheduleTable() {
        initializeComponents();
        setupTable();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        cellColors = new HashMap<>();
        
        // Crear modelo de tabla
        tableModel = new DefaultTableModel(days, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0; // Solo la primera columna (hora) no es editable
            }
        };
        
        // Añadir filas de horarios
        for (String timeSlot : timeSlots) {
            Object[] row = new Object[6];
            row[0] = timeSlot;
            for (int i = 1; i < 6; i++) {
                row[i] = ""; // Inicialmente vacío
            }
            tableModel.addRow(row);
        }
        
        table = new JTable(tableModel);
    }
    
    private void setupTable() {
        // Configuración básica de la tabla
        table.setRowHeight(40);
        table.setFont(new Font("Tahoma", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(51, 102, 153));
        table.getTableHeader().setForeground(Color.WHITE);
        
        // Configurar ancho de columnas
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100); // Columna hora más estrecha
        for (int i = 1; i < 6; i++) {
            columnModel.getColumn(i).setPreferredWidth(150);
        }
        
        // Renderer personalizado para colores de celdas
        table.setDefaultRenderer(Object.class, new CustomCellRenderer());
        
        // Añadir la tabla con scroll
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(800, 350));
        add(scrollPane, BorderLayout.CENTER);
        
        // Título de la tabla
        JLabel titleLabel = new JLabel("Horario Semanal", JLabel.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        titleLabel.setForeground(new Color(51, 102, 153));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Panel de leyenda
        add(createLegendPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createLegendPanel() {
        JPanel legendPanel = new JPanel(new FlowLayout());
        legendPanel.setBorder(BorderFactory.createTitledBorder("Leyenda"));
        
        legendPanel.add(createLegendItem("Clase Normal", NORMAL_CLASS));
        legendPanel.add(createLegendItem("Reunión Programada", MEETING_SCHEDULED));
        legendPanel.add(createLegendItem("Reunión Cancelada", MEETING_CANCELLED));
        legendPanel.add(createLegendItem("Tiempo Libre", FREE_TIME));
        
        return legendPanel;
    }
    
    private JPanel createLegendItem(String text, Color color) {
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
     * Establece el contenido de una celda específica
     * @param day Día de la semana (1=Lunes, 2=Martes, etc.)
     * @param timeSlotIndex Índice del slot de tiempo (0-7)
     * @param content Contenido a mostrar
     */
    public void setCellContent(int day, int timeSlotIndex, String content) {
        if (day >= 1 && day <= 5 && timeSlotIndex >= 0 && timeSlotIndex < timeSlots.length) {
            tableModel.setValueAt(content, timeSlotIndex, day);
        }
    }
    
    /**
     * Establece el color de una celda específica
     * @param day Día de la semana (1=Lunes, 2=Martes, etc.)
     * @param timeSlotIndex Índice del slot de tiempo (0-7)
     * @param color Color a aplicar
     */
    public void setCellColor(int day, int timeSlotIndex, Color color) {
        if (day >= 1 && day <= 5 && timeSlotIndex >= 0 && timeSlotIndex < timeSlots.length) {
            String key = timeSlotIndex + "," + day;
            cellColors.put(key, color);
            table.repaint();
        }
    }
    
    /**
     * Limpia todo el horario
     */
    public void clearSchedule() {
        for (int i = 0; i < timeSlots.length; i++) {
            for (int j = 1; j <= 5; j++) {
                tableModel.setValueAt("", i, j);
            }
        }
        cellColors.clear();
        table.repaint();
    }
    
    /**
     * Carga un horario completo desde una matriz
     * @param scheduleData Matriz [timeSlot][day] con el contenido
     */
    public void loadScheduleData(String[][] scheduleData) {
        for (int i = 0; i < Math.min(scheduleData.length, timeSlots.length); i++) {
            for (int j = 0; j < Math.min(scheduleData[i].length, 5); j++) {
                setCellContent(j + 1, i, scheduleData[i][j]);
            }
        }
    }
    
    /**
     * Obtiene el contenido de una celda específica
     */
    public String getCellContent(int day, int timeSlotIndex) {
        if (day >= 1 && day <= 5 && timeSlotIndex >= 0 && timeSlotIndex < timeSlots.length) {
            return (String) tableModel.getValueAt(timeSlotIndex, day);
        }
        return "";
    }
    
    /**
     * Establece el título de la tabla
     */
    public void setScheduleTitle(String title) {
        Component[] components = getComponents();
        for (Component comp : components) {
            if (comp instanceof JLabel) {
                ((JLabel) comp).setText(title);
                break;
            }
        }
    }
    
    // Renderer personalizado para aplicar colores a las celdas
    private class CustomCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            // Color por defecto
            if (column == 0) {
                // Columna de horas con color de fondo diferente
                c.setBackground(new Color(220, 220, 220));
                c.setForeground(Color.BLACK);
                setHorizontalAlignment(CENTER);
            } else {
                // Buscar color personalizado para esta celda
                String key = row + "," + column;
                Color cellColor = cellColors.get(key);
                
                if (cellColor != null) {
                    c.setBackground(cellColor);
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
                setHorizontalAlignment(CENTER);
            }
            
            // Destacar celda seleccionada
            if (isSelected && column != 0) {
                c.setBackground(SELECTED_CELL);
                c.setForeground(Color.BLACK);
            }
            
            setBorder(BorderFactory.createLineBorder(Color.GRAY));
            return c;
        }
    }
}