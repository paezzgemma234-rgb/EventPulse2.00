package com.seminario2026.eventpulse.data

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seminario2026.eventpulse.data.local.AppDatabase
import com.seminario2026.eventpulse.data.local.EventDao
import com.seminario2026.eventpulse.data.local.TicketDao
import com.seminario2026.eventpulse.data.local.toDomainModel
import com.seminario2026.eventpulse.data.local.toEntity
import com.seminario2026.eventpulse.model.Artist
import com.seminario2026.eventpulse.model.Category
import com.seminario2026.eventpulse.model.DigitalEvent
import com.seminario2026.eventpulse.model.DigitalTicket
import com.seminario2026.eventpulse.model.StageSchedule
import com.seminario2026.eventpulse.ui.theme.CyberCyan
import com.seminario2026.eventpulse.ui.theme.DarkCharcoal
import com.seminario2026.eventpulse.ui.theme.EventPulseTheme
import com.seminario2026.eventpulse.ui.theme.MidnightVoid
import com.seminario2026.eventpulse.ui.theme.MutedSilver
import com.seminario2026.eventpulse.ui.theme.NeonMagenta
import com.seminario2026.eventpulse.ui.theme.SoftWhite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

interface EventRepository {
    fun getEvents(): Flow<List<DigitalEvent>>
    fun getCategories(): List<Category>
    fun getEventById(id: String): DigitalEvent?
    fun toggleFavorite(eventId: String)
    fun purchasePass(eventId: String): DigitalTicket?
    fun getSavedTickets(): Flow<List<DigitalTicket>>
    fun getInitialEvents(): List<DigitalEvent>
    fun getInitialTickets(): List<DigitalTicket>
}

class EventRepositoryImpl(
    context: Context? = null
) : EventRepository {

    private val database: AppDatabase? = try {
        if (context != null &&
            !context.javaClass.name.contains("LayoutLib", ignoreCase = true) &&
            !context.javaClass.name.contains("Bridge", ignoreCase = true) &&
            context.packageName != null
        ) {
            AppDatabase.getInstance(context)
        } else {
            null
        }
    } catch (_: Throwable) {
        null
    }
    private val eventDao: EventDao? = try { database?.eventDao() } catch (_: Throwable) { null }
    private val ticketDao: TicketDao? = try { database?.ticketDao() } catch (_: Throwable) { null }

    private val sampleEvents = listOf(
        DigitalEvent(
            id = "mutek-2026",
            title = "MUTEK CyberPulse 2026",
            subtitle = "Festival Internacional de Creatividad Digital y Música Electrónica",
            category = Category.ELECTRONIC,
            date = "28 - 30 OCTUBRE 2026",
            location = "Centro de Arte Sonoro & Dome 360°, CABA",
            imageUrl = "https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?auto=format&fit=crop&w=800&q=80",
            isLive = true,
            isFeatured = true,
            description = "MUTEK celebra su edición futurista reuniendo a los mayores pioneros del sonido modular, arte generativo, proyecciones inmersivas y experiencias fulldome 360°. Vive una inmersión completa en la intersección de sonido y código en tiempo real.",
            price = "$18.500 ARS",
            artists = listOf(
                Artist("a1", "Amon Tobin (ISAM Live)", "Sculptural Projection & Glitch Synth", "Pionero mundial del mapeo 3D y diseño de sonido algorítmico."),
                Artist("a2", "Ryoichi Kurokawa", "Laser & Audio-Visual Generative Art", "Artista japonés que transforma datos científicos en arquitecturas de luz y sonido."),
                Artist("a3", "Caterina Barbieri", "Modular Analog Synthesizer", "Compositora italiana explorando patrones psicofísicos y arpegios infinitos.")
            ),
            stages = listOf(
                StageSchedule("s1", "Dome 360°", "20:00 - 21:30", "Amon Tobin (ISAM Live)", "AV Live Glitch", pulseIntensity = 98),
                StageSchedule("s2", "Darkroom Lab", "21:45 - 23:00", "Caterina Barbieri", "Modular Synth Meditation", pulseIntensity = 88),
                StageSchedule("s3", "Cyber Stage Principal", "23:15 - 01:30", "Ryoichi Kurokawa", "Laser & Generative AV", pulseIntensity = 95)
            ),
            isFavorite = true,
            hasPass = true
        ),
        DigitalEvent(
            id = "sonar-xr-2026",
            title = "Sónar Light & Sound XR",
            subtitle = "Instalaciones Inmersivas y Performance Cuántica",
            category = Category.IMMERSIVE,
            date = "12 - 14 NOVIEMBRE 2026",
            location = "Complejo ArtLAB & Usina Inmersiva",
            imageUrl = "https://images.unsplash.com/photo-1508700115892-45ecd05ae2ad?auto=format&fit=crop&w=800&q=80",
            isLive = false,
            isFeatured = true,
            description = "Una experiencia multisensorial donde la inteligencia artificial dialoga con láseres hiper-direccionales y síntesis de partículas. Un laboratorio vivo para la exploración del espacio-tiempo a través del arte digital.",
            price = "$22.000 ARS",
            artists = listOf(
                Artist("a4", "Nonotak Studio", "Light & Sound Architecture", "Dúo francojaponés famoso por sus instalaciones kinetico-lumínicas."),
                Artist("a5", "Holly Herndon", "AI Vocal Synthesis & Choral Art", "Innovadora de la música generativa creada junto a IA.")
            ),
            stages = listOf(
                StageSchedule("s4", "VR Cube", "19:00 - 20:30", "Nonotak Studio", "Light Kinetic Performance", pulseIntensity = 82),
                StageSchedule("s5", "Main Cyber Stage", "21:00 - 22:30", "Holly Herndon", "AI Choir Symphony", pulseIntensity = 90)
            ),
            isFavorite = false,
            hasPass = false
        ),
        DigitalEvent(
            id = "ars-electronica-2026",
            title = "Ars Electronica XR Nexus",
            subtitle = "Ecología Digital, IA y Bio-Sintetizadores",
            category = Category.GENERATIVE,
            date = "05 - 07 DICIEMBRE 2026",
            location = "Pabellón Digital & Jardín Botánico Biotec",
            imageUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&w=800&q=80",
            isLive = true,
            isFeatured = false,
            description = "Ecosistemas cibernéticos y esculturas vivas que reaccionan a la frecuencia cardíaca de los visitantes. Explora la simbiósis entre biotecnología y código creativo.",
            price = "$15.000 ARS",
            artists = listOf(
                Artist("a6", "Refik Anadol", "AI Data Sculptures", "Creador de campos de datos cinéticos e ilusiones digitales masivas.")
            ),
            stages = listOf(
                StageSchedule("s6", "Jardín Biotec", "18:00 - 20:00", "Refik Anadol BioData Lab", "Algorithmic Ecology", pulseIntensity = 92)
            ),
            isFavorite = true,
            hasPass = false
        ),
        DigitalEvent(
            id = "mira-dome-2026",
            title = "MIRA Digital Dome 360",
            subtitle = "Performances Audiovisuales Imersivas",
            category = Category.AV_LIVE,
            date = "18 DICIEMBRE 2026",
            location = "Planetario Digital Interactivo",
            imageUrl = "https://images.unsplash.com/photo-1492684223066-81342ee5ff30?auto=format&fit=crop&w=800&q=80",
            isLive = false,
            isFeatured = false,
            description = "Visuales generativas en tiempo real sobre la cúpula geodésica con sonido espacial ambisónico 3D de 64 canales.",
            price = "$14.500 ARS",
            artists = listOf(
                Artist("a7", "Max Cooper", "3D Audio & Science Aesthetics", "Doctor en biología computacional que traduce la complejidad científica en techno emotivo.")
            ),
            stages = listOf(
                StageSchedule("s7", "Dome Geodésico", "22:00 - 00:00", "Max Cooper (Live 3D)", "AmbiSonic Techno", pulseIntensity = 96)
            ),
            isFavorite = false,
            hasPass = false
        )
    )

    private val sampleTickets = listOf(
        DigitalTicket(
            ticketId = "PASS-9982-MUTEK",
            eventId = "mutek-2026",
            eventTitle = "MUTEK CyberPulse 2026",
            date = "28 OCTUBRE 2026",
            location = "Dome 360°, CABA",
            accessZone = "VIP FULLDOME ACCESS",
            qrCodeSeed = "EVENTPULSE_MUTEK_VIP_SECURE_TOKEN_2026"
        )
    )

    private val eventsStateFlow = MutableStateFlow(sampleEvents)
    private val ticketsStateFlow = MutableStateFlow(sampleTickets)

    init {
        if (eventDao != null && ticketDao != null) {
            CoroutineScope(Dispatchers.IO).launch {
                if (eventDao.getEventCount() == 0) {
                    eventDao.insertEvents(sampleEvents.map { it.toEntity() })
                }
                if (ticketDao.getTicketCount() == 0) {
                    ticketDao.insertTickets(sampleTickets.map { it.toEntity() })
                }
            }
        }
    }

    override fun getEvents(): Flow<List<DigitalEvent>> {
        return eventDao?.getEvents()?.map { entities ->
            entities.map { it.toDomainModel() }
        } ?: eventsStateFlow.asStateFlow()
    }

    override fun getCategories(): List<Category> = Category.DEFAULT_CATEGORIES

    override fun getEventById(id: String): DigitalEvent? {
        return if (eventDao != null) {
            runBlocking(Dispatchers.IO) {
                eventDao.getEventById(id)?.toDomainModel()
            }
        } else {
            eventsStateFlow.value.find { it.id == id }
        }
    }

    override fun toggleFavorite(eventId: String) {
        if (eventDao != null) {
            CoroutineScope(Dispatchers.IO).launch {
                val current = eventDao.getEventById(eventId)
                if (current != null) {
                    eventDao.updateFavorite(eventId, !current.isFavorite)
                }
            }
        } else {
            eventsStateFlow.value = eventsStateFlow.value.map { event ->
                if (event.id == eventId) {
                    event.copy(isFavorite = !event.isFavorite)
                } else {
                    event
                }
            }
        }
    }

    override fun purchasePass(eventId: String): DigitalTicket? {
        val event = getEventById(eventId) ?: return null

        val newTicket = DigitalTicket(
            ticketId = "PASS-${(1000..9999).random()}-${event.id.take(4).uppercase()}",
            eventId = event.id,
            eventTitle = event.title,
            date = event.date,
            location = event.location,
            accessZone = "FULL ACCESS CYBER PASS",
            qrCodeSeed = "PULSE_${event.id}_${System.currentTimeMillis()}"
        )

        if (eventDao != null && ticketDao != null) {
            CoroutineScope(Dispatchers.IO).launch {
                eventDao.updateHasPass(eventId, true)
                ticketDao.insertTicket(newTicket.toEntity())
            }
        } else {
            eventsStateFlow.value = eventsStateFlow.value.map {
                if (it.id == eventId) it.copy(hasPass = true) else it
            }
            val updatedTickets = ticketsStateFlow.value.toMutableList().apply {
                if (none { it.eventId == eventId }) {
                    add(newTicket)
                }
            }
            ticketsStateFlow.value = updatedTickets
        }

        return newTicket
    }

    override fun getSavedTickets(): Flow<List<DigitalTicket>> {
        return ticketDao?.getTickets()?.map { entities ->
            entities.map { it.toDomainModel() }
        } ?: ticketsStateFlow.asStateFlow()
    }

    override fun getInitialEvents(): List<DigitalEvent> {
        return if (eventDao != null) {
            runBlocking(Dispatchers.IO) {
                val dbList = eventDao.getEventsSync()
                if (dbList.isNotEmpty()) dbList.map { it.toDomainModel() } else sampleEvents
            }
        } else {
            sampleEvents
        }
    }

    override fun getInitialTickets(): List<DigitalTicket> {
        return if (ticketDao != null) {
            runBlocking(Dispatchers.IO) {
                val dbList = ticketDao.getTicketsSync()
                if (dbList.isNotEmpty()) dbList.map { it.toDomainModel() } else sampleTickets
            }
        } else {
            sampleTickets
        }
    }
}

// ------------------- PREVIEWS -------------------

@Preview(name = "Event Repository Data Preview", showBackground = true, backgroundColor = 0xFF080B18)
@Composable
fun EventRepositoryDataPreview() {
    val repository = remember { EventRepositoryImpl() }
    val eventsState = (repository.getEvents() as? StateFlow<List<DigitalEvent>>)?.value ?: emptyList()
    val ticketsState = (repository.getSavedTickets() as? StateFlow<List<DigitalTicket>>)?.value ?: emptyList()

    EventPulseTheme {
        Column(
            modifier = Modifier
                .background(MidnightVoid)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "EVENT REPOSITORY DATA (${eventsState.size} Eventos)",
                style = MaterialTheme.typography.titleMedium,
                color = CyberCyan
            )
            eventsState.forEach { event ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkCharcoal)
                        .padding(10.dp)
                ) {
                    Text(text = event.title, style = MaterialTheme.typography.bodyLarge, color = SoftWhite, fontWeight = FontWeight.Bold)
                    Text(text = "${event.date} • ${event.price}", style = MaterialTheme.typography.labelSmall, color = MutedSilver)
                }
            }
            Text(
                text = "PASES GUARDADOS (${ticketsState.size})",
                style = MaterialTheme.typography.titleSmall,
                color = NeonMagenta
            )
            ticketsState.forEach { ticket ->
                Text(text = "🎟️ ${ticket.ticketId} - ${ticket.eventTitle}", style = MaterialTheme.typography.labelMedium, color = SoftWhite)
            }
        }
    }
}
