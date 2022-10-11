package com.example.extensions

import com.example.data.BuildConfig
import com.example.data.extensions.resolvePosterPath
import com.example.data.utils.Constants
import org.junit.Assert
import org.junit.Test

class PosterPathResolveTest {
    @Test
    fun `given PosterPath without baseImageUrl when resolvePosterPath then must return baseImageUrl append to image size and PosterPath`() {
        //arranges
        val fakePosterPath = "fakePosterPath/jpg"

        //action
        val actual = fakePosterPath.resolvePosterPath()

        //assertion
        val expected = "${BuildConfig.BASE_IMAGE_PATH}${Constants.POSTER_SIZE}$fakePosterPath"
        Assert.assertEquals(expected, actual)
    }
}