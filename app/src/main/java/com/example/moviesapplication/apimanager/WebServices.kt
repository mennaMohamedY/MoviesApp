package com.example.moviesapplication.apimanager

import com.example.moviesapplication.module.MoviesCategoriesResponse
import com.example.moviesapplication.module.SingleMovieResponse
import com.example.moviesapplication.module.TMDBResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WebServices {

    @GET("trending/movie/week")
    fun getTrendingMovies(@Query("api_key") apiKeyAuthentication: String ): Call<TMDBResponse>

//, @Query("language") language:String

    @GET("movie/top_rated")
    fun TopRatedMovies(@Query("api_key") apiKeyAuthentication: String):Call<TMDBResponse>

    @GET("movie/{id}")
    suspend fun getMovieDetails(@Path("id") id:Int , @Query("append_to_response") appendvideos:String, @Query("api_key") apiKeyAuthentication: String):SingleMovieResponse

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(@Path("movie_id") movie_id:Int, @Query("api_key") apiKeyAuthentication: String):TMDBResponse

    @GET("genre/movie/list")
    suspend fun getMoviesGenre(@Query("api_key") apiKeyAuthentication: String):MoviesCategoriesResponse

    @GET("discover/movie")
    suspend fun getMoviesByGenreID(@Query("api_key") apiKeyAuthentication: String, @Query("with_genres") genreId:Int):TMDBResponse

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("api_key") apiKeyAuthentication: String,@Query("page") pageNum:Int):TMDBResponse
}