package gestion_eventos.com.data.remote.api

import gestion_eventos.com.data.remote.dto.AuthRequestDto
import gestion_eventos.com.data.remote.dto.AuthResponseDto
import gestion_eventos.com.data.remote.dto.CommentDto
import gestion_eventos.com.data.remote.dto.CommentRequestDto
import gestion_eventos.com.data.remote.dto.EventDto
import gestion_eventos.com.data.remote.dto.EventRequestDto
import gestion_eventos.com.data.remote.dto.RSVPDto
import gestion_eventos.com.data.remote.dto.RSVPRequestDto
import gestion_eventos.com.data.remote.dto.RatingDto
import gestion_eventos.com.data.remote.dto.RatingRequestDto
import gestion_eventos.com.data.remote.dto.SocialLoginRequestDto
import gestion_eventos.com.data.remote.dto.StatsDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    // Define los endpoints REST que consume la app.
    @POST("auth/register")
    suspend fun register(@Body request: AuthRequestDto): Response<AuthResponseDto>

    @POST("auth/login")
    suspend fun login(@Body request: AuthRequestDto): Response<AuthResponseDto>

    @POST("auth/social-login")
    suspend fun socialLogin(@Body request: SocialLoginRequestDto): Response<AuthResponseDto>

    @GET("events")
    suspend fun getEvents(): Response<List<EventDto>>

    @POST("events")
    suspend fun createEvent(@Body request: EventRequestDto): Response<EventDto>

    @GET("events/{id}")
    suspend fun getEventById(@Path("id") eventId: String): Response<EventDto>

    @PUT("events/{id}")
    suspend fun updateEvent(
        @Path("id") eventId: String,
        @Body request: EventRequestDto
    ): Response<EventDto>

    @DELETE("events/{id}")
    suspend fun deleteEvent(@Path("id") eventId: String): Response<Unit>

    @POST("events/{id}/rsvp")
    suspend fun createRsvp(
        @Path("id") eventId: String,
        @Body request: RSVPRequestDto
    ): Response<RSVPDto>

    @GET("events/{id}/comments")
    suspend fun getComments(@Path("id") eventId: String): Response<List<CommentDto>>

    @POST("events/{id}/comments")
    suspend fun addComment(
        @Path("id") eventId: String,
        @Body request: CommentRequestDto
    ): Response<CommentDto>

    @GET("events/{id}/ratings")
    suspend fun getRatings(@Path("id") eventId: String): Response<List<RatingDto>>

    @POST("events/{id}/ratings")
    suspend fun addRating(
        @Path("id") eventId: String,
        @Body request: RatingRequestDto
    ): Response<RatingDto>

    @GET("users/{id}/history")
    suspend fun getHistory(@Path("id") userId: String): Response<List<EventDto>>

    @GET("users/{id}/stats")
    suspend fun getStats(@Path("id") userId: String): Response<StatsDto>

    @GET("users/{id}/rsvps")
    suspend fun getUserRsvps(@Path("id") userId: String): Response<List<RSVPDto>>
}
