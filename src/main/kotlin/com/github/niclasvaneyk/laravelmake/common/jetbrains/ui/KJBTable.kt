package com.github.niclasvaneyk.laravelmake.common.jetbrains.ui

import com.intellij.ui.table.JBTable
import java.util.*
import javax.swing.table.AbstractTableModel
import javax.swing.table.DefaultTableCellRenderer
import javax.swing.table.DefaultTableModel
import javax.swing.table.TableCellRenderer
import javax.swing.table.TableModel

class Column<RowType, CellType>(
    val header: String,
    val valueRetriever: (row: RowType) -> CellType,
    val renderer: TableCellRenderer = DefaultTableCellRenderer(),
)

class KJBTableModel<T>(vararg val columns: Column<T, *>): AbstractTableModel() {
    private var data = mutableListOf<T>()
    val model = DefaultTableModel(cellData(), columnNames)

    fun cellData(): Vector<Vector<*>> {
        return Vector<Vector<*>>(data.size).apply {
            addAll(data.mapIndexed { rowIndex, row ->
                Vector<Any?>(columns.size).apply {
                    addAll(columns.map { it.valueRetriever(row) })
                }
            })
        }
    }

    val columnNames: Vector<String> get() {
        return Vector<String>(columns.size).apply {
            addAll(columns.map { it.header })
        }
    }

    fun data(): MutableList<T> {
        val newModel = mutableListOf<T>()
        newModel.addAll(data)

        return newModel
    }

    fun refresh(newData: List<T>) {
        data.clear()
        data.addAll(newData)
        model.setDataVector(cellData(), columnNames)
        fireTableDataChanged()
    }

    override fun getRowCount() = data.size
    override fun getColumnCount() = columns.size
    override fun getColumnName(column: Int) = columns[column].header
    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any? {
        return columns[columnIndex].valueRetriever(data[rowIndex])
    }
}

abstract class KJBTable<T>(protected val kjbModel: KJBTableModel<T>): JBTable(kjbModel) {
    init {
        tableHeader.reorderingAllowed = false
    }
    override fun getCellRenderer(row: Int, column: Int): TableCellRenderer {
        return super.getCellRenderer(row, column)
    }
}