package fr.vannsuplabs.formationkotlin.data.repository.user

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import fr.vannsuplabs.formationkotlin.data.model.User
import fr.vannsuplabs.formationkotlin.data.networking.HttpClientManager
import fr.vannsuplabs.formationkotlin.data.networking.api.UserApi
import fr.vannsuplabs.formationkotlin.data.networking.createApi
import fr.vannsuplabs.formationkotlin.data.networking.datasource.SearchUserDataSource
import fr.vannsuplabs.formationkotlin.data.networking.datasource.UserDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private class UserRepositoryImpl(
    private val api: UserApi
) : UserRepository {

    private val paginationConfig = PagedList.Config
        .Builder()
        .setEnablePlaceholders(false)
        .setPageSize(20)
        .build()

    override fun getPaginatedList(
        scope: CoroutineScope
    ): LiveData<PagedList<User>> {
        return LivePagedListBuilder(
            UserDataSource.Factory(api, scope),
            paginationConfig
        ).build()
    }

    override suspend fun getUserDetails(id: String): User? {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getUserDetails(id)
                check(response.isSuccessful) { "Response is not a success : code = ${response.code()}" }
                response.body() ?: throw IllegalStateException("Body is null")
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    override fun getSearchPaginatedList(
        scope: CoroutineScope,
        searchQuery: String,
    ): LiveData<PagedList<User>> {
        return LivePagedListBuilder(
            SearchUserDataSource.Factory(api, scope, searchQuery),
            paginationConfig
        ).build()
    }

    override suspend fun getItemCount(query: String): Int {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getItemsCount(query, 1, 20)
                check(response.isSuccessful) { "Response is not a success : code = ${response.code()}" }
                response.body()?.total_count ?: throw IllegalStateException("Body is null")

            } catch (e: Exception) {
                e.printStackTrace()
                0
            }
        }
    }
}

interface UserRepository {

    fun getPaginatedList(scope: CoroutineScope): LiveData<PagedList<User>>

    suspend fun getUserDetails(id: String): User?

    fun getSearchPaginatedList(scope: CoroutineScope, searchQuery: String): LiveData<PagedList<User>>

    suspend fun getItemCount( query: String): Int

    companion object {
        /**
         * Singleton for the interface [UserRepository]
         */
        val instance: UserRepository by lazy {
            // Lazy means "When I need it" so here this block will be launch
            // the first time you need the instance,
            // then, the reference will be stored in the value `instance`
            UserRepositoryImpl(HttpClientManager.instance.createApi())
        }
    }
}