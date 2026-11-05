package com.seminario2026.eventpulse.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Query("SELECT * FROM events")
    fun getEvents(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events")
    suspend fun getEventsSync(): List<EventEntity>

    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getEventById(id: String): EventEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity)

    @Query("UPDATE events SET isFavorite = :isFavorite WHERE id = :eventId")
    suspend fun updateFavorite(eventId: String, isFavorite: Boolean)

    @Query("UPDATE events SET hasPass = :hasPass WHERE id = :eventId")
    suspend fun updateHasPass(eventId: String, hasPass: Boolean)

    @Query("SELECT COUNT(*) FROM events")
    suspend fun getEventCount(): Int
}
