package com.hi.dhl.pokemon.ui.detail

import androidx.databinding.ObservableBoolean
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.hi.dhl.pokemon.data.remote.doFailure
import com.hi.dhl.pokemon.data.remote.doSuccess
import com.hi.dhl.pokemon.data.repository.Repository
import com.hi.dhl.pokemon.model.PokemonInfoModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * <pre>
 *     author: dhl
 *     date  : 2020/7/11
 *     desc  :
 * </pre>
 */

@FlowPreview
@ExperimentalCoroutinesApi
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val pokemonRepository: Repository
) : ViewModel() {
    val mLoading = ObservableBoolean()

    // 私有的 MutableLiveData 可变的，对内访问
    private val _pokemon = MutableLiveData<PokemonInfoModel>()

    // 对外暴露不可变的 LiveData，只能查询
    val pokemon: LiveData<PokemonInfoModel> = _pokemon

    private val _failure = MutableLiveData<String>()
    val failure = _failure

    /**
     * 方法二
     */
    fun fectchPokemonInfo2(name: String) = liveData<PokemonInfoModel> {
        pokemonRepository.fetchPokemonInfo(name)
            .onStart {
                // 在调用 flow 请求数据之前，做一些准备工作，例如显示正在加载数据的按钮
                mLoading.set(true)
            }
            .catch {
                // 捕获上游出现的异常ss
                mLoading.set(false)
            }
            .onCompletion {
                // 请求完成
                mLoading.set(false)
            }
            .collectLatest { result ->
                result.doFailure { throwable ->
                    _failure.value = throwable?.message ?: "failure"
                }
                result.doSuccess { value ->
                    _pokemon.postValue(value)
                    emit(value)
                }
            }
    }

    /**
     * 方法三
     */
    suspend fun fetchPokemonInfo3(name: String) =
        pokemonRepository.fetchPokemonInfo(name)
            .onStart {
                // 在调用 flow 请求数据之前，做一些准备工作，例如显示正在加载数据的按钮
                mLoading.set(true)
            }
            .catch {
                // 捕获上游出现的异常
                mLoading.set(false)
            }
            .onCompletion {
                // 请求完成
                mLoading.set(false)
            }.asLiveData()

    /**
     * 方法一
     */
    fun fetchPokemonInfo1(name: String) = viewModelScope.launch {
        pokemonRepository.fetchPokemonInfo(name)
            .onStart {
                // 在调用 flow 请求数据之前，做一些准备工作，例如显示正在加载数据的按钮
                mLoading.set(true)
            }
            .catch {
                // 捕获上游出现的异常
                mLoading.set(false)
            }
            .onCompletion {
                // 请求完成
                mLoading.set(false)
            }
            .collectLatest { result ->
                result.doFailure { throwable ->
                    _failure.value = throwable?.message ?: "failure"
                }

                result.doSuccess { value ->
                    _pokemon.postValue(value)
                }
            }
    }


    companion object {
        private val TAG = "DetailViewModel"
    }

}