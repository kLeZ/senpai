package me.klez.senpai.persistence

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import me.klez.senpai.model.review.Review

@State(
	name = "ReviewPersistence",
	storages = [Storage(value = "review.ipr")]
)
class ReviewPersistence : PersistentStateComponent<Review> {
	private var stateValue = Review()

	override fun getState(): Review? {
		return stateValue
	}

	override fun loadState(state: Review) {
		stateValue = state
	}
}
