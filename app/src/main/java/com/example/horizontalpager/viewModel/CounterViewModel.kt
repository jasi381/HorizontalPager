package com.example.horizontalpager.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.horizontalpager.states.CounterViewAction
import com.example.horizontalpager.states.CounterViewState
import kotlinx.coroutines.launch

class CounterViewModel: ViewModel() {

    private val _state = MutableLiveData<CounterViewState>()
    val state : LiveData<CounterViewState> = _state

    init {
        _state.value = CounterViewState.CounterValue(0)
    }

    fun processIntent(intent:CounterViewAction){
        viewModelScope.launch {

                when(intent){
                    is CounterViewAction.Increment -> {
                        incrementCounter()
                    }
                    is CounterViewAction.Decrement -> {
                       decrementCounter()
                    }
                    is CounterViewAction.Reset -> {
                        resetCounter()
                    }
                }

        }
    }

    private fun resetCounter() {
        _state.value = CounterViewState.CounterValue(0)
    }

    private fun decrementCounter() {
        val currentCount = (_state.value as CounterViewState.CounterValue).count
        _state.value = CounterViewState.CounterValue(currentCount - 1)
    }

    private fun incrementCounter() {
        val currentCount = (_state.value as CounterViewState.CounterValue).count
        _state.value = CounterViewState.CounterValue(currentCount + 1)
    }
    data class  Result(val  isLoading :Boolean = false,val  data : Any? = null,val  error : String? = null)

    }
