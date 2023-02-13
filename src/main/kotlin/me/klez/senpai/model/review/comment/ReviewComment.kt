package me.klez.senpai.model.review.comment

import me.klez.senpai.model.ValueObject

abstract class ReviewComment : ValueObject() {
	var details = ReviewCommentDetails()
	val empty: Boolean
		get() {
			val tags = this.details.tags
			val description = this.details.description
			return tags.isEmpty() && description.isBlank()
		}
}
