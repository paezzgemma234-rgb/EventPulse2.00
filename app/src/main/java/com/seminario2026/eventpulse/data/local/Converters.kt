package com.seminario2026.eventpulse.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seminario2026.eventpulse.model.Artist
import com.seminario2026.eventpulse.model.Category
import com.seminario2026.eventpulse.model.StageSchedule

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromCategory(category: Category): String {
        return gson.toJson(category)
    }

    @TypeConverter
    fun toCategory(data: String): Category {
        return gson.fromJson(data, Category::class.java)
    }

    @TypeConverter
    fun fromArtistList(artists: List<Artist>): String {
        return gson.toJson(artists)
    }

    @TypeConverter
    fun toArtistList(data: String): List<Artist> {
        val listType = object : TypeToken<List<Artist>>() {}.type
        return gson.fromJson(data, listType) ?: emptyList()
    }

    @TypeConverter
    fun fromStageScheduleList(stages: List<StageSchedule>): String {
        return gson.toJson(stages)
    }

    @TypeConverter
    fun toStageScheduleList(data: String): List<StageSchedule> {
        val listType = object : TypeToken<List<StageSchedule>>() {}.type
        return gson.fromJson(data, listType) ?: emptyList()
    }
}
