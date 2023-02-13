package me.klez.senpai.model.review.comment

class ReviewGeneralComment() : ReviewComment() {
	constructor(_details: ReviewCommentDetails) : this() {
		details = _details
	}

	fun clone(): ReviewGeneralComment {
		return ReviewGeneralComment(details.clone())
	}
}
