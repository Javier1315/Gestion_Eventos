package gestion_eventos.com.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gestion_eventos.com.data.repository.HistoryRepositoryImpl
import gestion_eventos.com.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val historyRepository: HistoryRepository = HistoryRepositoryImpl()
) : ViewModel() {
    // Carga historial y estadisticas del usuario autenticado.
    private val _state = MutableStateFlow(HistoryState())
    val state: StateFlow<HistoryState> = _state.asStateFlow()

    fun loadHistory(userId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            runCatching {
                val events = historyRepository.getAttendedEvents(userId)
                val stats = historyRepository.getStats(userId)
                events to stats
            }.onSuccess { (events, stats) ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        attendedEvents = events,
                        stats = stats,
                        error = null
                    )
                }
            }.onFailure { throwable ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = throwable.message ?: "No se pudo cargar el historial."
                    )
                }
            }
        }
    }
}
