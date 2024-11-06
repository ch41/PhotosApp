package com.example.photosapp.data.mapper

import com.example.photosapp.data.localdb.comments.CommentsItemEntity
import com.example.photosapp.data.localdb.gallery.GalleryItemEntity
import com.example.photosapp.data.model.auth.SignInBody
import com.example.photosapp.data.model.auth.SignInResponseBody
import com.example.photosapp.data.model.auth.SignUpBody
import com.example.photosapp.data.model.auth.SignUpResponseBody
import com.example.photosapp.data.model.comment.GetCommentResponse
import com.example.photosapp.data.model.comment.PostCommentBody
import com.example.photosapp.data.model.comment.PostCommentResponse
import com.example.photosapp.data.model.images.ImageResponse
import com.example.photosapp.data.model.images.PostImageBody
import com.example.photosapp.data.model.images.PostImageResponse
import com.example.photosapp.domain.model.images.PostImage
import com.example.photosapp.domain.model.images.PostImageDto
import com.example.photosapp.domain.model.auth.SignIn
import com.example.photosapp.domain.model.auth.SignInResponse
import com.example.photosapp.domain.model.auth.SignUp
import com.example.photosapp.domain.model.auth.SignUpResponse
import com.example.photosapp.domain.model.comment.CommentDto
import com.example.photosapp.domain.model.comment.PostComment
import com.example.photosapp.domain.model.comment.PostCommentDto
import com.example.photosapp.domain.model.images.ImagesDto

fun SignIn.toRequestBody() = SignInBody(login, password)

fun SignUp.toRequestBody() = SignUpBody(login, password)

fun SignInResponseBody.toDomain() = SignInResponse(
    SignInResponse.Data(
        this.data.login,
        this.data.token,
        this.data.userId
    ),
    this.status
)

fun SignUpResponseBody.toDomain() = SignUpResponse(
    SignUpResponse.Data(
        this.data.login,
        this.data.token,
        this.data.userId
    ),
    this.status
)

fun PostImageBody.toDomain() = PostImage(base64Image, date, lat, lng)

fun PostImageResponse.toDomain() = PostImageDto(
    id = this.data.id,
    url = this.data.url,
    date = this.data.date,
    lat = this.data.lat,
    lng = this.data.lng
)

fun PostImage.toRequestBody() = PostImageBody(base64Image, date, lat, lng)

fun List<ImageResponse>.toDomain(): List<ImagesDto> {
    return this.map { imageResponse ->
        ImagesDto(
            date = imageResponse.date,
            id = imageResponse.id,
            lat = imageResponse.lat,
            lng = imageResponse.lng,
            url = imageResponse.url
        )
    }
}

fun List<GalleryItemEntity>.entityToDomain(): List<ImagesDto> {
    return this.map { localImageItem ->
        ImagesDto(
            date = localImageItem.date,
            id = localImageItem.id,
            lat = localImageItem.lat,
            lng = localImageItem.lng,
            url = localImageItem.url
        )
    }
}

fun GalleryItemEntity.toDomain(): ImagesDto = ImagesDto(date, id, lat, lng, url)
fun ImagesDto.toLocalDbEntity() : GalleryItemEntity = GalleryItemEntity(date, id, lat, lng, url)
fun List<ImagesDto>.domainToLocalEntity(): List<GalleryItemEntity> {
    return this.map { imageDto ->
        GalleryItemEntity(
            date = imageDto.date,
            id = imageDto.id,
            lat = imageDto.lat,
            lng = imageDto.lng,
            url = imageDto.url
        )
    }
}

fun PostCommentResponse.toDomain(): PostCommentDto = PostCommentDto(
    data = PostCommentDto.Data(
        date = this.data.date,
        id = this.data.id,
        text = this.data.text
    ),
    status = this.status
)

fun PostComment.toRequestBody(): PostCommentBody = PostCommentBody(text = commentText)

fun List<GetCommentResponse.Comment>.responseToDomain(): List<CommentDto> {
    return this.map { comment ->
        CommentDto(
            date = comment.date,
            id = comment.id,
            text = comment.text
        )
    }
}

fun List<CommentDto>.dtoToEntity(imageId: Int): List<CommentsItemEntity> {
    return this.map {
        CommentsItemEntity(
            date = it.date,
            id = it.id,
            text = it.text,
            galleryId = imageId
        )
    }
}