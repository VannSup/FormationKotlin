package fr.vannsuplabs.formationkotlin.data.networking.datasource

import android.util.Log
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import fr.vannsuplabs.formationkotlin.data.model.User
import fr.vannsuplabs.formationkotlin.data.networking.api.UserApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchUserDataSource private constructor(
    private val api: UserApi,
    private val scope: CoroutineScope,
    private val searchQuery: String,
    private val accessToken: String
) : PageKeyedDataSource<Int, User>() {

    private var pageCount = FIRST_KEY

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, User>
    ) {
        scope.launch(Dispatchers.IO) {
            try {
                val response = api.searchUsers(
                    page = FIRST_KEY,
                    accessToken = accessToken,
                    perPage = USER_PER_PAGE,
                    query = searchQuery
                ).run {
                    if (this.isSuccessful) this.body()
                        ?: throw IllegalStateException("Body is null")
                    else throw IllegalStateException("Response is not successful : code = ${this.code()}")
                }
                pageCount += 1
                if (params.placeholdersEnabled) callback.onResult(
                    response.items,
                    0,
                    getMaxPageCount(response.total_count),
                    null,
                    pageCount
                ) else callback.onResult(
                    response.items,
                    null,
                    pageCount
                )

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {
        scope.launch(Dispatchers.IO) {
            try {
                val response = api.searchUsers(
                    page = params.key,
                    accessToken = accessToken,
                    perPage = USER_PER_PAGE,
                    query = searchQuery
                ).run {
                    if (this.isSuccessful) this.body()
                        ?: throw IllegalStateException("Body is null")
                    else throw IllegalStateException("Response is not successful : code = ${this.code()}")
                }
                pageCount += 1
                callback.onResult(
                    response.items,
                    pageCount
                )
            } catch (e: Exception) {
                Log.e(TAG, "loadInitial: ", e)
            }
        }
    }

    private fun getMaxPageCount(totalCount: Int) = (totalCount / USER_PER_PAGE) + 1

    // This method will not be used in this app
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, User>) = Unit

    class Factory(
        private val api: UserApi,
        private val scope: CoroutineScope,
        private val searchQuery: String,
        private val accessToken: String
    ) : DataSource.Factory<Int, User>() {
        override fun create(): DataSource<Int, User> = SearchUserDataSource(api, scope, searchQuery, accessToken)
    }

    companion object {
        private const val TAG: String = "SearchUserDataSource"
        private const val FIRST_KEY = 1
        private const val USER_PER_PAGE = 20
    }

}