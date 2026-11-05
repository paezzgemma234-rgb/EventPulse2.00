package com.seminario2026.eventpulse.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.seminario2026.eventpulse.model.DigitalTicket

@Entity(tableName = "tickets")
data class TicketEntity(
    @PrimaryKey val ticketId: String,
    val eventId: String,
    val eventTitle: String,
    val date: String,
    val location: String,
    val accessZone: String,
    val qrCodeSeed: String,
    val holderName: String
)

fun TicketEntity.toDomainModel(): DigitalTicket {
    return DigitalTicket(
        ticketId = ticketId,
        eventId = eventId,
        eventTitle = eventTitle,
        date = date,
        location = location,
        accessZone = accessZone,
        qrCodeSeed = qrCodeSeed,
        holderName = holderName
    )
}

fun DigitalTicket.toEntity(): TicketEntity {
    return TicketEntity(
        ticketId = ticketId,
        eventId = eventId,
        eventTitle = eventTitle,
        date = date,
        location = location,
        accessZone = accessZone,
        qrCodeSeed = qrCodeSeed,
        holderName = holderName
    )
}
