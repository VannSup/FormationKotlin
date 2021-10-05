package fr.vannsuplabs.formationkotlin.data.networking.datasource

import android.util.Log
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import fr.vannsuplabs.formationkotlin.data.model.User
import fr.vannsuplabs.formationkotlin.data.networking.api.UserApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * DataSource use for the paginated api ([UserApi.getAllUsers])
 */
class UserDataSource private constructor(
    private val api: UserApi,
    private val scope: CoroutineScope,
    private val accessToken: String
) : PageKeyedDataSource<Int, User>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, User>
    ) {
        scope.launch(Dispatchers.IO) {
            try {
                val response = api.getAllUsers(
                    since = FIRST_KEY,
                    accessToken = accessToken,
                    perPage = USER_PER_PAGE
                ).run {
                    if (this.isSuccessful) this.body()
                        ?: throw IllegalStateException("Body is null")
                    else throw IllegalStateException("Response is not successful : code = ${this.code()}")
                }
                if (params.placeholdersEnabled) callback.onResult(
                    response,
                    0,
                    MAX_PAGE_COUNT,
                    null,
                    response.last().id
                ) else callback.onResult(
                    response,
                    null,
                    response.last().id
                )

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {
        scope.launch(Dispatchers.IO) {
            try {
                val response = api.getAllUsers(
                    since = params.key,
                    accessToken = accessToken,
                    perPage = USER_PER_PAGE
                ).run {
                    if (this.isSuccessful) this.body()
                        ?: throw IllegalStateException("Body is null")
                    else throw IllegalStateException("Response is not successful : code = ${this.code()}")
                }
                callback.onResult(
                    response,
                    response.last().id
                )
            } catch (e: Exception) {
                Log.e(TAG, "loadInitial: ", e)
            }
        }
    }

    // This method will not be used in this app
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, User>) = Unit

    class Factory(
        private val api: UserApi,
        private val scope: CoroutineScope,
        private val accessToken: String
    ) : DataSource.Factory<Int, User>() {
        override fun create(): DataSource<Int, User> = UserDataSource(
            api,
            scope,
            accessToken
        )
    }

    companion object {
        private const val TAG: String = "UserDataSource"
        private const val FIRST_KEY = 0
        private const val USER_PER_PAGE = 20
        private const val MAX_PAGE_COUNT = 10
    }
}