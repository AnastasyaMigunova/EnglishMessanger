package com.example.application.data.model

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface Service {
    @POST("register")
    suspend fun registerUser(@Body user: User): Response<String>

    @POST("auth")
    suspend fun authUser(@Body user: User): Response<String>

    @GET("interests/getAll")
    suspend fun getInterests(): List<Interest>

    @POST("interests/save/{email}")
    suspend fun saveUserInterests(
        @Path("email") email: String,
        @Body answerList: List<Long>
    ): String

    @GET("api/v1/testing/fetchAllQuestions")
    suspend fun getQuestions(): List<Question>

    @POST("/api/v1/testing/getCurrentLevel/{email}")
    suspend fun getCurrentLevel(
        @Path("email") email: String,
        @Body answerList: List<Answer>
    ): Response<String>

    @POST("/api/v1/user/set_onboarding")
    suspend fun setUserInfo(@Body onBoarding: OnBoardingInfo): String

    @GET("/api/v1/cards/get_all_learned/{email}")
    suspend fun getTotalLearned(@Path("email") email: String): List<Card>

    @GET("/api/v1/cards/get_all_to_learn/{email}")
    suspend fun getTotalToLearn(@Path("email") email: String): List<Card>

    @GET("/api/v1/cards/get_all_card_sets/{email}")
    suspend fun getAllCardSets(@Path("email") email: String): List<CardSet>

    @POST("/api/v1/cards/createCardSet")
    suspend fun createCardSet(@Body cardSet: CardSet): String

    @POST("/api/v1/cards/createCard")
    suspend fun createCard(@Body card: Card): String

    @GET("/get_sentence_exercise")
    suspend fun getSentencesExercises(@Query("topic") topic: String): List<SentenceExercise>

    @GET("/get_question")
    suspend fun getQuestion(@Query("level") level: String): String

    @GET("/send_answer")
    suspend fun getAnswer(
        @Query("question") question: String,
        @Query("answer") answer: String
    ): QuestionExercise

    @GET("/get_translation_exercise")
    suspend fun getTranslationExercise(
        @Query("topic") topic: String,
        @Query("level") level: String
    ): String

    @GET("/send_translation_exercise")
    suspend fun sendTranslationExercise(
        @Query("original_text") original_text: String,
        @Query("text") text: String,
        @Query("level") level: String
    ): TranslationModel

    @GET("/api/v1/theory/get_theory")
    suspend fun getTheory(): GrammarTheory

    @GET("/dictionary/findEngWord")
    suspend fun findEngWord(@Query("searchWord") searchWord: String): Word

    @GET("/dictionary/findRusWord")
    suspend fun findRusWord(@Query("searchWord") searchWord: String): List<Word>

    @GET("/api/v1/user/get_user_by_username/{username}")
    suspend fun getUserByUsername(@Path("username") username: String): User

    @GET("/api/v1/user/get_friends/{email}")
    suspend fun getFriends(@Path("email") email: String): List<User>

    @GET("/api/v1/friends/addingFriend/{email}")
    suspend fun addingFriend(
        @Path("email") email: String,
        @Query("requestedEmail") requestedEmail: String
    ): String

    @GET("/api/v1/friends/get_friends_requests/{email}")
    suspend fun getFriendsRequests(@Path("email") email: String): List<User>

    @GET("/api/v1/cards/get_card_set/{email}/{id}")
    suspend fun getCardSet(@Path("email") email: String, @Path("id") id: Long): CardSet

    @GET("/api/v1/user/get_user_by_email/{email}")
    suspend fun getUserByEmail(@Path("email") email: String): User

    @POST("/api/v1/cards/save_to_learned")
    suspend fun saveToLearned(@Body card: Card): String

    @POST("/api/v1/cards/refresh_set/{id}/{email}")
    suspend fun refreshCards(@Path("id") id: Long, @Path("email") email: String): String

    @POST("/api/v1/friends/addingFriend/accepted/{email}")
    suspend fun acceptedFriending(
        @Path("email") email: String,
        @Query("sentEmail") sentEmail: String
    ): String

    @POST("/api/v1/friends/addingFriend/rejected/{email}")
    suspend fun rejectedFriending(
        @Path("email") email: String,
        @Query("sentEmail") sentEmail: String
    ): String

    @GET("/api/v1/users/fetch_all_chat_users/{email}")
    suspend fun fetchAllChatUsers(@Path("email") email: String) : List<User>

    @POST("/dialog/generateDialog/{email}")
    suspend fun generateDialog(@Path("email") email: String): User

    @GET("/dialog/generateDialogTopic/{email}")
    suspend fun generateDialogTopic(@Path("email") email: String): Interest

    @POST("/api/v1/users/create_chatroom")
    suspend fun createChatRoom(@Body chatRoom: ChatRoom): Response<String>

    @POST("/api/v1/users/save_message")
    suspend fun saveMessage(@Body chatMessage: ChatMessage): Response<String>

    @GET("/api/v1/users/get_chat_messages/{chatId}")
    suspend fun getChatMessages(@Path("chatId") chatId: String): List<ChatMessage>

    @GET("/api/v1/users/get_last_message/{chatId}")
    suspend fun getLastMessage(@Path("chatId") chatId: String): Response<String>

    @POST("/api/v1/checkGrammar")
    suspend fun checkGrammar(@Body text: String): List<Match>
}