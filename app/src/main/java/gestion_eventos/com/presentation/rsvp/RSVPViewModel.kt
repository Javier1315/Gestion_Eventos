package gestion_eventos.com.presentation.rsvp

class RSVPViewModel {
    var status = RSVPStatus.PENDING
        private set

    fun confirm() {
        status = RSVPStatus.CONFIRMED
    }

    fun decline() {
        status = RSVPStatus.DECLINED
    }
}
