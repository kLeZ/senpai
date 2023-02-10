package me.klez.senpai.model.review.comment

import me.klez.senpai.model.ValueObject

abstract class ReviewComment : ValueObject() {
    var details = ReviewCommentDetails()
}
