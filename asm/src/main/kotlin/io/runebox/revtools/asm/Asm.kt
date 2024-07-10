package io.runebox.revtools.asm

import sootup.core.inputlocation.AnalysisInputLocation
import sootup.core.jimple.common.stmt.JInvokeStmt
import sootup.core.jimple.javabytecode.stmt.JSwitchStmt
import sootup.core.model.SourceType
import sootup.java.bytecode.inputlocation.JavaClassPathAnalysisInputLocation
import sootup.java.bytecode.inputlocation.PathBasedAnalysisInputLocation
import sootup.java.core.views.JavaView
import java.io.File
import java.nio.file.Paths

fun main() {

    val inputLocation: AnalysisInputLocation = PathBasedAnalysisInputLocation.create(Paths.get("gamepack.deob.jar"), SourceType.Library)
    val view = JavaView(inputLocation)

    val clientClassType = view.identifierFactory.getClassType("Client")
    val clientClass = view.getClass(clientClassType).get()

    val initMethodSig = view.identifierFactory.getMethodSignature(clientClassType, "init", "void", emptyList())
    view.getMethod(initMethodSig)

    val initMethod = clientClass.getMethod(initMethodSig.subSignature).get()
    println(initMethod.body)

    for(stmt in initMethod.body.stmts.stream()) {
        when(stmt) {
            is JInvokeStmt -> {
                val expr = stmt.invokeExpr
                println("Invoke: ${expr.methodSignature} : Args(${expr.args.joinToString { it.type.toString() }}), Return: ${expr.type}")
                println("Stmt: ${stmt}")
            }
        }
    }
}

