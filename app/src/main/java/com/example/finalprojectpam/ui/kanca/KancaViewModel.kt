package com.example.finalprojectpam.ui.kanca

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectpam.data.model.ScoreRecord
import com.example.finalprojectpam.data.model.Story
import com.example.finalprojectpam.data.model.UserProfile
import com.example.finalprojectpam.data.repository.StoryRepository
import com.example.finalprojectpam.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ChatMessage(
    val text: String,
    val isFromUser: Boolean,
    val time: String
)

enum class QuickReply(val title: String, val subtitle: String, val userText: String) {
    TARGET(
        title    = "Target Hari Ini",
        subtitle = "Apakah anak menyelesaikan misinya?",
        userText = "Apakah anak saya sudah menyelesaikan misinya hari ini?"
    ),
    PERKEMBANGAN(
        title    = "Lihat Perkembangan",
        subtitle = "Ringkasan aktivitas anak",
        userText = "Tolong tampilkan perkembangan anak saya."
    ),
    REKOMENDASI(
        title    = "Rekomendasi Cerita",
        subtitle = "Cerita yang cocok berikutnya",
        userText = "Cerita apa yang kamu rekomendasikan untuk anak saya selanjutnya?"
    )
}

class KancaViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository  = UserRepository()
    private val storyRepository = StoryRepository(application)

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var userProfile: UserProfile? = null
    private var allScores:   List<ScoreRecord> = emptyList()
    private var allStories:  List<Story>       = emptyList()

    init {
        allStories = storyRepository.loadAllStories()
        viewModelScope.launch {
            userProfile = userRepository.getUserProfile()
            allScores   = userRepository.getAllScores()
            // Sapa pertama dari KANCA
            addKancaMessage(buildGreeting())
        }
    }

    fun onQuickReply(reply: QuickReply) {
        addUserMessage(reply.userText)
        addKancaMessage(buildResponse(reply))
    }

    // ── Response builder ───────────────────────────────────────────────────────

    private fun buildGreeting(): String {
        val name = userProfile?.name?.split(" ")?.firstOrNull() ?: "Anak Anda"
        return "Halo, orang tua $name! 👋 Saya KANCA, asisten yang menemani anak Anda belajar nilai keuangan bersama Kimo. Ada yang ingin Anda ketahui? Pilih pertanyaan di bawah ini! ✨"
    }

    private fun buildResponse(reply: QuickReply): String {
        val profile = userProfile
        val completed = profile?.storiesCompleted ?: 0
        val totalXp   = profile?.totalScore ?: 0
        val level     = totalXp / 100 + 1

        val avgPct = if (allScores.isEmpty()) 0
        else allScores.map { (it.score.coerceAtLeast(0) * 100) / it.maxScore.coerceAtLeast(1) }
            .average().toInt()

        return when (reply) {

            QuickReply.TARGET -> {
                if (completed == 0) {
                    "Anak Anda belum memainkan cerita apapun 🙁 Coba ajak mereka membuka KANCA dan memulai petualangan pertama bersama Kimo! Cerita \"Uang Saku Budi\" adalah pilihan yang sempurna untuk pemula. 🌟"
                } else {
                    val lastStory = allScores.firstOrNull()
                    val lastInfo = if (lastStory != null) {
                        val pct = (lastStory.score.coerceAtLeast(0) * 100) / lastStory.maxScore.coerceAtLeast(1)
                        "\n\nCerita terakhir yang dimainkan: \"${lastStory.storyTitle}\" dengan skor $pct%. ${if (pct >= 80) "Luar biasa!" else if (pct >= 50) "Cukup baik!" else "Masih ada ruang untuk berkembang!"}"
                    } else ""
                    "Anak Anda sudah menyelesaikan $completed cerita dan mengumpulkan total $totalXp XP! 🎉 Mereka menunjukkan semangat belajar yang luar biasa.$lastInfo"
                }
            }

            QuickReply.PERKEMBANGAN -> {
                val successLabel = when {
                    avgPct >= 80 -> "Sangat Baik 🏆"
                    avgPct >= 60 -> "Baik 👍"
                    avgPct >= 40 -> "Cukup 📈"
                    else         -> "Perlu Semangat 💪"
                }
                "Berikut ringkasan perkembangan anak Anda:\n\n" +
                "📚 Cerita selesai: $completed\n" +
                "⭐ Total XP: $totalXp\n" +
                "🏅 Level saat ini: Level $level\n" +
                "📊 Rata-rata skor: $avgPct% ($successLabel)\n\n" +
                if (completed == 0)
                    "Anak Anda belum mulai bermain. Yuk ajak mereka berpetualangan bersama Kimo! 🌟"
                else
                    "Anak Anda sudah belajar nilai ${buildLearnedValues()}. Terus dukung semangat belajarnya ya!"
            }

            QuickReply.REKOMENDASI -> {
                val playedIds = allScores.map { it.storyId }.toSet()
                val unplayed  = allStories.filter { it.id !in playedIds }

                if (allStories.isEmpty()) {
                    "Belum ada cerita tersedia saat ini. Pastikan koneksi internet aktif dan coba buka kembali aplikasinya. 📶"
                } else if (unplayed.isEmpty()) {
                    val best = allScores.minByOrNull {
                        (it.score.coerceAtLeast(0) * 100) / it.maxScore.coerceAtLeast(1)
                    }
                    "Anak Anda sudah memainkan semua cerita yang tersedia — luar biasa! 🎊\n\nSaya sarankan untuk memainkan kembali \"${best?.storyTitle ?: "cerita"}\" untuk meningkatkan skor dan memperkuat pemahaman nilainya. 💪"
                } else {
                    val next = unplayed.first()
                    val emoji = when (next.category.lowercase()) {
                        "menabung"  -> "🪙"
                        "kejujuran" -> "⭐"
                        "berbagi"   -> "❤️"
                        "bijak"     -> "💡"
                        else        -> "📖"
                    }
                    "Saya merekomendasikan cerita berikutnya:\n\n$emoji \"${next.title}\"\nTema: ${next.category.replaceFirstChar { it.uppercase() }} · ${next.difficulty.replaceFirstChar { it.uppercase() }}\n\n${next.description}\n\nCerita ini akan mengajarkan nilai ${next.category} dengan cara yang menyenangkan!"
                }
            }
        }
    }

    private fun buildLearnedValues(): String {
        val categories = allScores
            .mapNotNull { record -> allStories.find { it.id == record.storyId }?.category }
            .distinct()
        return when (categories.size) {
            0    -> "keuangan"
            1    -> categories[0]
            2    -> "${categories[0]} dan ${categories[1]}"
            else -> categories.dropLast(1).joinToString(", ") + ", dan ${categories.last()}"
        }
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    private fun now() = SimpleDateFormat("HH:mm", Locale("id")).format(Date())

    private fun addKancaMessage(text: String) {
        _messages.value = _messages.value + ChatMessage(text, isFromUser = false, time = now())
    }

    private fun addUserMessage(text: String) {
        _messages.value = _messages.value + ChatMessage(text, isFromUser = true, time = now())
    }
}
