package com.sng.patternexample.ui

import androidx.lifecycle.*
import com.sng.patternexample.model.Blog
import com.sng.patternexample.repository.BlogRepository
import com.sng.patternexample.utils.DataState
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class BlogViewModel constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val blogRepository: BlogRepository
) : ViewModel() {

    private val _dataState: MutableLiveData<DataState<List<Blog>>> = MutableLiveData()

    val dataState: LiveData<DataState<List<Blog>>>
        get() = _dataState

    fun setStateEvent(stateEvent: MainStateEvent) {
        viewModelScope.launch {
            when (stateEvent) {
                is MainStateEvent.GetBlogEvent -> {
                    blogRepository.getBlogs()
                        .onEach { dataState ->
                            _dataState.value = dataState
                        }
                        .launchIn(viewModelScope)
                }
                is MainStateEvent.None -> {
                    //TODO
                }
            }
        }
    }

    sealed class MainStateEvent() {
        object GetBlogEvent : MainStateEvent()
        object None : MainStateEvent()
    }

}