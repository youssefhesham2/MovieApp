package com.example.extensions

import com.example.data.entities.ErrorResponse
import com.example.data.entities.Results
import com.example.data.extensions.toDomainEntity
import com.example.domain.entities.ErrorResponseDomainEntity
import com.example.domain.entities.ResultsDomainEntity
import org.junit.Assert
import org.junit.Test

class DataMapperTest {
    @Test
    fun `given an Results DataEntity when Map to DomainEntity then return ResultsDomainEntity with same data`() {
        //arranges
        val resultDataEntity = Results(1, "tes1", "test1", "test1", "path", "release", 1.5)

        //action
        val actual = resultDataEntity.toDomainEntity()

        //assertion
        val expected = ResultsDomainEntity(1, "tes1", "test1", "test1", "path", "release", 1.5)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `given an ErrorResponse DataEntity when Map to DomainEntity then return ErrorResponseDomainEntity with same data`() {
        //arranges
        val errorResponseDataEntity = ErrorResponse(1, "tes1", true)

        //action
        val actual = errorResponseDataEntity.toDomainEntity()

        //assertion
        val expected = ErrorResponseDomainEntity(1, "tes1", true)
        Assert.assertEquals(expected, actual)
    }
}