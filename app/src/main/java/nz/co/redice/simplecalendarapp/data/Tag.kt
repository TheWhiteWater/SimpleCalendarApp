package nz.co.redice.simplecalendarapp.data

data class Tag(
    val tagId: Int,
    val title: String,
    val events: List<Event>
)