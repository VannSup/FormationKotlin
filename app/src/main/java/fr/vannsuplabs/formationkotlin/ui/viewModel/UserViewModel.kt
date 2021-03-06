package fr.vannsuplabs.formationkotlin.ui.viewModel

import androidx.lifecycle.*
import androidx.paging.PagedList
import fr.vannsuplabs.formationkotlin.data.model.User
import fr.vannsuplabs.formationkotlin.data.repository.user.UserRepository
import fr.vannsuplabs.formationkotlin.utils.OnSuccess
import kotlinx.coroutines.launch

class UserViewModel private constructor(
    private val repository: UserRepository
): ViewModel(){

    private val itemCount = MutableLiveData<Int>()

    /**
     *  Return the paginated list of User from the git API
     */
    val usersPagedList = repository.getPaginatedList(viewModelScope)

    fun getUserByLogin(login: String, onSuccess: OnSuccess<User>) {
        viewModelScope.launch {
            repository.getUserDetails(login)?.run(onSuccess)
        }
    }

    fun getUserSearch(searchQuery: String): LiveData<PagedList<User>> {
        viewModelScope.launch {
            itemCount.postValue(repository.getItemCount(searchQuery))
        }
        return repository.getSearchPaginatedList(viewModelScope, searchQuery)
    }

    companion object Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return UserViewModel(UserRepository.instance) as T
        }
    }
}