package com.breakingchains.app.data.model

data class Milestone(
    val title: String,
    val subtitle: String,
    val requiredDays: Int,
    val isUnlocked: Boolean,
    val iconType: MilestoneIcon
)

enum class MilestoneIcon {
    MEDAL,
    RIBBON,
    LOCK
}
