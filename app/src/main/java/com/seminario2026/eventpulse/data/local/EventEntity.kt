package com.seminario2026.eventpulse.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.seminario2026.eventpulse.model.Artist
import com.seminario2026.eventpulse.model.Category
import com.seminario2026.eventpulse.model.DigitalEvent
import com.seminario2026.eventpulse.model.StageSchedule

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey val id: String,
    val title: String,
    val subtitle: String,
    val category: Category,
    val date: String,
    val location: String,
    val imageUrl: String,
    val isLive: Boolean,
    val isFeatured: Boolean,
    val description: String,
    val price: String,
    val artists: List<Artist>,
    val stages: List<StageSchedule>,
    val isFavorite: Boolean,
    val hasPass: Boolean
)

fun EventEntity.toDomainModel(): DigitalEvent {
    return DigitalEvent(
        id = id,
        title = title,
        subtitle = subtitle,
        category = category,
        date = date,
        location = location,
        imageUrl = imageUrl,
        isLive = isLive,
        isFeatured = isFeatured,
        description = description,
        price = price,
        artists = artists,
        stages = stages,
        isFavorite = isFavorite,
        hasPass = hasPass
    )
}

fun DigitalEvent.toEntity(): EventEntity {
    return EventEntity(
        id = id,
        title = title,
        subtitle = subtitle,
        category = category,
        date = date,
        location = location,
        imageUrl = imageUrl,
        isLive = isLive,
        isFeatured = isFeatured,
        description = description,
        price = price,
        artists = artists,
        stages = stages,
        isFavorite = isFavorite,
        hasPass = hasPass
    )
}
