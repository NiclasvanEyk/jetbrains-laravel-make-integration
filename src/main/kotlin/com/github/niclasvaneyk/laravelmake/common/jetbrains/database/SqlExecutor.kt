package com.github.niclasvaneyk.laravelmake.common.jetbrains.database

import com.intellij.database.dataSource.DatabaseConnection
import com.intellij.database.dataSource.connection.statements.OutParameter
import com.intellij.database.dataSource.connection.statements.ResultsProducer
import com.intellij.database.dataSource.connection.statements.SmartStatements
import com.intellij.database.dataSource.connection.tryGet
import com.intellij.database.remote.jdbc.RemoteResultSet

internal class SqlExecutor(private val connection: DatabaseConnection) {
    fun <T> run(sql: String, buildRow: (RemoteResultSet) -> T): List<T> {
        val results = mutableListOf<T>()

        SmartStatements
            .poweredBy(connection)
            .simple()
            .execute(sql)
            .tryGet()
            .processRemaining(object: ResultsProducer.Processor<Unit> {
                override fun parameterResults(
                    parameters: List<OutParameter<*>>,
                    retriever: (OutParameter<*>) -> Any?,
                    index: Int,
                ) {}

                override fun results(resultSet: RemoteResultSet, index: Int) {
                    while (resultSet.next()) {
                        results.add(buildRow(resultSet))
                    }
                }

                override fun updateCount(count: Int, index: Int) {
                }
            })
        return results
    }
}
