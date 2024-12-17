import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import java.util.UUID

data class Wiki(
    val id: UUID? = null,
    val name: String,
    val url: String,
    val bannerImage: String,
    val default: Boolean
)

data class WikiUser(
    val wikiId: UUID,
    val userId: UUID,
    val lastOpened: Instant
)

class SupabaseDBHandler {

    fun pullDatabase() {

    }

    fun pushDatabase() {

    }
}