package fr.vannsuplabs.formationkotlin.data.networking.api

import fr.vannsuplabs.formationkotlin.data.model.PaginatedResult
import fr.vannsuplabs.formationkotlin.data.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {
    @GET(GET_ALL_USERS_PATH)
    suspend fun getAllUsers(
        @Header("Authorization") accessToken: String,
        @Query("since") since: Int,
        @Query("per_page") perPage: Int
    ): Response<List<User>>

    @GET(GET_USER_DETAILS_PATH)
    suspend fun getUserDetails(
        @Header("Authorization") accessToken: String,
        @Path("id") id: String
    ): Response<User>

    @GET(GET_USERS_SEARCH_PATH)
    suspend fun searchUsers(
        @Header("Authorization") accessToken: String,
        @Query("q", encoded = true) query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Response<PaginatedResult<User>>

    @GET(GET_USERS_SEARCH_PATH)
    suspend fun getItemsCount(
        @Header("Authorization") accessToken: String,
        @Query("q", encoded = true) query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Response<PaginatedResult<User>>

    companion object {
        const val GET_ALL_USERS_PATH = "users"
        const val GET_USER_DETAILS_PATH = "users/{id}"
        const val GET_USERS_SEARCH_PATH = "search/users"
    }
}