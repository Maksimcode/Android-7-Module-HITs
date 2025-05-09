package com.example.android_7_module_hits.interpreter

class ExecutionContext {
    private val variables = mutableMapOf<String, Int?>()

    fun addVariable(name: String){
        variables[name] = null
    }

    fun setVariable(name: String, value: Int) {
        if (hasVariable(name)) {variables[name] = value}
    }

    fun getVariable(name: String): Int {
        return variables[name] ?: throw IllegalArgumentException("Variable $name not declared")
    }

    fun hasVariable(name: String): Boolean {
        return variables.containsKey(name)
    }
}