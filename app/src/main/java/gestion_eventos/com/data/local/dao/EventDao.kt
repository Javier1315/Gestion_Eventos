package gestion_eventos.com.data.local.dao

import gestion_eventos.com.data.local.entity.EventEntity

interface EventDao {
    suspend fun insert(event: EventEntity)
    suspend fun update(event: EventEntity)
    suspend fun findAll(): List<EventEntity>
    suspend fun deleteById(eventId: String)
}
