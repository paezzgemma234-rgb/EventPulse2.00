package com.seminario2026.eventpulse.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seminario2026.eventpulse.data.EventRepository
import com.seminario2026.eventpulse.data.EventRepositoryImpl
import com.seminario2026.eventpulse.model.Category
import com.seminario2026.eventpulse.model.DigitalEvent
import com.seminario2026.eventpulse.model.DigitalTicket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

enum class DateFilter(val label: String) {
    ALL("Cualquier Fecha"),
    LIVE_NOW("En Vivo Ahora"),
    OCTOBER("Octubre 2026"),
    NOVEMBER("Noviembre 2026"),
    DECEMBER("Diciembre 2026")
}

class MainViewModel(
    private val repository: EventRepository = EventRepositoryImpl()
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow(Category.ALL)
    val selectedCategory: StateFlow<Category> = _selectedCategory.asStateFlow()

    private val _selectedDateFilter = MutableStateFlow(DateFilter.ALL)
    val selectedDateFilter: StateFlow<DateFilter> = _selectedDateFilter.asStateFlow()

    val categories: List<Category> = repository.getCategories()
    val dateFilters: List<DateFilter> = DateFilter.values().toList()

    private val initialEvents: List<DigitalEvent> = repository.getInitialEvents()
    private val initialTickets: List<DigitalTicket> = repository.getInitialTickets()

    val allEvents: StateFlow<List<DigitalEvent>> = repository.getEvents()
        .stateIn(viewModelScope, SharingStarted.Eagerly, initialEvents)

    val filteredEvents: StateFlow<List<DigitalEvent>> = combine(
        allEvents,
        _searchQuery,
        _selectedCategory,
        _selectedDateFilter
    ) { events, query, category, dateFilter ->
        events.filter { event ->
            val matchesQuery = query.isEmpty() ||
                    event.title.contains(query, ignoreCase = true) ||
                    event.subtitle.contains(query, ignoreCase = true) ||
                    event.artists.any { it.name.contains(query, ignoreCase = true) }

            val matchesCategory = category.id == Category.ALL.id || event.category.id == category.id

            val matchesDate = when (dateFilter) {
                DateFilter.ALL -> true
                DateFilter.LIVE_NOW -> event.isLive
                DateFilter.OCTOBER -> event.date.contains("OCTUBRE", ignoreCase = true)
                DateFilter.NOVEMBER -> event.date.contains("NOVIEMBRE", ignoreCase = true)
                DateFilter.DECEMBER -> event.date.contains("DICIEMBRE", ignoreCase = true)
            }

            matchesQuery && matchesCategory && matchesDate
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, initialEvents)

    val featuredEvents: StateFlow<List<DigitalEvent>> = combine(allEvents) { events ->
        events[0].filter { it.isFeatured }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, initialEvents.filter { it.isFeatured })

    val liveEvents: StateFlow<List<DigitalEvent>> = combine(allEvents) { events ->
        events[0].filter { it.isLive }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, initialEvents.filter { it.isLive })

    val favoriteEvents: StateFlow<List<DigitalEvent>> = combine(allEvents) { events ->
        events[0].filter { it.isFavorite }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, initialEvents.filter { it.isFavorite })

    val savedTickets: StateFlow<List<DigitalTicket>> = repository.getSavedTickets()
        .stateIn(viewModelScope, SharingStarted.Eagerly, initialTickets)

    val homeUiState: StateFlow<UiState<List<DigitalEvent>>> = combine(filteredEvents) { events ->
        val list = events[0]
        if (list.isEmpty()) {
            UiState.Empty("No hay eventos que coincidan con la búsqueda o filtro seleccionado.")
        } else {
            UiState.Success(list)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        if (initialEvents.isEmpty()) UiState.Empty("No hay eventos que coincidan con la búsqueda o filtro seleccionado.") else UiState.Success(initialEvents)
    )

    val savedTicketsUiState: StateFlow<UiState<List<DigitalTicket>>> = combine(savedTickets) { tickets ->
        val list = tickets[0]
        if (list.isEmpty()) {
            UiState.Empty("No tienes pases de entrada activos.")
        } else {
            UiState.Success(list)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        if (initialTickets.isEmpty()) UiState.Empty("No tienes pases de entrada activos.") else UiState.Success(initialTickets)
    )

    val favoriteEventsUiState: StateFlow<UiState<List<DigitalEvent>>> = combine(favoriteEvents) { favorites ->
        val list = favorites[0]
        if (list.isEmpty()) {
            UiState.Empty("No has marcado eventos como favoritos.")
        } else {
            UiState.Success(list)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        if (initialEvents.none { it.isFavorite }) UiState.Empty("No has marcado eventos como favoritos.") else UiState.Success(initialEvents.filter { it.isFavorite })
    )

    private val _selectedScheduleDate = MutableStateFlow("TODAS")
    val selectedScheduleDate: StateFlow<String> = _selectedScheduleDate.asStateFlow()

    val availableScheduleDates: StateFlow<List<String>> = combine(allEvents) { events ->
        listOf("TODAS") + events[0].map { it.date }.distinct()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, listOf("TODAS") + initialEvents.map { it.date }.distinct())

    fun onScheduleDateSelected(date: String) {
        _selectedScheduleDate.value = date
    }

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onCategorySelected(category: Category) {
        _selectedCategory.value = category
    }

    fun onDateFilterSelected(dateFilter: DateFilter) {
        _selectedDateFilter.value = dateFilter
    }

    fun toggleFavorite(eventId: String) {
        repository.toggleFavorite(eventId)
    }

    fun purchasePass(eventId: String): DigitalTicket? {
        return repository.purchasePass(eventId)
    }

    fun getEventById(eventId: String): DigitalEvent? {
        return repository.getEventById(eventId)
    }
}
