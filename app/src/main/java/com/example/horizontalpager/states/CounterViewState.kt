package com.example.horizontalpager.states

sealed class CounterViewState {
    data class CounterValue(val count : Int) : CounterViewState()
}

sealed class CounterViewAction {
    object Increment : CounterViewAction()
    object Decrement : CounterViewAction()
    object Reset : CounterViewAction()
}